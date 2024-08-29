package com.bs.sriwilis.ui.homepage.operation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CatalogAdapter
import com.bs.sriwilis.adapter.CategoryAdapter
import com.bs.sriwilis.adapter.UserAdapter
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.data.preference.dataStore
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityManageCatalogBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ManageCatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageCatalogBinding
    private val viewModel by viewModels<ManageCatalogViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var token: String? = null
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        catalogAdapter = CatalogAdapter(emptyList(), this)

        binding.apply {
            fabAddCatalog.setOnClickListener {
                val intent = Intent(this@ManageCatalogActivity, AddCatalogActivity::class.java)
                startActivity(intent)
            }
            btnBack.setOnClickListener { finish() }
        }
        observeCatalog()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvCatalog.layoutManager = LinearLayoutManager(this)
        binding.rvCatalog.adapter = catalogAdapter

        val userPreferences = UserPreferences.getInstance(this.dataStore)
        lifecycleScope.launch {
            token = userPreferences.token.first()
            token?.let {
                viewModel.getCatalog()
            }
        }
    }

    private fun observeCatalog() {
        viewModel.catalog.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val catalogDetails = result.data.data ?: emptyList()
                    catalogAdapter.updateCatalog(catalogDetails)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
        viewModel.getCatalog()
    }
}
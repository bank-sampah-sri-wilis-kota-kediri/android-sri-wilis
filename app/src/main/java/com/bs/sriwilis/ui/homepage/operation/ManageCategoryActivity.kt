package com.bs.sriwilis.ui.homepage.operation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CategoryAdapter
import com.bs.sriwilis.adapter.UserAdapter
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.data.preference.dataStore
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityManageCategoryBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ManageCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageCategoryBinding
    private lateinit var categoryLauncher: ActivityResultLauncher<Intent>


    private val viewModel by viewModels<ManageCategoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var token: String? = null
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryAdapter = CategoryAdapter(emptyList(), this)

        categoryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getCategory()
            }
        }

        binding.apply {
            fabAddCategory.setOnClickListener {
                val intent = Intent(this@ManageCategoryActivity, AddCategoryActivity::class.java)
                categoryLauncher.launch(intent)
            }
            btnBack.setOnClickListener { finish() }
        }
        observeCategory()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        binding.rvCategory.adapter = categoryAdapter
    }

    private fun observeCategory() {
        viewModel.categories.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val categoryDetails = result.data
                    if (categoryDetails != null) {
                        categoryAdapter.updateCategory(categoryDetails)
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                else -> {}
            }
        }
        viewModel.getCategory()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCategory()
    }
}
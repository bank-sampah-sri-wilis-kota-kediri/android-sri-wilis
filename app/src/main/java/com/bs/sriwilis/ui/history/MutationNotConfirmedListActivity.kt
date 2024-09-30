package com.bs.sriwilis.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CatalogAdapter
import com.bs.sriwilis.adapter.CategoryAdapter
import com.bs.sriwilis.adapter.MutationNotConfirmedAdapter
import com.bs.sriwilis.databinding.ActivityMutationNotConfirmedListBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch

class MutationNotConfirmedListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMutationNotConfirmedListBinding
    private val viewModel by viewModels<ManageHistoryMutationViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var mutationAdapter: MutationNotConfirmedAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMutationNotConfirmedListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mutationAdapter = MutationNotConfirmedAdapter(emptyList(), this)

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }

        setupRecyclerView()
        setupDataMutation()
        setupViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvMutation.layoutManager = LinearLayoutManager(this)
        binding.rvMutation.adapter = mutationAdapter
    }

    private fun setupDataMutation() {
        viewModel.historyMutation.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val dataMutation = result.data?.filter { it.statusPenarikan?.lowercase() == "diproses" }
                    if (dataMutation != null) {
                        mutationAdapter.updateMutation(dataMutation)
                    }else {
                        mutationAdapter.updateMutation(emptyList())
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                else -> {}
            }
        })
    }

    private fun setupViewModel() {
        viewModel.editCategoryResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Update Status Penarikan Berhasil")
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Update Status Penarikan Gagal")
                }

                else -> {}
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getAllMutation()
        }
    }

}
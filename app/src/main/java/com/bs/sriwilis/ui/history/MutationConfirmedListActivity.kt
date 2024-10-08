package com.bs.sriwilis.ui.history

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.MutationConfirmedAdapter
import com.bs.sriwilis.adapter.MutationNotConfirmedAdapter
import com.bs.sriwilis.databinding.ActivityMutationConfirmedListBinding
import com.bs.sriwilis.databinding.ActivityMutationNotConfirmedListBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch

class MutationConfirmedListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMutationConfirmedListBinding
    private val viewModel by viewModels<ManageHistoryMutationViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var mutationAdapter: MutationConfirmedAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMutationConfirmedListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mutationAdapter = MutationConfirmedAdapter(emptyList(), this)

        binding.apply {
            btnBack.setOnClickListener { finish() }

            swipeRefreshLayout.setOnRefreshListener {
                lifecycleScope.launch {
                    viewModel.syncData()
                    viewModel.getAllMutation()
                }
            }
        }

        setupRecyclerView()
        setupDataMutation()
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
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.progressBar.visibility = View.GONE

                    val dataMutation = result.data?.filter { it?.status_penarikan == "Gagal" || it?.status_penarikan == "Berhasil" }
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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { viewModel.getAllMutation() }
    }
}
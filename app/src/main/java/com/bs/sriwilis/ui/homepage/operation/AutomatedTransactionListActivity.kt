package com.bs.sriwilis.ui.homepage.operation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.OrderAutomatedAdapter
import com.bs.sriwilis.adapter.OrderScheduledAdapter
import com.bs.sriwilis.databinding.ActivityAutomatedTransactionListBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.scheduling.OrderUnscheduledFragment
import com.bs.sriwilis.ui.settings.SettingViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch

class AutomatedTransactionListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAutomatedTransactionListBinding
    private lateinit var adapter: OrderAutomatedAdapter

    private val viewModel by viewModels<ManageTransactionAutomateViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutomatedTransactionListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            observeOrder()
            setupUI()
        }

    }

    private suspend fun setupUI() {
        adapter = OrderAutomatedAdapter(emptyList(), this, viewModel)

        binding.rvScheduledTask.layoutManager = LinearLayoutManager(this)
        binding.rvScheduledTask.adapter = adapter

        viewModel.pesananSampahEntities.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    result.data.let { adapter.updateOrder(it) }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Gagal memuat data: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("Loading", "Loading")
                }
            }
        }
        viewModel.getPesananSampahKeranjangScheduled()
    }

    private suspend fun observeOrder() {
        viewModel.pesananSampahEntities.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.bindingSwipe.isRefreshing = false
                    result.data.let { adapter.updateOrder(it) }
                }
                is Result.Error -> {
                    binding.bindingSwipe.isRefreshing = false
                    Toast.makeText(this, "Gagal memuat data: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Log.d("Loading", "Loading")
                }
            }
        }
    }
}
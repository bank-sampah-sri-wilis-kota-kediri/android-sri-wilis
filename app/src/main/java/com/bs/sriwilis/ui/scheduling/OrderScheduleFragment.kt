package com.bs.sriwilis.ui.scheduling

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.OrderScheduledAdapter
import com.bs.sriwilis.adapter.OrderUnscheduledAdapter
import com.bs.sriwilis.databinding.FragmentOrderScheduleBinding
import com.bs.sriwilis.databinding.FragmentSettingsBinding
import com.bs.sriwilis.helper.InjectionMain
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.settings.SettingViewModel
import com.bs.sriwilis.utils.OrderSchedulingViewModelFactory
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch

class OrderScheduleFragment : Fragment() {

    private var _binding: FragmentOrderScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OrderScheduledAdapter
    private lateinit var viewModel: OrderSchedulingViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val repository = InjectionMain.provideRepository(requireContext())
            viewModel =
                ViewModelProvider(requireActivity(), OrderSchedulingViewModelFactory(repository))[OrderSchedulingViewModel::class.java]
            setupUI()
        }

        lifecycleScope.launch { observeOrder() }

        binding.apply {
            cvUnscheduled.setOnClickListener {
                replaceFragment(OrderUnscheduledFragment())
            }
        }

        binding.bindingSwipe.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.syncData()
                viewModel.getPesananSampahKeranjangScheduled()
            }
        }
    }

    private suspend fun setupUI() {
        adapter = OrderScheduledAdapter(emptyList(), requireContext(), viewModel)

        binding.rvScheduledTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScheduledTask.adapter = adapter

        viewModel.pesananSampahEntities.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    result.data.let { adapter.updateOrder(it) }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Gagal memuat data: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("Loading", "Loading")
                }
            }
        }

        viewModel.getPesananSampahKeranjangScheduled()
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun observeOrder() {
        viewModel.pesananSampahEntities.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.bindingSwipe.isRefreshing = false
                    result.data.let { adapter.updateOrder(it) }
                }
                is Result.Error -> {
                    binding.bindingSwipe.isRefreshing = false
                    Toast.makeText(requireContext(), "Gagal memuat data: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Log.d("Loading", "Loading")
                }
            }
        }
        viewModel.getPesananSampahKeranjangScheduled()
    }
}

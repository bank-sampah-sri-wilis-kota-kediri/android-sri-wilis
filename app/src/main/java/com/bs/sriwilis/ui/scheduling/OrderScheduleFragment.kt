package com.bs.sriwilis.ui.scheduling

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.bs.sriwilis.ui.settings.SettingViewModel
import com.bs.sriwilis.utils.OrderSchedulingViewModelFactory
import com.bs.sriwilis.utils.OrderUnschedulingViewModelFactory
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

        binding.apply {
            cvUnscheduled.setOnClickListener {
                replaceFragment(OrderUnscheduledFragment())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val repository = InjectionMain.provideRepository(requireContext())
            viewModel =
                ViewModelProvider(requireActivity(), OrderSchedulingViewModelFactory(repository))[OrderSchedulingViewModel::class.java]
            setupUI()
        }
    }

    private fun setupUI() {
        adapter = OrderScheduledAdapter(emptyList(), requireContext(), viewModel)

        binding.rvScheduledTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScheduledTask.adapter = adapter

        viewModel.scheduledOrdersLiveData.observe(viewLifecycleOwner) { orders ->
            if (isAdded) {
                if (orders != null) {
                    adapter.updateCatalog(orders)
                } else {
                    adapter.updateCatalog(emptyList())
                }
            }
        }

        viewModel.fetchScheduledOrders()
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
}

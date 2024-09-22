package com.bs.sriwilis.ui.scheduling

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.OrderScheduledAdapter
import com.bs.sriwilis.adapter.OrderUnscheduledAdapter
import com.bs.sriwilis.databinding.FragmentOrderScheduleBinding
import com.bs.sriwilis.databinding.FragmentOrderUnscheduledBinding
import com.bs.sriwilis.helper.InjectionMain
import com.bs.sriwilis.utils.OrderUnschedulingViewModelFactory
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch

class OrderUnscheduledFragment : Fragment() {

    private var _binding: FragmentOrderUnscheduledBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OrderUnscheduledAdapter
    private lateinit var viewModel: OrderUnschedulingViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderUnscheduledBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            binding.cvScheduled.setOnClickListener {
                replaceFragment(OrderScheduleFragment())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val repository = InjectionMain.provideRepository(requireContext())
            viewModel =
                ViewModelProvider(requireActivity(), OrderUnschedulingViewModelFactory(repository))[OrderUnschedulingViewModel::class.java]
            setupUI()
        }
    }

    private fun setupUI() {
        adapter = OrderUnscheduledAdapter(emptyList(), requireContext(), viewModel)

        binding.rvUnscheduledTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUnscheduledTask.adapter = adapter

        viewModel.unscheduledOrdersLiveData.observe(viewLifecycleOwner) { orders ->
            if (isAdded) {
                if (orders != null) {
                    adapter.updateOrder(orders)
                } else {
                    adapter.updateOrder(emptyList())
                }
            }
        }

        viewModel.fetchUnscheduledOrders()
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

package com.bs.sriwilis.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.HistoryOrderAdapter
import com.bs.sriwilis.adapter.OrderScheduledAdapter
import com.bs.sriwilis.databinding.FragmentHistoryOrderBinding
import com.bs.sriwilis.databinding.FragmentHomeBinding
import com.bs.sriwilis.helper.InjectionMain
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.homepage.operation.ManageCatalogViewModel
import com.bs.sriwilis.ui.scheduling.OrderSchedulingViewModel
import com.bs.sriwilis.utils.HistoryViewModelFactory
import com.bs.sriwilis.utils.OrderSchedulingViewModelFactory
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch

class HistoryOrderFragment : Fragment() {

    private var _binding: FragmentHistoryOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ManageHistoryOrderViewModel
    private lateinit var adapter: HistoryOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cvMutationFilter.setOnClickListener {
                replaceFragment(HistoryMutationFragment())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val repository = InjectionMain.provideRepository(requireContext())
            viewModel =
                ViewModelProvider(requireActivity(), HistoryViewModelFactory(repository))[ManageHistoryOrderViewModel::class.java]
            setupUI()
        }
    }

    private fun setupUI() {
        adapter = HistoryOrderAdapter(emptyList(), requireContext())

        binding.rvOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrder.adapter = adapter

        viewModel.historyOrders.observe(viewLifecycleOwner) { orders ->
            if (isAdded) {
                if (orders != null) {
                    adapter.updateOrder(orders)
                } else {
                    adapter.updateOrder(emptyList())
                }
            }
        }

        viewModel.getAllTransaction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_history_mutation, fragment)
            .addToBackStack(null)
            .commit()
    }

    /*private fun observeCatalog() {
        viewModel.catalog.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val catalogDetails = result.data
                    catalogAdapter.updateCatalog(catalogDetails)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
        viewModel.getCatalog()
    }*/
}
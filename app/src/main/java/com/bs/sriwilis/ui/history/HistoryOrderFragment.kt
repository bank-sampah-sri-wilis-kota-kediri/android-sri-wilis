package com.bs.sriwilis.ui.history

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.bs.sriwilis.ui.homepage.HomeViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageCatalogViewModel
import com.bs.sriwilis.ui.scheduling.OrderSchedulingViewModel
import com.bs.sriwilis.utils.HistoryViewModelFactory
import com.bs.sriwilis.utils.OrderSchedulingViewModelFactory
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[ManageHistoryOrderViewModel::class.java]

        setupUI()
        viewModel.getCombinedTransaction()
        observeOrder()

        binding.apply {
            cvMutationFilter.setOnClickListener {
                replaceFragment(HistoryMutationFragment())
            }
        }

        binding.tvSelectDate.setOnClickListener {
            showDatePicker()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.syncDataTransaction()
                viewModel.getCombinedTransaction()
                observeOrder()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupUI() {
        adapter = HistoryOrderAdapter(emptyList(), requireContext())

        binding.rvOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrder.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun observeOrder() {
        viewModel.historyOrders.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val categoryDetails = result.data
                    if (categoryDetails != null) {
                        adapter.updateOrder(categoryDetails)
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                else -> {}
            }
            viewModel.getCombinedTransaction()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                binding.tvSelectDate.text = selectedDate
                filterByDate(selectedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun filterByDate(selectedDate: String) {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val parsedSelectedDate = inputFormat.parse(selectedDate)
            val formattedSelectedDate = outputFormat.format(parsedSelectedDate)

            viewModel.historyOrders.observe(viewLifecycleOwner) { result ->
                if (result is Result.Success) {
                    val filteredData = result.data?.filter { order ->
                        val orderDate = order.tanggal
                        orderDate == formattedSelectedDate
                    }
                    if (filteredData != null) {
                        adapter.updateOrder(filteredData)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Tanggal tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

}
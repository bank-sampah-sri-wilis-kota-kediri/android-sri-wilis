package com.bs.sriwilis.ui.homepage.operation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CartTransactionAdapter
import com.bs.sriwilis.databinding.ActivityAddTransactionBinding
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.model.CartTransaction
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTransaction : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private val viewModel by viewModels<ManageTransactionViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var spinner: Spinner

    private var selectedId: String? = null
    private var selectedDate: String? = null

    private var totalWeight: Int = 0
    private var totalPrice: Float = 0.0f
    private var cartItems = mutableListOf<CartTransaction>()

    private lateinit var adapter: CartTransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinnerNasabahChoose

        val recyclerView: RecyclerView = findViewById(R.id.rv_transaction_cart)
        val cartItems = mutableListOf<CartTransaction>() // pesanansampah

        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.tvSelectedDate.text = dateFormat.format(currentDate)

        adapter = CartTransactionAdapter(cartItems)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        getUserId()
        setupDatePicker()

        binding.apply {
            btnAddCart.setOnClickListener {
                val intent = Intent(this@AddTransaction, AddCartTransactionActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_ADD_CART)
            }
            btnBack.setOnClickListener { finish() }
            btnSave.setOnClickListener {
                lifecycleScope.launch { submitTransaction() }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_CART && resultCode == Activity.RESULT_OK) {
            val cartTransactions = data?.getParcelableArrayListExtra<CartTransaction>("transaksi_sampah")
            cartTransactions?.forEach {
                Log.d("ReceivedTransaction", "Transaction: $it")
                adapter.addCartTransaction(it)
                cartItems.add(it)
            }

            calculateTotal(cartItems)
        }
    }

    private fun getUserId() {
        viewModel.nasabah.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val nameList = result.data
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nameList)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            selectedId = viewModel.getIdByPosition(position)
                            Log.d("SelectedId", "ID: $selectedId")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) { }
                    }
                }

                is Result.Error -> {
                    Log.e("Spinner", "Error loading user details: ${result.error}")
                }

                Result.Loading -> { }
            }
        }
        viewModel.getUserId()
    }

    private fun setupDatePicker() {
        binding.btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format the date
                    selectedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.tvSelectedDate.text = selectedDate
                    Log.d("SelectedDate", "Date: $selectedDate")
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun calculateTotal(cartTransactions: List<CartTransaction>) {
        totalWeight = cartTransactions.sumOf { it.berat }
        totalPrice = cartTransactions.sumOf { it.harga?.toDouble() ?: 0.0 }.toFloat()

        Log.d("TotalCalculation", "Total weight: $totalWeight kg, Total price: Rp $totalPrice")

        binding.tvWeightEstimation.text = "$totalWeight kg"
        binding.tvPriceEstimation.text = "Rp $totalPrice"
    }

    private fun submitTransaction() {
        val idNasabah = selectedId
        val tanggal = selectedDate

        if (idNasabah != null && tanggal != null && cartItems.isNotEmpty()) {
            // Trigger the transaction
            viewModel.addCartTransaction(idNasabah, tanggal, cartItems)
            viewModel.transactionResult.observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        showToast(getString(R.string.tv_on_process))
                    }
                    is Result.Success -> {
                        lifecycleScope.launch {
                            viewModel.syncDataTransaction()
                            binding.progressBar.visibility = View.GONE
                            showToast(getString(R.string.tv_transaction_success))
                            finish()
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast("Error: ${result.error}")
                    }
                }
            }
        } else {
            showToast("Please select a user, date, and add items to the cart")
        }
    }



    companion object {
        private const val REQUEST_CODE_ADD_CART = 1002
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
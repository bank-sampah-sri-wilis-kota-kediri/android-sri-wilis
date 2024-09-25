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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CartTransactionAdapter
import com.bs.sriwilis.databinding.ActivityAddTransactionBinding
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.model.CartTransaction
import java.util.Calendar

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
        val cartItems = mutableListOf<CartTransaction>()

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

            // Update total weight and price after adding items
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
                            Log.d("SelectedPhone", "Phone: $selectedId")
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
        totalPrice = cartTransactions.sumOf { it.harga.toDouble() }.toFloat()

        Log.d("TotalCalculation", "Total weight: $totalWeight kg, Total price: Rp $totalPrice")

        binding.tvWeightEstimation.text = "$totalWeight kg"
        binding.tvPriceEstimation.text = "Rp $totalPrice"
    }

   /* private suspend fun submitCatalog() {
        val nasabah = selectedPhone
        val tanggal = selectedDate
        val link = binding.edtLinkShopee.text.toString()
        val number = binding.edtMobileNumber.text.toString()
        val desc = binding.edtDescriptionCatalog.text.toString()
        val token = viewModel.getToken()

        if (nasabah != null && tanggal != null &&) {
            if (nasabah.isEmpty() || tanggal.isEmpty() ) {
                showToast(getString(R.string.tv_make_sure))
                return
            }
        }

        if (token != null) {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.addCatalog(token, name, desc, price, number, link, imageBase64)
        }

        viewModel.addCatalogResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    showToast(getString(R.string.tv_on_process))
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(getString(R.string.tv_category_process_success))
                    finish()
                }
                is Result.Error -> {
                    showToast(" ${result.error}")
                }
            }
        })
    }
*/

    companion object {
        private const val REQUEST_CODE_ADD_CART = 1002
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
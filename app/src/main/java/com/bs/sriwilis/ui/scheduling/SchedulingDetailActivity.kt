package com.bs.sriwilis.ui.scheduling

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.adapter.CartOrderAdapter
import com.bs.sriwilis.databinding.ActivitySchedulingDetailBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySchedulingDetailBinding
    private lateinit var dateTextView: TextView
    private lateinit var selectDateButton: Button
    private lateinit var adapter: CartOrderAdapter
    private var selectedDate: String? = null


    private val viewModel by viewModels<SchedulingDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.tvDateResult.text = dateFormat.format(currentDate)

        var selectDateButton = binding.btnDatePicker

        selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }

        val orderId = intent.getStringExtra("id")
        val nasabahId = intent.getStringExtra("nasabahId")

        nasabahId.let {
            if (it != null) {
                viewModel.fetchSchedule(it)
                viewModel.fetchTransactionItemById(it)
                viewModel.fetchDataKeranjangById(it)
            }
        }

        setupDataKeranjang()
        setupTransactionData()
        setupDatePicker()
        binding.btnSuksesPesanan.setOnClickListener { selectedDate?.let {
            if (orderId != null) {
                updateStatusConfirm(orderId, it)
            }
        } }
        binding.btnCancelPesanan.setOnClickListener {
            if (orderId != null) {
                updateStatusFailed(orderId)
            }
        }
        observeViewModel()

        Log.d("orderId cik", "$orderId")

        adapter = CartOrderAdapter(emptyList(), this)

        binding.rvPesanan.layoutManager = LinearLayoutManager(this)
        binding.rvPesanan.adapter = adapter

        viewModel.cartOrderData.observe(this) { orders ->
            val orderCart = orders?.flatMap { it.transaksiSampah!! } ?: emptyList()

            if (orders != null) {
                adapter.updateOrder(orderCart)
            } else {
                adapter.updateOrder(emptyList())
            }
        }
    }

 /*   private fun observeUser() {
        viewModel.unscheduledOrdersLiveData.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val userDetails = result.data

                    binding.tvNamaDetailPesanan.text = userDetails.noHpNasabah.toEditable()
                    binding.edtFullNameForm.text = userDetails.namaNasabah.toEditable()
                    binding.edtEditUserAddress.text = userDetails.alamatNasabah.toEditable()
                    binding.edtEditUserAccountBalance.text = userDetails.saldoNasabah.toString().toEditable()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Failed to fetch user details: ${result.error}")
                }
            }
        })
    }

    private fun setupAction() {
        binding.btnChangeUsers.setOnClickListener {
            val userId = intent.getStringExtra("userId") ?: throw IllegalArgumentException("ID Pengguna tidak ada")
            val phone = binding.edtEditUserPhone.text.toString()
            val name = binding.edtFullNameForm.text.toString()
            val address = binding.edtEditUserAddress.text.toString()
            val balanceString = binding.edtEditUserAccountBalance.text.toString()
            val balance = balanceString.toDoubleOrNull() ?: throw IllegalArgumentException("Saldo tidak valid")

            editUser(userId, phone, name, address, balance)
        }
    }

    private fun editUser(userId: String, phone: String, name: String, address: String, balance: Double) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.editUser(userId, name, phone, address, balance)
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Berhasil!")
                        setMessage("Akun Pengguna Berhasil Diubah")
                        setPositiveButton("Ok") { _, _ ->
                            refreshUserList()
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Gagal!")
                        setMessage("Akun Pengguna Gagal Diubah")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }

    private fun refreshUserList() {
        viewModel.getUsers()

        userAdapter.notifyDataSetChanged()
    }*/

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val date = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(date.time)
                dateTextView.text = formattedDate
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setupDataKeranjang() {
        viewModel.dataKeranjangItem.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val dataKeranjang = result.data

                    dataKeranjang?.forEach {
                        it.idNasabah.let { nasabahId ->
                            viewModel.getCustomerName(nasabahId) { customerName ->
                                binding.tvNamaDetailPesanan.text = customerName
                                viewModel.getCustomerPhone(nasabahId) { customerPhone ->
                                    binding.tvNomorwaDetailPesanan.text = customerPhone
                                    viewModel.getCustomerAddress(nasabahId) { customerAddress ->
                                        binding.tvAlamatDetailPesanan.text = customerAddress
                                    }
                            }
                        }
                        }
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                else -> {}
            }
        })
    }

    private fun setupTransactionData() {
        viewModel.transactionDataItem.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val transactionDetail = result.data

                    transactionDetail?.forEach {
                        val totalBerat = transactionDetail?.sumOf { it?.beratPerkiraan ?: 0 } ?: 0
                        binding.tvBeratDetailPesanan.text = "$totalBerat kg"
                     }
                    }

                is Result.Error -> TODO()
                else -> {}
            }
        })
    }

    private fun setupDatePicker() {
        binding.btnDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format the date
                    selectedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    binding.tvDateResult.text = selectedDate
                    Log.d("SelectedDate", "Date: $selectedDate")
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun updateStatusConfirm(orderId: String, date: String) {
        orderId.let {
            viewModel.registerDate(orderId, date)
        }
    }

    private fun updateStatusFailed(orderId: String) {
        orderId.let {
            viewModel.updateFailed(orderId)
        }
    }

    private fun observeViewModel() {
        viewModel.crudResponse.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = result.data
                    showToast("Update tanggal dan status pesanan sampah berhasil!")

                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Update tanggal dan status pesanan sampah gagal!")
                }
            }
        })

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

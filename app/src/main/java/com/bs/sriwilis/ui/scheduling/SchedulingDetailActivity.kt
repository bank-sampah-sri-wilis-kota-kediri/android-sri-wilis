package com.bs.sriwilis.ui.scheduling

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.adapter.CartAdapter
import com.bs.sriwilis.adapter.CategoryAdapter
import com.bs.sriwilis.databinding.ActivitySchedulingDetailBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySchedulingDetailBinding
    private lateinit var dateTextView: TextView
    private lateinit var selectDateButton: Button
    private lateinit var cartAdapter: CartAdapter

    private val viewModel by viewModels<SchedulingDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var dateTextView = binding.tvDateResult
        var selectDateButton = binding.btnDatePicker

        selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }

        setupRecyclerView()
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

    private fun setupRecyclerView() {
        binding.rvPesanan.layoutManager = LinearLayoutManager(this)
        binding.rvPesanan.adapter = CartAdapter(emptyList(), this)

        lifecycleScope.launch {
            viewModel.getPesananSampah()
        }
    }

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

    private fun observeViewModel() {
        viewModel.pesananSampahData.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show progress bar if needed
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val pesananList = result.data
                    if (pesananList.isNotEmpty()) {
                        // Update the RecyclerView's adapter with new data
/*
                        cartAdapter.updateCatalog(pesananList)
*/
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    // Handle the error, show a toast or log the error
                    Log.e("SchedulingDetail", "Error fetching orders: ${result.error}")
                }
            }
        }
    }

}
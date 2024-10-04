package com.bs.sriwilis.ui.homepage.operation

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CategoryAdapter
import com.bs.sriwilis.adapter.UserAdapter
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityEditUserBinding
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserBinding
    private lateinit var userAdapter: UserAdapter

    private val viewModel by viewModels<ManageUserViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId") ?: throw IllegalArgumentException("ID Pengguna tidak ada")
        val phone = intent.getStringExtra("phone") ?: throw IllegalArgumentException("Nomor Pengguna tidak ada")

        phone.let {
            viewModel.fetchUserDetails(it)
        }

        observeUser()
        observeViewModel()
        setupAction()

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }
    }

    private fun observeUser() {
        viewModel.usersData.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val userDetails = result.data

                    if (userDetails != null) {
                        binding.edtEditUserPhone.text = userDetails.no_hp_nasabah.toEditable()
                        binding.edtFullNameForm.text = userDetails.nama_nasabah.toEditable()
                        binding.edtEditUserAddress.text = userDetails.alamat_nasabah.toEditable()
                        binding.edtEditUserAccountBalance.text = userDetails.saldo_nasabah.toString().toEditable()
                    }
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
        viewModel.editUser(userId, phone, name, address, balance)
    }

    fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.users.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Berhasil!")
                        setMessage("Akun Pengguna Berhasil Diubah")
                        setPositiveButton("OK") { _, _ ->
                            lifecycleScope.launch {
                                viewModel.syncData()
                                viewModel.getUsers()
                            }
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

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch { viewModel.syncData() }
    }
}
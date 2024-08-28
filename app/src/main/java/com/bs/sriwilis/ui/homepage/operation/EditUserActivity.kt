package com.bs.sriwilis.ui.homepage.operation

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityEditUserBinding
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilis.helper.Result

class EditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditUserBinding

    private val viewModel by viewModels<ManageUserViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("userId") ?: throw IllegalArgumentException("ID Pengguna tidak ada")

        userId.let {
            viewModel.fetchUserDetails(it)
        }

        observeUser()
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
                    binding.edtEditUserPhone.text = userDetails.noHpNasabah.toEditable()
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

    private fun editUser(userId: String, name: String, phone: String, address: String, balance: Double) {
        viewModel.editUser(userId, name, phone, address, balance)
    }

    fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
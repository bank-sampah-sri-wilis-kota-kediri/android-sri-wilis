package com.bs.sriwilis.ui.settings

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityChangePasswordBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.homepage.operation.ManageUserViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

class ChangePasswordActivity : AppCompatActivity() {

    private val adminId = "1"
    private lateinit var binding: ActivityChangePasswordBinding

    private val viewModel by viewModels<AdminViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }

        observeViewModel()
        setupAction()

    }

    private fun setupAction() {
        binding.btnChangePassword.setOnClickListener {
            val newPassword = binding.edtNewPasswordForm.text.toString()
            val newPasswordConfirmation = binding.edtNewPasswordConfirmationForm.text.toString()

            if (newPassword == newPasswordConfirmation) {
                changePassword(adminId, newPassword)
            } else {
                showToast(R.string.tv_password_do_not_match.toString())
            }
        }
    }

    private fun changePassword(adminId: String, newPassword: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.changePassword(adminId, newPassword)
    }

    fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


    private fun observeViewModel() {
        viewModel.changeResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Berhasil!")
                        setMessage("Sandi Berhasil Diubah!")
                        setPositiveButton("Ok") { _, _ ->
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
                        setMessage("Sandi Gagal Diubah")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
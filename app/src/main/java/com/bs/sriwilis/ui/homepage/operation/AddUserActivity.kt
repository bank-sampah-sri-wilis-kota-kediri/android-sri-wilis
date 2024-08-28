package com.bs.sriwilis.ui.homepage.operation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.authorization.LoginActivity

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding

    private val viewModel by viewModels<ManageUserViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var isPasswordVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupPasswordToggle()
        observeViewModel()
        setupAction()

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupAction() {
        binding.btnAddUsers.setOnClickListener {
            val phone = binding.edtMobileNumberForm.text.toString()
            val password = binding.edtPasswordForm.text.toString()
            val name = binding.edtFullNameForm.text.toString()
            val address = binding.edtUserAddressForm.text.toString()
            val balance = binding.edtUserBalanceForm.text.toString()

            registerUser(phone, password, name, address, balance)
        }
    }

    private fun registerUser(name: String, phone: String, address: String, password: String, balance: String) {
        viewModel.register(name, phone, address, password, balance)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordToggle() {
        binding.edtPasswordForm.setOnTouchListener(View.OnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.edtPasswordForm.right - binding.edtPasswordForm.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val selection = binding.edtPasswordForm.selectionEnd
                    if (isPasswordVisible) {
                        binding.edtPasswordForm.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.edtPasswordForm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_close, 0)
                    } else {
                        binding.edtPasswordForm.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.edtPasswordForm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0)
                    }
                    binding.edtPasswordForm.setSelection(selection)
                    isPasswordVisible = !isPasswordVisible
                    return@OnTouchListener true
                }
            }
            false
        })
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
                        setMessage("Akun Pengguna berhasil dibuat")
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
                        setMessage("Akun Pengguna Gagal Dibuat")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }
}
package com.bs.sriwilis.ui.authorization

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityLoginBinding
import com.bs.sriwilis.ui.homepage.HomepageActivity
import com.bs.sriwilis.ui.settings.ChangePasswordActivity
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.splashscreen.WelcomeViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val viewModelWelcome by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPasswordToggle()
        observeViewModel()
        setupAction()
    }

    private fun login(phone: String, password: String) {
        viewModel.login(phone, password)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupPasswordToggle() {
        binding.edtEmailPasswordForm.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.edtEmailPasswordForm.right - binding.edtEmailPasswordForm.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val selection = binding.edtEmailPasswordForm.selectionEnd
                    if (isPasswordVisible) {
                        binding.edtEmailPasswordForm.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.edtEmailPasswordForm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_close, 0)
                    } else {
                        binding.edtEmailPasswordForm.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        binding.edtEmailPasswordForm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0)
                    }
                    binding.edtEmailPasswordForm.setSelection(selection)
                    isPasswordVisible = !isPasswordVisible
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private val phoneTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            //validatePhone()
            validateFields()
        }
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            validateFields()
        }
    }

/*    private fun validatePhone() {
        val email = binding.emailEtLogin.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            binding.emailAlert.visibility = View.VISIBLE
            binding.emailAlert.text = getString(R.string.email_can_t_empty)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailAlert.visibility = View.VISIBLE
            binding.emailAlert.text = getString(R.string.invalid_email)
        } else {
            binding.emailAlert.visibility = View.GONE
        }
    }

    private fun validatePassword() {
        val password = binding.edtEmailPasswordForm.text.toString().trim()
        if (TextUtils.isEmpty(password)) {
            binding.passwordAlert.text = getString(R.string.password_can_t_empty)
            binding.passwordAlert.visibility = View.VISIBLE
        } else if (password.length < MIN_PASSWORD_LENGTH) {
            binding.passwordAlert.text = getString(R.string.password_must_be_at_least_8_characters_long)
            binding.passwordAlert.visibility = View.VISIBLE
        } else {
            binding.passwordAlert.visibility = View.GONE
        }
    }*/

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val phone = binding.edtEmailLoginForm.text.toString()
            val password = binding.edtEmailPasswordForm.text.toString()

            login(phone, password)
        }
    }

    private fun validateFields(){
        val password = binding.edtEmailPasswordForm.text.toString().trim()

        val isPasswordValid = !TextUtils.isEmpty(password) && password.length >= MIN_PASSWORD_LENGTH

        binding.btnLogin.isEnabled = isPasswordValid
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    lifecycleScope.launch {
                        viewModelWelcome.syncData()
                    }
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Sukses")
                        setMessage("Anda Berhasil Masuk")
                        setPositiveButton("OK") { _, _ ->
                            val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                            startActivity(intent)
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
                        setMessage("Email atau sandi yang dimasukkan salah")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}
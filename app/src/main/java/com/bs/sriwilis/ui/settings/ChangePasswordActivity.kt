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

    private var isOldPasswordValid = false
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
            val oldPassword = binding.edtOldPasswordForm.text.toString()
            val newPassword = binding.edtNewPasswordForm.text.toString()

            changePassword(oldPassword, newPassword)
        }
    }

/*    private fun oldPassValidation() {
        viewModel.adminData.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> { }
                is Result.Success -> {
                    val categoryDetails = result.data

                    categoryDetails.gambarKategori?.let { gambarKategori ->
                        if (gambarKategori.isNotEmpty()) {

                            val imageBytes = Base64.decode(gambarKategori, Base64.DEFAULT)

                            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            val tempFile = File(cacheDir, "api_image.jpg")
                            val outStream = FileOutputStream(tempFile)
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                            outStream.flush()
                            outStream.close()

                            currentImageUri = tempFile.toUri()

                            Glide.with(this@EditCategoryActivity)
                                .load(imageBytes)
                                .into(binding.ivCategoryListPreview)
                        } else {
                            binding.ivCategoryListPreview.setImageResource(R.drawable.iv_panduan2)
                        }
                    } ?: run {
                        binding.ivCategoryListPreview.setImageResource(R.drawable.iv_panduan2)
                    }

                    binding.edtCategoryNameForm.text = categoryDetails.namaKategori.toEditable()
                    binding.edtCategoryPriceForm.text = categoryDetails.hargaKategori.toString().toEditable()

                    val spinnerAdapter = binding.spinnerWasteCategory.adapter
                    val position = (0 until spinnerAdapter.count)
                        .firstOrNull { spinnerAdapter.getItem(it).toString() == categoryDetails.jenisKategori.toString() }

                    if (position != null) {
                        binding.spinnerWasteCategory.setSelection(position)
                    } else {
                        binding.spinnerWasteCategory.setSelection(0)
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Failed to fetch user details: ${result.error}")
                }
            }
        })
    }*/

    private fun changePassword(oldPassword: String, newPassword: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.changePassword(oldPassword, newPassword)
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
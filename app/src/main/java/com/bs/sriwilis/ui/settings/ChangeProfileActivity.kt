package com.bs.sriwilis.ui.settings

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityChangePasswordBinding
import com.bs.sriwilis.databinding.ActivityChangeProfileBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.homepage.operation.ManageUserViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ChangeProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeProfileBinding
    private var currentImageUri: Uri? = null
    private val adminId: String = "1"

    private val viewModel by viewModels<AdminViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val uCropFunction = object : ActivityResultContract<List<Uri>, Uri>() {
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val uriInput = input[0]
            val uriOutput = input[1]

            val uCrop = UCrop.of(uriInput, uriOutput)
                .withAspectRatio(5f, 5f)
                .withMaxResultSize(800, 800)

            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return intent?.let { UCrop.getOutput(it) } ?: Uri.EMPTY
        }

    }

    private val cropImage = registerForActivityResult(uCropFunction) { uri ->
        if (uri != Uri.EMPTY) {
            currentImageUri = uri
            binding.icProfileCircle.setImageURI(uri)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchAdminDetails()

        observeAdmin()
        observeViewModel()
        setupAction()

        binding.apply {
            btnBack.setOnClickListener { finish() }
            icProfileCircle.setOnClickListener { startGallery() }
            icEditPencil.setOnClickListener { startGallery() }
        }
    }

    private fun observeAdmin() {
        viewModel.adminData.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val adminDetails = result.data

                    adminDetails?.gambarAdmin?.let { gambarAdmin ->
                        if (gambarAdmin.isNotEmpty()) {

                            val imageBytes = Base64.decode(gambarAdmin, Base64.DEFAULT)

                            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            val tempFile = File(cacheDir, "api_image.jpg")
                            val outStream = FileOutputStream(tempFile)
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                            outStream.flush()
                            outStream.close()

                            currentImageUri = tempFile.toUri()

                            Glide.with(this@ChangeProfileActivity)
                                .load(imageBytes)
                                .into(binding.icProfileCircle)
                        } else {
                            binding.icProfileCircle.setImageResource(R.drawable.ic_profile)
                        }
                    } ?: run {
                        binding.icProfileCircle.setImageResource(R.drawable.ic_profile)
                    }

                    binding.edtNameForm.text = adminDetails?.namaAdmin.toEditable()
                    binding.edtMobileNumberForm.text = adminDetails?.noHpAdmin.toEditable()
                    binding.edtUserAddressForm.text = adminDetails?.alamatAdmin.toEditable()
                    
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Failed to fetch user details: ${result.error}")
                }
            }
        })
    }

    private fun setupAction() {
        binding.btnChangeProfile.setOnClickListener {
            val name = binding.edtNameForm.text.toString()
            val phone = binding.edtMobileNumberForm.text.toString()
            val address = binding.edtUserAddressForm.text.toString()

            val imageBase64 = if (currentImageUri != null) {
                uriToBase64(currentImageUri!!)
            } else {
                (viewModel.adminData.value as? Result.Success)?.data?.gambarAdmin ?: ""
            }

            Log.d("ChangeProfileActivity", "Base64 Image String: $imageBase64")

            val validImageBase64 = imageBase64 ?: ""

            editAdmin(adminId, name, phone, address, validImageBase64)
        }
    }

    private fun editAdmin(adminId: String, name: String, phone: String, address: String, image: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.editAdmin(adminId, name, phone, address, image)
    }

    fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

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
                        setMessage("Akun Admin Berhasil Diubah")
                        setPositiveButton("OK") { _, _ ->
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
                        setMessage("Akun Admin Gagal Diubah")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            val uriOutput = File(filesDir, "croppedImage.jpg").toUri()

            val listUri = listOf(uri, uriOutput)
            cropImage.launch(listUri)
        }
    }

    private fun uriToBase64(uri: Uri): String {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}
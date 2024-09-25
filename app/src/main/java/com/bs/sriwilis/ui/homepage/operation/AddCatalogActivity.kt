package com.bs.sriwilis.ui.homepage.operation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
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
import androidx.lifecycle.lifecycleScope
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddCatalogBinding
import com.bs.sriwilis.databinding.ActivityLoginBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class AddCatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCatalogBinding
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<ManageCatalogViewModel> {
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
            binding.ivCatalogPreview.setImageURI(uri)
        }
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
        binding = ActivityAddCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnUploadPhoto.setOnClickListener { startGallery() }
            btnSave.setOnClickListener {
                lifecycleScope.launch {
                    submitCatalog()
                }
            }
            btnBack.setOnClickListener { finish() }
        }
    }

    private suspend fun submitCatalog() {
        val imageBase64 = currentImageUri?.let { uriToBase64(it) } ?: ""
        val name = binding.edtCatalogName.text.toString()
        val price = binding.edtCatalogPrice.text.toString()
        val link = binding.edtLinkShopee.text.toString()
        val number = binding.edtMobileNumber.text.toString()
        val desc = binding.edtDescriptionCatalog.text.toString()
        val token = viewModel.getToken()

        if (name.isEmpty() || price.isEmpty() || link.isEmpty() || number.isEmpty() || desc.isEmpty() || imageBase64.isEmpty()) {
            showToast(getString(R.string.tv_make_sure))
            return
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

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        viewModel.addCatalogResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Berhasil!")
                        setMessage("Katalog Berhasil Ditambahkan")
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
                        setMessage("Katalog Gagal Ditambahkan")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }
}
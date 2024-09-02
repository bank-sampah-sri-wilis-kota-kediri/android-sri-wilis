package com.bs.sriwilis.ui.homepage.operation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityEditCatalogBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class EditCatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCatalogBinding
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
        binding = ActivityEditCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryId = intent.getStringExtra("id") ?: throw IllegalArgumentException("ID Kategori tidak ada")

        categoryId.let {
            viewModel.fetchCatalogDetails(it)
        }

        setupAction()
        observeUser()

        binding.apply {
            btnUploadPhoto.setOnClickListener { startGallery() }
            btnBack.setOnClickListener { finish() }
        }
    }

    private fun observeUser() {
        viewModel.catalogData.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val catalogDetails = result.data

                    catalogDetails.gambarKatalog?.let { gambarKatalog ->
                        if (gambarKatalog.isNotEmpty()) {
                            val imageBytes = Base64.decode(gambarKatalog, Base64.DEFAULT)
                            Glide.with(this@EditCatalogActivity)
                                .load(imageBytes)
                                .into(binding.ivCatalogPreview)
                        } else {
                            binding.ivCatalogPreview.setImageResource(R.drawable.iv_panduan2)
                        }
                    } ?: run {
                        binding.ivCatalogPreview.setImageResource(R.drawable.iv_panduan2)
                    }

                    binding.edtCatalogName.text = catalogDetails.judulKatalog.toEditable()
                    binding.edtCatalogPrice.text = catalogDetails.hargaKatalog.toString().toEditable()
                    binding.edtLinkShopee.text = catalogDetails.shopeeLink.toEditable()
                    binding.edtMobileNumber.text = catalogDetails.noWa.toEditable()
                    binding.edtDescriptionCatalog.text = catalogDetails.deskripsiKatalog.toEditable()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showToast("Failed to fetch user details: ${result.error}")
                }
            }
        })
    }


    private fun setupAction() {
        binding.btnSave.setOnClickListener {

            val catalogId = intent.getStringExtra("id") ?: throw IllegalArgumentException("ID Katalog tidak ada")
            val name = binding.edtCatalogName.text.toString()
            val price = binding.edtCatalogPrice.text.toString()
            val link = binding.edtLinkShopee.text.toString()
            val number = binding.edtMobileNumber.text.toString()
            val desc = binding.edtDescriptionCatalog.text.toString()

            val imageBase64 = currentImageUri?.let { uriToBase64(it) } ?: ""

            editCatalog(catalogId, name, desc, price, number, link, imageBase64)

            viewModel.addCatalogResult.observe(this, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        showToast(getString(R.string.tv_on_process))
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(getString(R.string.tv_category_change_process_success))
                        finish()
                    }
                    is Result.Error -> {
                        showToast(" ${result.error}")
                    }
                }
            })
        }
    }

    private fun editCatalog(catalogId: String, name: String, desc: String, price: String, number: String, link: String, image: String) {
        viewModel.editCatalog(catalogId, name, desc, price, number, link, image)
    }

    fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

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
}
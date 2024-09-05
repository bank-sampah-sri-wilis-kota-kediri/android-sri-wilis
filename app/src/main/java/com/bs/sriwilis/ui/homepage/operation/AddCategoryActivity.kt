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
import android.widget.AdapterView
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
import com.bs.sriwilis.R
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.data.preference.dataStore
import com.bs.sriwilis.databinding.ActivityAddCatalogBinding
import com.bs.sriwilis.databinding.ActivityAddCategoryBinding
import com.bs.sriwilis.utils.ViewModelFactory
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import com.bs.sriwilis.helper.Result

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<ManageCategoryViewModel> {
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
            binding.ivCategoryListPreview.setImageURI(uri)
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
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()
        observeViewModel()
        setupAction()

        binding.apply {
            btnUploadPhoto.setOnClickListener { startGallery() }
            btnBack.setOnClickListener { finish() }
        }
    }

    private fun setupAction() {
        binding.btnAddCategory.setOnClickListener {
            val imageBase64 = currentImageUri?.let { uriToBase64(it) } ?: ""
            val name = binding.edtCategoryNameForm.text.toString()
            val price = binding.edtCategoryPrice.text.toString()
            val type = binding.spinnerTag.selectedItem.toString()
            val userPreferences = UserPreferences.getInstance(this.dataStore)
            val token = runBlocking { userPreferences.token.first() }

            if (token != null) {
                addCategory(token, name, price, type, imageBase64)
            }
        }
    }

    private fun addCategory(token: String, name: String, price: String, type: String, imageBase64: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.addCategory(token, name, price, type, imageBase64)
    }

    private fun setupSpinner() {
        val spinnerTag = binding.spinnerTag

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.pilihan_tag,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTag.adapter = adapter
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
        viewModel.addCategoryResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Berhasil!")
                        setMessage("Kategori berhasil ditambahkan")
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
                        setMessage("Kategori gagal ditambahkan")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }
}

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityEditCategoryBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class EditCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCategoryBinding
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
        binding = ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryId = intent.getStringExtra("id") ?: throw IllegalArgumentException("ID Kategori tidak ada")

        categoryId.let {
            viewModel.fetchCategoryDetails(it)
        }

        observeCategory()
        observeViewModel()
        setupAction()
        setupSpinner()

        binding.apply {
            btnUploadPhoto.setOnClickListener { startGallery() }
            btnBack.setOnClickListener { finish() }
        }
    }

    private fun observeCategory() {
        viewModel.categoryData.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val categoryDetails = result.data

                    categoryDetails.gambarKategori?.let { gambarKategori ->
                        if (gambarKategori.isNotEmpty()) {
                            val imageBytes = Base64.decode(gambarKategori, Base64.DEFAULT)
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
    }


    private fun setupAction() {
        binding.btnChangeCategory.setOnClickListener {

        val userId = intent.getStringExtra("id") ?: throw IllegalArgumentException("ID Kategori tidak ada")
        val name = binding.edtCategoryNameForm.text.toString()
        val price = binding.edtCategoryPriceForm.text.toString()
        val type = binding.spinnerWasteCategory.selectedItem.toString()
        val imageBase64 = currentImageUri?.let { uriToBase64(it) } ?: ""

            if (name.isEmpty() || price.isEmpty() || type.isEmpty()){
                showToast(getString(R.string.tv_make_sure))
                return@setOnClickListener
            }

            if (currentImageUri == null) {
                showToast(getString(R.string.tv_make_sure_image))
                return@setOnClickListener
            }

        editCategory(userId, name, price, type, imageBase64)

        viewModel.editCategoryResult.observe(this, Observer { result ->
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

    private fun setupSpinner() {
        val spinnerTag = binding.spinnerWasteCategory

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.pilihan_tag,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTag.adapter = adapter
    }

    private fun editCategory(categoryId: String, name: String, price: String, type: String, image: String) {
        viewModel.editCategory(categoryId, name, price, type, image)
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

    private fun observeViewModel() {
        viewModel.editCategoryResult.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Berhasil!")
                        setMessage("Kategori Berhasil Diubah")
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
                        setMessage("Kategori Gagal Diubah")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }
}
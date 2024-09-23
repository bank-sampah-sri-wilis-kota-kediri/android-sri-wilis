package com.bs.sriwilis.ui.homepage.operation

import android.annotation.SuppressLint
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
import com.bs.sriwilis.adapter.CategoryAdapter
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityEditCategoryBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCategoryBinding
    private var currentImageUri: Uri? = null
    private lateinit var categoryAdapter: CategoryAdapter

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

        categoryAdapter = CategoryAdapter(emptyList(), this)

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

                    categoryDetails.gambar_kategori?.let { gambarKategori ->
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

                    binding.edtCategoryNameForm.text = categoryDetails.nama_kategori.toEditable()
                    binding.edtCategoryPriceForm.text = categoryDetails.harga_kategori.toString().toEditable()

                    val spinnerAdapter = binding.spinnerWasteCategory.adapter
                    val position = (0 until spinnerAdapter.count)
                        .firstOrNull { spinnerAdapter.getItem(it).toString() == categoryDetails.jenis_kategori.toString() }

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


    @SuppressLint("SuspiciousIndentation")
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

        editCategory(userId, name, price, type, imageBase64)
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
                        setMessage("Kategori Gagal Diubah")
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
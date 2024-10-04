package com.bs.sriwilis.ui.homepage.operation

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
import androidx.lifecycle.lifecycleScope
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CatalogAdapter
import com.bs.sriwilis.adapter.CategoryAdapter
import com.bs.sriwilis.databinding.ActivityAddUserBinding
import com.bs.sriwilis.databinding.ActivityEditCatalogBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditCatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCatalogBinding
    private var currentImageUri: Uri? = null
    private lateinit var catalogAdapter: CatalogAdapter

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

        val catalogId = intent.getStringExtra("id") ?: throw IllegalArgumentException("ID Katalog tidak ada")

        catalogId.let {
            viewModel.fetchCatalogDetails(it)
        }

        catalogAdapter = CatalogAdapter(emptyList(), this)

        observeCatalog()
        observeViewModel()
        setupAction()

        binding.apply {
            btnUploadPhoto.setOnClickListener { startGallery() }
            btnBack.setOnClickListener { finish() }
        }
    }

    private fun observeCatalog() {
        viewModel.catalogDetail.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val catalogDetails = result.data

                    catalogDetails?.gambar_katalog?.let { gambarKatalog ->
                        if (gambarKatalog.isNotEmpty()) {
                            val imageBytes = Base64.decode(gambarKatalog, Base64.DEFAULT)

                            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            val tempFile = File(cacheDir, "api_image.jpg")
                            val outStream = FileOutputStream(tempFile)
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                            outStream.flush()
                            outStream.close()

                            currentImageUri = tempFile.toUri()

                             Glide.with(this@EditCatalogActivity)
                                .load(currentImageUri)
                                .into(binding.ivCatalogPreview)
                        } else {
                            binding.ivCatalogPreview.setImageResource(R.drawable.iv_panduan2)
                        }
                    } ?: run {
                        binding.ivCatalogPreview.setImageResource(R.drawable.iv_panduan2)
                    }

                    binding.edtCatalogName.text = catalogDetails?.judul_katalog.toEditable()
                    binding.edtCatalogPrice.text = catalogDetails?.harga_katalog.toString().toEditable()
                    binding.edtLinkShopee.text = catalogDetails?.shopee_link.toEditable()
                    binding.edtMobileNumber.text = catalogDetails?.no_wa.toEditable()
                    binding.edtDescriptionCatalog.text = catalogDetails?.deskripsi_katalog.toEditable()
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

            if (name.isEmpty() || price.isEmpty() || link.isEmpty() || number.isEmpty() || desc.isEmpty()){
                showToast(getString(R.string.tv_make_sure))
                return@setOnClickListener
            }
            editCatalog(catalogId, name, desc, price, number, link, imageBase64)
        }
    }

    private fun editCatalog(catalogId: String, name: String, desc: String, price: String, number: String, link: String, image: String) {
        Log.d("EditCatalog", "Image Base64: $image")
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
            Base64.encodeToString(byteArray, Base64.DEFAULT).trim()
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
                        setMessage("Katalog Berhasil Diubah")
                        setPositiveButton("Ok") { _, _ ->
                            lifecycleScope.launch { refreshCategoryList() }
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
                        setMessage("Katalog Gagal Diubah")
                        setPositiveButton("OK", null)
                        create()
                        show()
                    }
                }
            }
        })
    }

    private suspend fun refreshCategoryList() {
        viewModel.syncData()
        viewModel.getCatalog()

        catalogAdapter.notifyDataSetChanged()
    }
}
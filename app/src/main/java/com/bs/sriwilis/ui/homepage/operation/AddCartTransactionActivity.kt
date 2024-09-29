package com.bs.sriwilis.ui.homepage.operation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.bs.sriwilis.R
import com.bs.sriwilis.databinding.ActivityAddCartTransaction2Binding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.model.CartTransaction
import com.bs.sriwilis.utils.ViewModelFactory
import java.io.ByteArrayOutputStream

class AddCartTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCartTransaction2Binding
    private val viewModel by viewModels<ManageTransactionViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var spinner: Spinner

    private var cartTransactions = mutableListOf<CartTransaction>()
    private var selectedCategory: String? = null
    private var cartImage: Bitmap? = null
    private var basePrice: Float = 0.0f

    private var totalWeight: Int = 0
    private var totalPrice: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCartTransaction2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinnerWasteCategory

        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnUploadPhoto.setOnClickListener { openCamera() }
            btnSave.setOnClickListener { saveCartTransaction() }

            edtWasteWeight.addTextChangedListener {
                updatePrice()
            }

        }
        getCategorySpinner()
    }

    private fun updatePrice() {
        val weight = binding.edtWasteWeight.text.toString().toFloatOrNull() ?: 0.0f
        val calculatedPrice = basePrice * weight
        binding.edtWastePrice.setText(calculatedPrice.toString())
    }


    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
            if (photo != null) {
                cartImage = photo
                binding.ivCatalogPreview.setImageBitmap(photo)
            }
        }
    }

    private fun saveCartTransaction() {
        val selectedCategory = binding.spinnerWasteCategory.selectedItem.toString()
        val weight = binding.edtWasteWeight.text.toString().toIntOrNull() ?: 0
        val price = binding.edtWastePrice.text.toString().toFloatOrNull() ?: 0.0f
        val encodedImage = cartImage?.let { encodeImageToBase64(it) }

        if (selectedCategory.isNullOrEmpty() || weight == null || price == null) {
            Toast.makeText(this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show()
        }

        val cartTransactionId = cartTransactions.size + 1

        val cartTransaction = CartTransaction(
            kategori = selectedCategory,
            berat = weight,
            harga = price,
            gambar = encodedImage
        )

        cartTransactions.add(cartTransaction)


        // Calculate total weight and price
        val totalWeight = cartTransactions.sumOf { it.berat }
        val totalPrice = cartTransactions.sumOf { it.harga.toDouble() }

        Log.d("CartTransaction", "Added: $cartTransaction")

        val intent = Intent().apply {
            putParcelableArrayListExtra("transaksi_sampah", ArrayList(cartTransactions))
            putExtra("total_weight", totalWeight) // Pass total weight
            putExtra("total_price", totalPrice.toFloat())   // Pass total price
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
    }

    private fun getCategorySpinner() {
        viewModel.categoryNames.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val categoryNames = result.data
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            selectedCategory = categoryNames[position] // This should now be just the name
                            val categoryItem = viewModel.getCategoryByPosition(position) // Retrieve full object
                            basePrice = categoryItem?.basePrice ?: 0.0f // Now retrieve base price if needed
                            Log.d("SelectedCategory", "Category: $selectedCategory, Base Price: $basePrice")

                            updatePrice()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) { }
                    }
                }

                is Result.Error -> {
                    Log.e("Spinner", "Error loading categories: ${result.error}")
                }

                Result.Loading -> {
                    // Show loading indicator
                }
            }
        }
        viewModel.getCategoryDetails()
    }


    companion object {
        private const val REQUEST_CAMERA = 1001
    }
}
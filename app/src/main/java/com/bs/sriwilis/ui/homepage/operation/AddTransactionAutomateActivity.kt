package com.bs.sriwilis.ui.homepage.operation

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CartOrderAdapter
import com.bs.sriwilis.databinding.ActivityAddTransactionAutomateBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.model.CartTransaction
import com.bs.sriwilis.ui.scheduling.SchedulingDetailViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import kotlinx.coroutines.launch

class AddTransactionAutomateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionAutomateBinding

    private val viewModel by viewModels<SchedulingDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val viewModelTransaction by viewModels<ManageTransactionAutomateViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: CartOrderAdapter

    private var selectedId: String? = null
    private var selectedDate: String? = null

    private var totalWeight: Int = 0
    private var totalPrice: Float = 0.0f
    private var cartItems = mutableListOf<CardDetailPesanan>()
    private var idKeranjang: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionAutomateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CartOrderAdapter(emptyList(), this)

        setupAdapter()

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }

        val orderId = intent.getStringExtra("id")
        val nasabahId = intent.getStringExtra("id_nasabah")
        val keranjangId = intent.getStringExtra("id_keranjang_transaksi")

        orderId.let {
            lifecycleScope.launch {
                Log.d("jalan sampe sini ga diaaaaaa?", "$nasabahId")
                viewModel.getPesananSampahKeranjang()
                if (orderId != null) {
                    viewModel.getDataDetailPesananSampahKeranjang(orderId)
                    viewModelTransaction.getPesananSampahKeranjangScheduled()
                    viewModel.getTransaksiListDetailById(orderId)
                    observeTransaction()
                    observeCartDetails()
                }
            }
        }



        binding.btnSave.setOnClickListener {
            if (nasabahId != null) {
                selectedDate?.let { date -> submitTransaction(nasabahId, date) }
            }
        }

    }

    private fun setupAdapter() {
        binding.rvTransactionCart.layoutManager = LinearLayoutManager(this)
        binding.rvTransactionCart.adapter = adapter
    }

    private fun observeTransaction() {
        viewModel.pesananSampah.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val detailTransaksi = result.data

                    binding.edtNameForm.text = detailTransaksi.nama_nasabah.toEditable()
                    binding.tvSelectedDate.text = detailTransaksi.tanggal

                    selectedId = detailTransaksi.id_nasabah
                    selectedDate = detailTransaksi.tanggal
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun observeCartDetails() {
        viewModel.transaksiSampahDetailList.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val cartDetails = result.data
                    cartItems.clear()
                    cartItems.addAll(cartDetails)

                    adapter.updateOrder(cartItems)

                    calculateTotal(cartItems.map { mapCardDetailToCartTransaction(it) })
                    Log.d("cart cart cart", "$cartItems")
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("Transaction", "Error fetching details: ${result.error}")
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun submitTransaction(nasabahId: String, selectedDate: String) {
        val idNasabah = nasabahId
        val tanggal = formatDate(selectedDate)

        if (idNasabah != null && tanggal != null && cartItems.isNotEmpty()) {
            val mappedCartItems = cartItems.map { mapCardDetailToCartTransaction(it) }

            viewModelTransaction.addCartTransaction(idNasabah, tanggal, mappedCartItems)

            viewModelTransaction.transactionResult.observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        showToast(getString(R.string.tv_on_process))
                    }
                    is Result.Success -> {
                        lifecycleScope.launch {
                            viewModel.syncDataTransaction()
                            binding.progressBar.visibility = View.GONE
                            showToast(getString(R.string.tv_transaction_success))
                            finish()
                        }
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast("Error: ${result.error}")
                    }
                }
            }
        } else {
            showToast("Please select a user, date, and add items to the cart")
        }
    }


    private fun mapCardDetailToCartTransaction(cardDetail: CardDetailPesanan): CartTransaction {
        return CartTransaction(
            gambar =  cardDetail.gambar ?: "",
            berat = cardDetail.berat,
            kategori = cardDetail.nama_kategori,
            harga = cardDetail.harga ?: 0.0f
        )
    }

    private fun formatDate(date: String): String {
        val parts = date.split("-")

        return if (parts.size == 3) {
            "${parts[2]}-${parts[1]}-${parts[0]}"
        } else {
            Log.e("formatDate", "Invalid date format: $date")
            date
        }
    }

    private fun calculateTotal(cartTransactions: List<CartTransaction>) {
        totalWeight = cartTransactions.sumOf { it.berat }
        totalPrice = cartTransactions.sumOf { it.harga?.toDouble() ?: 0.0 }.toFloat()

        Log.d("TotalCalculation", "Total weight: $totalWeight kg, Total price: Rp $totalPrice")

        binding.tvWeightEstimation.text = "$totalWeight kg"
        binding.tvPriceEstimation.text = "Rp $totalPrice"
    }

    fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
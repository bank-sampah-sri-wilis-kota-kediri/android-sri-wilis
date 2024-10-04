package com.bs.sriwilis.ui.scheduling

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bs.sriwilis.R
import com.bs.sriwilis.adapter.CartOrderAdapter
import com.bs.sriwilis.databinding.ActivitySchedulingDetailBinding
import com.bs.sriwilis.databinding.ActivitySchedulingDetailDoneBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch

class SchedulingDetailDoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySchedulingDetailDoneBinding
    private lateinit var dateTextView: TextView
    private lateinit var selectDateButton: Button
    private lateinit var adapter: CartOrderAdapter
    private var selectedDate: String? = null


    private val viewModel by viewModels<SchedulingDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulingDetailDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }

        val orderId = intent.getStringExtra("id")
        val nasabahId = intent.getStringExtra("nasabahId")

        orderId.let {
            lifecycleScope.launch {
                Log.d("jalan sampe sini ga diaaaaaa?", "tes tes")
                viewModel.getPesananSampahKeranjang()
                if (orderId != null) {
                    viewModel.getDataDetailPesananSampahKeranjang(orderId)
                    viewModel.getPesananSampahKeranjangList(orderId)
                }
            }
        }

        setupDataKeranjang()

        viewModel.pesananSampahDetail.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    result.data.let { orders ->
                        if (orders.isEmpty()) {
                            Log.e("Error", "Data order kosong")
                        } else {
                            Log.d("Order Size", "Jumlah order: ${orders.size}")
                            adapter.updateOrder(orders)
                        }
                    }
                }

                is Result.Error -> {
                    Toast.makeText(this, "Gagal memuat data: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Result.Loading -> {
                    Log.d("Loading", "Loading")
                }
            }
        }

        Log.d("orderId cik", "$orderId")


        adapter = CartOrderAdapter(emptyList(), this)

        binding.rvPesanan.layoutManager = LinearLayoutManager(this)
        binding.rvPesanan.adapter = adapter
    }

    private fun setupDataKeranjang() {
        viewModel.pesananSampah.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val dataKeranjang = result.data

                    binding.tvNamaDetailPesanan.text = dataKeranjang.nama_nasabah
                    binding.tvNomorwaDetailPesanan.text = dataKeranjang.no_hp_nasabah
                    binding.tvAlamatDetailPesanan.text = dataKeranjang.alamat_nasabah
                    binding.tvBeratDetailPesanan.text = dataKeranjang.total_berat.toString() + "kg"
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                else -> {}
            }
        })
    }
}
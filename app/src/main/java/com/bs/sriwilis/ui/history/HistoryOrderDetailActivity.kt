package com.bs.sriwilis.ui.history

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
import com.bs.sriwilis.databinding.ActivityHistoryOrderDetailBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.scheduling.SchedulingDetailViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import kotlinx.coroutines.launch

class HistoryOrderDetailActivity : AppCompatActivity() {

    private lateinit var adapter: CartOrderAdapter
    private lateinit var binding: ActivityHistoryOrderDetailBinding

    private val viewModel by viewModels<SchedulingDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBack.setOnClickListener { finish() }
        }

        val transactionId = intent.getStringExtra("id")

        Log.d("transactionId", "$transactionId")

        transactionId.let {
            lifecycleScope.launch {
                Log.d("jalan sampe sini ga diaaaaaa?", "tes tes")
                viewModel.getPesananSampahKeranjang()
                if (transactionId != null) {
                    viewModel.getTransaksiListDetailById(transactionId)
                }
            }
        }

        viewModel.transaksiSampahDetailList.observe(this) { result ->
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


        adapter = CartOrderAdapter(emptyList(), this)

        binding.rvPesanan.layoutManager = LinearLayoutManager(this)
        binding.rvPesanan.adapter = adapter
    }

/*    private fun setupDataKeranjang() {
        viewModel.transaksiSampah.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val dataKeranjang = result.data

                        binding.tvNamaDetailPesanan.text = dataKeranjang.nama_nasabah
                        binding.tvNomorwaDetailPesanan.text = transaksi.no_hp_nasabah
                        binding.tvAlamatDetailPesanan.text = transaksi.alamat_nasabah
                        binding.tvDateResult.text = transaksi.tanggal
                        binding.tvBeratDetailPesanan.text = transaksi.total_berat.toString() + " kg"
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                else -> {}
            }
        })
    }*/
}
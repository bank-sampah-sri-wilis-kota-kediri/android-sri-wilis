package com.bs.sriwilis.adapter

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardHistoryOrderBinding
import com.bs.sriwilis.databinding.CardOrderBinding
import com.bs.sriwilis.helper.Result
import com.bs.sriwilis.ui.history.HistoryOrderDetailActivity
import com.bs.sriwilis.ui.history.ManageHistoryOrderViewModel
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.scheduling.SchedulingDetailActivity
import com.bs.sriwilis.ui.scheduling.SchedulingDetailDoneActivity
import com.bs.sriwilispetugas.data.repository.modelhelper.CardTransaksi
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryOrderAdapter(
    private var transaction: List<CardTransaksi?>,
    private val context: Context

) : RecyclerView.Adapter<HistoryOrderAdapter.HistoryOrderViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()

    inner class HistoryOrderViewHolder(private val binding: CardHistoryOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: CardTransaksi?) {
            with(binding) {

                when (transaction?.status_transaksi) {

                }

                binding.tvStatusHistoryOrder.text = transaction?.status_transaksi
                tvNamaPesanan.text = transaction?.nama_nasabah

                val originalDate = transaction?.tanggal
                if (originalDate == "0001-01-01") {
                    tvTanggalPesanan.text = "Belum Ditentukan"
                } else {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))

                    try {
                        val date = inputFormat.parse(originalDate)
                        val formattedDate = outputFormat.format(date)
                        tvTanggalPesanan.text = convertDateToText(formattedDate)
                    } catch (e: Exception) {
                        tvTanggalPesanan.text = originalDate?.let { convertDateToText(it) }
                    }
                }

                tvBeratTransaksi.text = transaction?.total_berat?.toString() + " kg"
                tvNomorWaPesanan.text = transaction?.no_hp_nasabah


/*                itemView.setOnClickListener {
                    Log.d("itemviewclicktest", "ItemView clicked")
                    transaction?.let {
                        onItemClick?.invoke(it.id)
                        val intent = Intent(itemView.context, HistoryOrderDetailActivity::class.java)
                        intent.putExtra("id", it.id)
                        itemView.context.startActivity(intent)
                    }
                }*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderViewHolder {
        val binding = CardHistoryOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity

        return HistoryOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transaction.size
    }

    override fun onBindViewHolder(holder: HistoryOrderViewHolder, position: Int) {
        holder.bind(transaction[position])
        holder.itemView.setOnClickListener {
            transaction[position]?.let {
                val intent = Intent(holder.itemView.context, HistoryOrderDetailActivity::class.java)
                intent.putExtra("id", it.id)
                holder.itemView.context.startActivity(intent)
            }
        }
    }


    fun updateOrder(newOrders: List<CardTransaksi?>) {
        this.transaction = newOrders
        notifyDataSetChanged()
    }
//
//    private fun showDeleteConfirmationDialog(catalogId: String) {
//        val dialogBuilder = AlertDialog.Builder(context)
//        dialogBuilder.setTitle("Konfirmasi Penghapusan Kategori")
//        dialogBuilder.setMessage("Anda yakin ingin menghapus kategori ini??")
//        dialogBuilder.setPositiveButton("Ya") { _, _ ->
//            viewModel.deleteCategory(catalogId)
//        }
//        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
//            dialog.dismiss()
//        }
//        val alertDialog = dialogBuilder.create()
//        alertDialog.show()
//    }

    private fun convertDateToText(date: String): String {
        val months = arrayOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus",
            "September", "Oktober", "November", "Desember"
        )

        val parts = date.split("-")
        val day = parts[0]
        val month = months[parts[1].toInt() - 1]
        val year = parts[2]

        return "$day $month $year"
    }

}

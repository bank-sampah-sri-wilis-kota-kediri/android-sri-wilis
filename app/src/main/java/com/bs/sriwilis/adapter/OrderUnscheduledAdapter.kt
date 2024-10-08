package com.bs.sriwilis.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.CatalogData
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.DataKeranjangItem
import com.bs.sriwilis.data.response.UserData
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardOrderSchedulingBinding
import com.bs.sriwilis.databinding.CardUserListBinding
import com.bs.sriwilis.databinding.CardWasteCatalogBinding
import com.bs.sriwilis.ui.homepage.operation.EditCatalogActivity
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.EditUserActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCatalogViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageUserViewModel
import com.bs.sriwilis.ui.scheduling.OrderSchedulingViewModel
import com.bs.sriwilis.ui.scheduling.SchedulingDetailActivity
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class OrderUnscheduledAdapter(
    private var unscheduledOrder: List<CardPesanan?>,
    private val context: Context,
    private var viewModel: OrderSchedulingViewModel
) : RecyclerView.Adapter<OrderUnscheduledAdapter.OrderUnscheduledViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    inner class OrderUnscheduledViewHolder(private val binding: CardOrderSchedulingBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(unscheduledOrder: CardPesanan?) {
            with(binding) {

                when (unscheduledOrder?.status_pesanan) {
                    "Pending" -> tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.pendingColor))
                    "Confirmed" -> tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.confirmedColor))
                    else -> tvStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.defaultColor))
                }

                tvNamaPesanan.text = unscheduledOrder?.nama_nasabah

                val originalDate = unscheduledOrder?.tanggal
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

                tvBeratTransaksi.text = unscheduledOrder?.total_berat.toString() + " kg"

                itemView.setOnClickListener {
                    unscheduledOrder?.let { order ->
                        onItemClick?.invoke(order.id_pesanan)

                        val intent = Intent(itemView.context, SchedulingDetailActivity::class.java)
                        intent.putExtra("id", order.id_pesanan)
                        itemView.context.startActivity(intent)
                    }
                }

                tvNomorWaPesanan.text = unscheduledOrder?.no_hp_nasabah.toString()
                tvStatus.text = unscheduledOrder?.status_pesanan
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderUnscheduledViewHolder {
        val binding = CardOrderSchedulingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[OrderSchedulingViewModel::class.java]

        return OrderUnscheduledViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return unscheduledOrder.size
    }

    override fun onBindViewHolder(holder: OrderUnscheduledViewHolder, position: Int) {
        holder.bind(unscheduledOrder[position])
    }

    fun updateOrder(filtererdOrder: List<CardPesanan?>) {
        Log.d("OrderScheduledAdapter", "Updating catalog with ${filtererdOrder.size} items")
        this.unscheduledOrder = filtererdOrder
        notifyDataSetChanged()
    }

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

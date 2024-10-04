package com.bs.sriwilis.adapter

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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.CatalogData
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.DataKeranjangItem
import com.bs.sriwilis.data.response.PesananSampahItem
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
import com.bs.sriwilis.ui.scheduling.SchedulingDetailDoneActivity
import com.bs.sriwilis.utils.ViewModelFactory
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderScheduledAdapter(
    private var scheduledOrder: List<CardPesanan?>,
    private val context: Context,
    private var viewModel: OrderSchedulingViewModel
) : RecyclerView.Adapter<OrderScheduledAdapter.OrderScheduledViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    inner class OrderScheduledViewHolder(private val binding: CardOrderSchedulingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(scheduledOrder: CardPesanan?) {
            with(binding) {
                tvNamaPesanan.text = scheduledOrder?.nama_nasabah
                tvTanggalPesanan.text = scheduledOrder?.tanggal ?: "Belum Ditentukan"
                tvBeratTransaksi.text = scheduledOrder?.total_berat.toString() + " kg"

                itemView.setOnClickListener {
                    scheduledOrder?.let { order ->
                        onItemClick?.invoke(order.id_pesanan)

                        val intent = Intent(itemView.context, SchedulingDetailDoneActivity::class.java)
                        intent.putExtra("id", order.id_pesanan)
                        intent.putExtra("nasabahId", order.id_pesanan)
                        itemView.context.startActivity(intent)
                    }
                }

                tvNomorWaPesanan.text = scheduledOrder?.no_hp_nasabah.toString()
                tvStatus.text = scheduledOrder?.status_pesanan
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderScheduledViewHolder {
        val binding = CardOrderSchedulingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[OrderSchedulingViewModel::class.java]

        return OrderScheduledViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return scheduledOrder.size
    }

    override fun onBindViewHolder(holder: OrderScheduledViewHolder, position: Int) {
        holder.bind(scheduledOrder[position])
    }

    fun updateOrder(filtererdOrder: List<CardPesanan?>) {
        Log.d("OrderScheduledAdapter", "Updating catalog with ${filtererdOrder.size} items")
        this.scheduledOrder = filtererdOrder
        notifyDataSetChanged()
    }

/*
    private fun showDeleteConfirmationDialog(catalogId: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Konfirmasi Penghapusan Kategori")
        dialogBuilder.setMessage("Anda yakin ingin menghapus kategori ini?")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->
            viewModel.deleteCatalog(catalogId)
            viewModel.getCatalog()
        }
        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
*/
}

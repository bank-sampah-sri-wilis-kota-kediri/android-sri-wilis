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
import com.bs.sriwilis.ui.scheduling.OrderUnschedulingViewModel
import com.bs.sriwilis.ui.scheduling.SchedulingDetailActivity
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderUnscheduledAdapter(
    private var unscheduledOrder: List<DataKeranjangItem?>,
    private val context: Context,
    private var viewModel: OrderUnschedulingViewModel
) : RecyclerView.Adapter<OrderUnscheduledAdapter.OrderUnscheduledViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    inner class OrderUnscheduledViewHolder(private val binding: CardOrderSchedulingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(unscheduledOrder: DataKeranjangItem?) {
            with(binding) {
                unscheduledOrder?.idNasabah?.let { nasabahId ->
                    viewModel.getCustomerName(nasabahId) { customerName ->
                        tvNamaPesanan.text = customerName
                    }
                }

                tvTanggalPesanan.text = unscheduledOrder?.tanggal ?: "Belum Ditentukan"

                val totalBerat = unscheduledOrder?.pesananSampah?.sumOf { it?.beratPerkiraan ?: 0 } ?: 0
                tvBeratTransaksi.text = "$totalBerat kg"

                itemView.setOnClickListener {
                    unscheduledOrder?.let { order ->
                        onItemClick?.invoke(order.idNasabah)

                        val intent = Intent(itemView.context, SchedulingDetailActivity::class.java)
                        intent.putExtra("id", order.idPesanan)
                        intent.putExtra("nasabahId", order.idNasabah)
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderUnscheduledViewHolder {
        val binding = CardOrderSchedulingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[OrderUnschedulingViewModel::class.java]

        return OrderUnscheduledViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return unscheduledOrder.size
    }

    override fun onBindViewHolder(holder: OrderUnscheduledViewHolder, position: Int) {
        holder.bind(unscheduledOrder[position])
    }

    fun updateOrder(filtererdOrder: List<DataKeranjangItem?>) {
        Log.d("OrderScheduledAdapter", "Updating catalog with ${filtererdOrder.size} items")
        this.unscheduledOrder = filtererdOrder
        notifyDataSetChanged()
    }

}

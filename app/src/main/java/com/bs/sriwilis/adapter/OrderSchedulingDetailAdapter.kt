package com.bs.sriwilis.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.data.response.DataKeranjangItem
import com.bs.sriwilis.data.response.PesananSampahItem
import com.bs.sriwilis.databinding.CardOrderSchedulingBinding
import com.bs.sriwilis.databinding.CardOrderSchedulingDetailListBinding
import com.bs.sriwilis.ui.homepage.operation.EditCatalogActivity
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.scheduling.SchedulingDetailActivity

class OrderSchedulingDetailAdapter(
    private var unscheduledOrder: List<PesananSampahItem?>,
    private val context: Context,
) : RecyclerView.Adapter<OrderSchedulingDetailAdapter.OrderSchedulingDetailViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    inner class OrderSchedulingDetailViewHolder(private val binding: CardOrderSchedulingDetailListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartOrder: PesananSampahItem?) {
            with(binding) {

                tvKategoriPesanan.text = cartOrder?.kategori.toString()
                tvBeratPesanan.text = cartOrder?.beratPerkiraan.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderSchedulingDetailViewHolder {
        val binding = CardOrderSchedulingDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OrderSchedulingDetailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return unscheduledOrder.size
    }

    override fun onBindViewHolder(holder: OrderSchedulingDetailViewHolder, position: Int) {
        holder.bind(unscheduledOrder[position])
    }

    fun updateCatalog(filtererdOrder: List<PesananSampahItem?>) {
        Log.d("OrderScheduledAdapter", "Updating catalog with ${filtererdOrder.size} items")
        this.unscheduledOrder = filtererdOrder
        notifyDataSetChanged()
    }

}

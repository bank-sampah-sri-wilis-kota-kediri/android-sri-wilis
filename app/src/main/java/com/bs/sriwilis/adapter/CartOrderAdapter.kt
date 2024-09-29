package com.bs.sriwilis.adapter

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransaksiSampahItem
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardHistoryOrderBinding
import com.bs.sriwilis.databinding.CardMutationHistory2Binding
import com.bs.sriwilis.databinding.CardOrderBinding
import com.bs.sriwilis.databinding.CardOrderSchedulingDetailListBinding
import com.bs.sriwilis.ui.history.ManageHistoryOrderViewModel
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.scheduling.SchedulingDetailViewModel
import com.bumptech.glide.Glide

class CartOrderAdapter(
    private var transaction: List<TransaksiSampahItem?>,
    private val context: Context

) : RecyclerView.Adapter<CartOrderAdapter.HistoryOrderViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: SchedulingDetailViewModel


    inner class HistoryOrderViewHolder(private val binding: CardOrderSchedulingDetailListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransaksiSampahItem?) {
            with(binding) {
                tvBeratPesanan.text = transaction?.berat.toString() + " kg"
                tvKategoriPesanan.text = transaction?.kategori
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderViewHolder {
        val binding = CardOrderSchedulingDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[SchedulingDetailViewModel::class]

        return HistoryOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transaction.size
    }

    override fun onBindViewHolder(holder: HistoryOrderViewHolder, position: Int) {
        holder.bind(transaction[position])
    }

    fun updateOrder(newCategories: List<TransaksiSampahItem?>) {
        this.transaction = newCategories
        notifyDataSetChanged()
    }

/*    private fun showDeleteConfirmationDialog(catalogId: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Konfirmasi Penghapusan Kategori")
        dialogBuilder.setMessage("Anda yakin ingin menghapus kategori ini??")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->
            viewModel.deleteCategory(catalogId)
        }
        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }*/
}

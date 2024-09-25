/*
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
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardMutationHistory2Binding
import com.bs.sriwilis.databinding.CardOrderBinding
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bumptech.glide.Glide

class HistoryOrderAdapter(
    private var category: List<CategoryData?>,
    private val context: Context

) : RecyclerView.Adapter<HistoryOrderAdapter.HistoryOrderViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: ManageCategoryViewModel

    inner class HistoryOrderViewHolder(private val binding: CardOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryData?) {
            with(binding) {
                category?.gambarKategori?.let { gambarKategori ->
                    val imageBytes = Base64.decode(gambarKategori, Base64.DEFAULT)
                    Glide.with(itemView.context)
                        .load(imageBytes)
                        .into(ivOrder)
                } ?: run {
                    ivOrder.setImageResource(R.drawable.iv_waste_box)
                }

                tvOrderName.text = category?.namaKategori
                tvOrderDate.text = category?.jenisKategori
                tvOrderWeight.text = category?.hargaKategori.toString()
                tvCardStatusProcess.text

                itemView.setOnClickListener {
                    category?.id?.let { id ->
                        onItemClick?.invoke(id)
                        val intent = Intent(itemView.context, EditCategoryActivity::class.java)
                        intent.putExtra("id", id)
                        itemView.context.startActivity(intent)
                    }
                }

                btnDelete.setOnClickListener {
                    category?.id?.let { id ->
                        showDeleteConfirmationDialog(id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderViewHolder {
        val binding = CardOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[ManageCategoryViewModel::class.java]

        return HistoryOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: HistoryOrderViewHolder, position: Int) {
        holder.bind(category[position])
    }

    fun updateCategory(newCategories: List<CategoryData?>) {
        this.category = newCategories
        notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(catalogId: String) {
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
    }
}*/

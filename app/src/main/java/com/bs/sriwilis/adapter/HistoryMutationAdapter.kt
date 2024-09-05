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
import com.bs.sriwilis.databinding.CardMutationHistory2Binding
import com.bs.sriwilis.databinding.CardOrderBinding
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bumptech.glide.Glide

class HistoryMutationAdapter(
    private var category: List<CategoryData?>,
    private val context: Context

) : RecyclerView.Adapter<HistoryMutationAdapter.HistoryMutationAdapterViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: ManageCategoryViewModel

    inner class HistoryMutationAdapterViewHolder(private val binding: CardMutationHistory2Binding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryData?) {
            with(binding) {

                tvMutationStatus.text = category?.namaKategori
                tvMutationUserTitle.text = category?.jenisKategori
                tvMutationDate.text = category?.hargaKategori.toString()
                tvMutationNominal.text

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryMutationAdapterViewHolder {
        val binding = CardMutationHistory2Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[ManageCategoryViewModel::class.java]

        return HistoryMutationAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: HistoryMutationAdapterViewHolder, position: Int) {
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

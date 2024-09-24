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
import com.bs.sriwilis.data.response.UserData
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardUserListBinding
import com.bs.sriwilis.databinding.CardWasteCatalogBinding
import com.bs.sriwilis.ui.homepage.operation.EditCatalogActivity
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.EditUserActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCatalogViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageUserViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatalogAdapter(
    private var catalog: List<CatalogData?>,
    private val context: Context

) : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: ManageCatalogViewModel

    inner class CatalogViewHolder(private val binding: CardWasteCatalogBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(catalog: CatalogData?) {
            with(binding) {
                catalog?.gambarKatalog?.let { gambarKategori ->
                    val imageBytes = Base64.decode(gambarKategori, Base64.DEFAULT)
                    Glide.with(itemView.context)
                        .load(imageBytes)
                        .into(ivCategoryListPreview)
                } ?: run {
                    ivCategoryListPreview.setImageResource(R.drawable.iv_panduan2)
                }

                tvCatalogName.text = catalog?.hargaKatalog.toString()
                tvCatalogListType?.text = catalog?.shopeeLink
                tvCatalogPrice?.text = "Rp" + catalog?.hargaKatalog.toString()
                descCatalog.text = catalog?.deskripsiKatalog

                itemView.setOnClickListener {
                    catalog?.id?.let { id ->
                        onItemClick?.invoke(id)
                        val intent = Intent(itemView.context, EditCatalogActivity::class.java)
                        intent.putExtra("id", id)
                        itemView.context.startActivity(intent)
                    }
                }

                btnDelete.setOnClickListener {
                    catalog?.id?.let { id ->
                        showDeleteConfirmationDialog(id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding = CardWasteCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[ManageCatalogViewModel::class.java]

        return CatalogViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return catalog.size
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(catalog[position])
    }

    fun updateCatalog(newCatalog: List<CatalogData?>) {
        this.catalog = newCatalog
        notifyDataSetChanged()
    }

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


}

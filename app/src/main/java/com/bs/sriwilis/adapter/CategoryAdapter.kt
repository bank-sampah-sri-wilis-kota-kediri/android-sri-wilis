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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.repository.modelhelper.CardCategory
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.UserData
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardUserListBinding
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.EditUserActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.homepage.operation.ManageUserViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryAdapter(
    private var category: List<CardCategory?>,
    private val context: Context

) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: ManageCategoryViewModel

    inner class CategoryViewHolder(private val binding: CardCategoryListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CardCategory?) {
            with(binding) {
                category?.gambar_kategori?.let { gambarKategori ->
                    val imageBytes = Base64.decode(gambarKategori, Base64.DEFAULT)
                    Glide.with(itemView.context)
                        .load(imageBytes)
                        .into(ivCategoryListPreview)
                } ?: run {
                    ivCategoryListPreview.setImageResource(R.drawable.iv_waste_box)
                }

                tvCategoryListName.text = category?.nama_kategori
                tvCategoryListType.text = category?.jenis_kategori
                tvCategoryItemPrice.text = "Rp" + category?.harga_kategori.toString()

                itemView.setOnClickListener {
                    category?.id?.let { id ->
                        onItemClick?.invoke(id)
                        val intent = Intent(itemView.context, EditCategoryActivity::class.java)
                        intent.putExtra("id", id)
                        Log.d("id adapter kategori bos", "$id",)
                        itemView.context.startActivity(intent)
                    }
                }

                btnDelete.setOnClickListener {
                    category?.id?.let { id ->
                        showDeleteConfirmationDialog(itemView, id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CardCategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[ManageCategoryViewModel::class.java]

        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(category[position])
    }

    fun updateCategory(newCategories: List<CardCategory?>) {
        this.category = newCategories
        notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(itemView: View, categoryId: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Konfirmasi Penghapusan Kategori")
        dialogBuilder.setMessage("Anda yakin ingin menghapus kategori ini??")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->

            val activity = itemView.context as AppCompatActivity
            activity.lifecycleScope.launch {
                viewModel.deleteCategory(categoryId)
                viewModel.syncData()
                viewModel.getCategory()
            }
        }
        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}

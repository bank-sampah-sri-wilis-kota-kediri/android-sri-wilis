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
import com.bs.sriwilis.databinding.CardOrderSchedulingDetailListBinding
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

class CartAdapter(
    private var cart: List<PesananSampahItem?>,
    private val context: Context

) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: ManageCatalogViewModel

    inner class CartViewHolder(private val binding: CardOrderSchedulingDetailListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: PesananSampahItem?) {
            with(binding) {
                tvKategoriPesanan.text = cart?.kategori
                tvBeratPesanan.text = cart?.beratPerkiraan.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CardOrderSchedulingDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[ManageCatalogViewModel::class.java]

        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cart.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cart[position])
    }

    fun updateCatalog(newCart: List<PesananSampahItem?>) {
        this.cart = newCart
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

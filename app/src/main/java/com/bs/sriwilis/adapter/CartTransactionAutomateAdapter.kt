package com.bs.sriwilis.adapter

import android.content.Context
import android.content.Intent
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransaksiSampahItem
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardHistoryOrderBinding
import com.bs.sriwilis.databinding.CardOrderBinding
import com.bs.sriwilis.databinding.CardOrderSchedulingDetailListBinding
import com.bs.sriwilis.databinding.CardTransactionCartBinding
import com.bs.sriwilis.model.CartTransaction
import com.bs.sriwilis.ui.history.ManageHistoryOrderViewModel
import com.bs.sriwilis.ui.homepage.operation.AddTransactionAutomateActivity
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.scheduling.SchedulingDetailViewModel
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class  CartTransactionAutomateAdapter(
    private var transaction: List<CardDetailPesanan?>,
    private val context: Context,
    private val updateCallback: (String, Float, Float) -> Unit

) : RecyclerView.Adapter<CartTransactionAutomateAdapter.HistoryOrderViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: SchedulingDetailViewModel


    inner class HistoryOrderViewHolder(private val binding: CardTransactionCartBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: CardDetailPesanan?) {
            with(binding) {
                tvBeratPesanan.text = String.format("%.2f kg", transaction?.berat ?: 0.0)
                tvKategoriPesanan.text = transaction?.nama_kategori

                btnDelete.setOnClickListener {
                    transaction?.id.let { keranjangId ->
                        if (keranjangId != null) {
                            showDeleteConfirmationDialog(itemView, keranjangId)
                        }
                    }
                }

                btnEdit.setOnClickListener {
                    transaction?.id.let { keranjangId ->
                        if (keranjangId != null) {
                            showInputBeratDialog(keranjangId)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderViewHolder {
        val binding = CardTransactionCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

    fun updateOrder(newCategories: List<CardDetailPesanan?>) {
        this.transaction = newCategories
        notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(itemView: View, keranjangId: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Konfirmasi Penghapusan Item Keranjang")
        dialogBuilder.setMessage("Anda yakin ingin menghapus item ini??")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->
            val activity = itemView.context as AppCompatActivity
            activity.lifecycleScope.launch {
                viewModel.deleteKeranjang(keranjangId)
                viewModel.syncDataTransaction()
            }
        }
        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun showInputBeratDialog(keranjangId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Masukkan Berat")

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.hint = "Masukkan berat (kg)"

        // Find the item to edit and set the current weight
        val item = transaction.find { it?.id == keranjangId }
        input.setText(item?.berat?.toString() ?: "0") // Show current weight or default to 0

        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val berat = input.text.toString().toFloatOrNull()

            if (berat != null) {
                val updatedItemPrice = (item?.harga ?: 0.0f) * berat
                item?.berat = berat
                item?.harga = updatedItemPrice

                updateOrder(transaction)

                val updatedTransactions = transaction.map { mapCardDetailToCartTransaction(it!!) }
                (context as AddTransactionAutomateActivity).calculateTotal(updatedTransactions)

                context.recalculateTotalPrice()

                updateCallback(keranjangId, berat, updatedItemPrice)
            } else {
                Toast.makeText(context, "Masukkan angka yang valid", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }


    private fun mapCardDetailToCartTransaction(cardDetail: CardDetailPesanan): CartTransaction {
        val totalPrice = (cardDetail.harga ?: 0.0f) * (cardDetail.berat ?: 0.0f)
        Log.d("coba liat dah", "$totalPrice")

        return CartTransaction(
            gambar = cardDetail.gambar ?: "",
            berat = cardDetail.berat,
            kategori = cardDetail.nama_kategori,
            harga = totalPrice
        )
    }
}

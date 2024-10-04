package com.bs.sriwilis.adapter

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.PenarikanData
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransaksiSampahItem
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardHistoryOrderBinding
import com.bs.sriwilis.databinding.CardMutationHistoryConfirmedBinding
import com.bs.sriwilis.databinding.CardMutationHistoryNotConfirmedBinding
import com.bs.sriwilis.databinding.CardOrderBinding
import com.bs.sriwilis.databinding.CardOrderSchedulingDetailListBinding
import com.bs.sriwilis.ui.history.ManageHistoryMutationViewModel
import com.bs.sriwilis.ui.history.ManageHistoryOrderViewModel
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.scheduling.SchedulingDetailViewModel
import com.bumptech.glide.Glide

class MutationConfirmedAdapter(
    private var mutations: List<PenarikanData?>,
    private val context: Context

) : RecyclerView.Adapter<MutationConfirmedAdapter.HistoryOrderViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: ManageHistoryMutationViewModel


    inner class HistoryOrderViewHolder(private val binding: CardMutationHistoryConfirmedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mutation: PenarikanData?) {
            with(binding) {
                mutation?.idNasabah?.let { nasabahId ->
                    viewModel.getCustomerName(nasabahId.toString()) { customerName ->
                        tvMutationUserTitle.text = customerName
                    }
                }
                tvMutationDate.text = mutation?.tanggal
                tvMutationNominal.text = "Rp" + mutation?.nominal.toString()

                when (mutation?.jenisPenarikan) {
                    "PLN" -> tvMutationStatus.text = "Token Listrik PLN"
                    "Tunai" -> tvMutationStatus.text = "Pencairan Tunai"
                    "Transfer" -> tvMutationStatus.text = "Pencairan Transfer"
                }

                val statusColor = when (mutation?.statusPenarikan) {
                    "Berhasil" -> ContextCompat.getColor(itemView.context, R.color.yellow_primary)
                    "Gagal" -> ContextCompat.getColor(itemView.context, R.color.red_primary)
                    else -> ContextCompat.getColor(itemView.context, R.color.green_teal)
                }

                cvStatusCard.setCardBackgroundColor(statusColor)

                tvStatusMutation.text = mutation?.statusPenarikan
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderViewHolder {
        val binding = CardMutationHistoryConfirmedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[ManageHistoryMutationViewModel::class]

        return HistoryOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mutations.size
    }

    override fun onBindViewHolder(holder: HistoryOrderViewHolder, position: Int) {
        holder.bind(mutations[position])
    }

    fun updateMutation(newMutations: List<PenarikanData?>) {
        this.mutations = newMutations
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

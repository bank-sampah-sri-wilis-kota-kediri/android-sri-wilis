package com.bs.sriwilis.adapter

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.repository.modelhelper.CardPenarikan
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
    private var mutations: List<CardPenarikan?>,
    private val context: Context

) : RecyclerView.Adapter<MutationConfirmedAdapter.HistoryOrderViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var categorylist: List<String> = emptyList()
    private lateinit var viewModel: ManageHistoryMutationViewModel


    inner class HistoryOrderViewHolder(private val binding: CardMutationHistoryConfirmedBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mutation: CardPenarikan?) {
            with(binding) {
                mutation?.id_nasabah?.let { nasabahId ->
                    viewModel.getCustomerName(nasabahId) { customerName ->
                        tvMutationUserTitle.text = customerName
                    }
                }
                tvMutationDate.text = mutation?.tanggal
                tvMutationNominal.text = "Rp" + mutation?.nominal.toString()

                when (mutation?.jenis_penarikan) {
                    "PLN" -> tvMutationStatus.text = "Token Listrik PLN"
                    "Tunai" -> tvMutationStatus.text = "Pencairan Tunai"
                    "Transfer" -> tvMutationStatus.text = "Pencairan Transfer"
                }

                when (mutation?.jenis_penarikan) {
                    "PLN" -> tvToken.visibility = View.VISIBLE
                }

                when (mutation?.status_penarikan) {
                    "Berhasil" -> cvStatusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green_label))
                    "Gagal" -> cvStatusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_primary))
                    else -> cvStatusCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green_teal))
                }

                tvStatusMutation.text = mutation?.status_penarikan

                tvToken.text = "No Token: " + mutation?.nomor_token

                itemView.setOnClickListener {
                    showMutationDetails(mutation)
                }
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

    fun updateMutation(newMutations: List<CardPenarikan?>) {
        this.mutations = newMutations
        notifyDataSetChanged()
    }

    private fun showMutationDetails(mutation: CardPenarikan?) {
        mutation?.let {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setTitle("Detail Mutasi")

            val message = StringBuilder().apply {
                append("Nasabah ID: ${mutation.id_nasabah}\n")
                append("Tanggal: ${mutation.tanggal}\n")
                append("Nominal: Rp${mutation.nominal}\n")
                append("Jenis Penarikan: ${mutation.jenis_penarikan}\n")
                append("Status Penarikan: ${mutation.status_penarikan}\n")
                if (mutation.jenis_penarikan == "PLN") {
                    append("Nomor Token: ${mutation.nomor_token}\n")
                }
            }.toString()

            dialogBuilder.setMessage(message)

            if (mutation.jenis_penarikan == "PLN") {
                dialogBuilder.setNeutralButton("Copy Token") { _, _ ->
                    mutation.nomor_token?.let { token ->
                        copyToClipboard(token.toString())
                    }
                }
            }

            dialogBuilder.setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun copyToClipboard(token: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Nomor Token", token)
        clipboard.setPrimaryClip(clip)

        // Show a toast to inform the user that the token has been copied
        android.widget.Toast.makeText(context, "Nomor token copied to clipboard", android.widget.Toast.LENGTH_SHORT).show()
    }
}

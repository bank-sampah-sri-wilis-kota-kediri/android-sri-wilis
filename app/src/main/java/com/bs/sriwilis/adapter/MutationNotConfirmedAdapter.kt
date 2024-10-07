package com.bs.sriwilis.adapter

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.repository.modelhelper.CardPenarikan
import com.bs.sriwilis.data.response.CategoryData
import com.bs.sriwilis.data.response.PenarikanData
import com.bs.sriwilis.data.response.TransactionDataItem
import com.bs.sriwilis.data.response.TransaksiSampahItem
import com.bs.sriwilis.databinding.ActivityMutationConfirmedListBinding
import com.bs.sriwilis.databinding.CardCategoryListBinding
import com.bs.sriwilis.databinding.CardHistoryOrderBinding
import com.bs.sriwilis.databinding.CardMutationHistoryNotConfirmedBinding
import com.bs.sriwilis.databinding.CardOrderBinding
import com.bs.sriwilis.databinding.CardOrderSchedulingDetailListBinding
import com.bs.sriwilis.ui.history.ManageHistoryMutationViewModel
import com.bs.sriwilis.ui.history.ManageHistoryOrderViewModel
import com.bs.sriwilis.ui.homepage.operation.EditCategoryActivity
import com.bs.sriwilis.ui.homepage.operation.ManageCategoryViewModel
import com.bs.sriwilis.ui.scheduling.SchedulingDetailViewModel
import com.bumptech.glide.Glide

class MutationNotConfirmedAdapter(
    private var mutations: List<CardPenarikan?>,
    private val context: Context

) : RecyclerView.Adapter<MutationNotConfirmedAdapter.HistoryOrderViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private lateinit var viewModel: ManageHistoryMutationViewModel

    inner class HistoryOrderViewHolder(private val binding: CardMutationHistoryNotConfirmedBinding) : RecyclerView.ViewHolder(binding.root) {

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
                    "PLN" -> edtTextInputToken.visibility = View.VISIBLE
                }

                mutation?.id?.let { nasabahId ->
                    viewModel.getCustomerPhone(nasabahId) { customerPhone ->
                        tvMutationUserTitle.text = customerPhone
                    }
                }

                mutation?.id?.let { mutationId ->
                    btnRefuseMutation.setOnClickListener {
                        viewModel.updateStatus(mutationId, "Gagal")
                    }
                    btnAcceptMutation.setOnClickListener {
                        if (mutation.jenis_penarikan == "PLN") {
                            val token = binding.edtTextInputToken.text.toString().trim()
                            if (token.isNotEmpty() && token.length == 20) {
                                viewModel.updateStatusPLN(mutationId, "Berhasil", token)
                            } else {
                                binding.edtTextInputToken.error = "Token tidak valid (harus 20 karakter)"
                            }
                        } else if (mutation.jenis_penarikan == "Tunai" || mutation.jenis_penarikan == "Transfer") {
                            viewModel.updateStatus(mutationId, "Berhasil")
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryOrderViewHolder {
        val binding = CardMutationHistoryNotConfirmedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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
}

package com.bs.sriwilis.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.data.repository.modelhelper.CardPenarikan
import com.bs.sriwilis.databinding.CardMutationHistoryNotConfirmedBinding
import com.bs.sriwilis.ui.history.ManageHistoryMutationViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

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

                val originalDate = mutation?.tanggal
                if (originalDate == "0001-01-01") {
                    binding.tvMutationDate.text = "Belum Ditentukan"
                } else {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))

                    try {
                        val date = inputFormat.parse(originalDate)
                        val formattedDate = outputFormat.format(date)
                        binding.tvMutationDate.text = convertDateToText(formattedDate)
                    } catch (e: Exception) {
                        binding.tvMutationDate.text = originalDate?.let { convertDateToText(it) }
                    }
                }

                val nominal = mutation?.nominal?.toDoubleOrNull() ?: 0.0
                val formattedNominal = NumberFormat.getNumberInstance(Locale("id", "ID")).format(nominal)
                tvMutationNominal.text = "Rp" + formattedNominal

                when (mutation?.jenis_penarikan) {
                    "PLN" -> tvMutationStatus.text = "Token Listrik PLN"
                    "Tunai" -> tvMutationStatus.text = "Pencairan Tunai"
                    "Transfer" -> tvMutationStatus.text = "Pencairan Transfer"
                }

                when (mutation?.jenis_penarikan) {
                    "PLN" -> edtTextInputToken.visibility = View.VISIBLE
                }

                mutation?.id?.let { mutationId ->
                    btnRefuseMutation.setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Alasan Penolakan")

                        val input = android.widget.EditText(context)
                        input.hint = "Masukkan alasan penolakan"
                        builder.setView(input)

                        builder.setPositiveButton("Tolak") { dialog, _ ->
                            val alasanPenolakan = input.text.toString().trim()
                            if (alasanPenolakan.isNotEmpty()) {
                                viewModel.updateStatus(mutationId, "Gagal", alasanPenolakan = alasanPenolakan)
                            } else {
                                input.error = "Alasan penolakan harus diisi"
                            }
                            dialog.dismiss()
                        }

                        builder.setNegativeButton("Batal") { dialog, _ ->
                            dialog.cancel()
                        }

                        builder.show()
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

    private fun convertDateToText(date: String): String {
        val months = arrayOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus",
            "September", "Oktober", "November", "Desember"
        )

        val parts = date.split("-")
        val day = parts[0]
        val month = months[parts[1].toInt() - 1]
        val year = parts[2]

        return "$day $month $year"
    }
}

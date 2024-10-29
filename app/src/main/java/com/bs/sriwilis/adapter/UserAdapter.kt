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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.data.repository.modelhelper.CardNasabah
import com.bs.sriwilis.databinding.CardUserListBinding
import com.bs.sriwilis.ui.homepage.operation.EditUserActivity
import com.bs.sriwilis.ui.homepage.operation.ManageUserViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserAdapter(
    private var user: List<CardNasabah?>,
    private val context: Context
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private lateinit var viewModel: ManageUserViewModel

    inner class UserViewHolder(private val binding: CardUserListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(users: CardNasabah?) {
            with(binding) {
                users?.gambar_nasabah?.let { gambarNasabah ->
                    try {
                        val imageBytes = Base64.decode(gambarNasabah, Base64.DEFAULT)
                        Glide.with(itemView.context)
                            .load(imageBytes)
                            .into(ivCategoryListPreview)
                    } catch (e: IllegalArgumentException) {
                        // Handle bad Base64 input gracefully
                        e.printStackTrace()
                        ivCategoryListPreview.setImageResource(R.drawable.ic_profile) // Set a default image
                    }
                } ?: run {
                    ivCategoryListPreview.setImageResource(R.drawable.ic_profile)
                }

                tvUserName.text = users?.nama_nasabah
                tvUserAddress.text = users?.alamat_nasabah

                itemView.setOnClickListener {
                    users?.let { user ->
                        val phone = user.no_hp_nasabah
                        val userId = user.id

                        if (phone.isNotEmpty() && userId.isNotEmpty()) {
                            onItemClick?.invoke(phone)

                            val intent =
                                Intent(itemView.context, EditUserActivity::class.java).apply {
                                    putExtra("phone", phone)
                                    putExtra("userId", userId)
                                }

                            itemView.context.startActivity(intent)
                        } else {
                            Log.e("Error", "Phone or userId is null")
                        }
                    }
                }

                btnDelete.setOnClickListener {
                    users?.no_hp_nasabah?.let { phone ->
                        showDeleteConfirmationDialog(itemView, phone)
                        Log.d("cek ke klik kah", "btn delete $phone")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CardUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[ManageUserViewModel::class.java]

        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(user[position])
    }

    fun updateUsers(newUsers: List<CardNasabah?>) {
        this.user = newUsers
        notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(itemView: View, userPhone: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Konfirmasi Penghapusan Akun")
        dialogBuilder.setMessage("Anda yakin ingin menghapus akun ini?")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->
            val activity = itemView.context as AppCompatActivity
            activity.lifecycleScope.launch {
                viewModel.deleteUser(userPhone)
                viewModel.syncData()
                viewModel.getUsers()
            }
        }
        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}

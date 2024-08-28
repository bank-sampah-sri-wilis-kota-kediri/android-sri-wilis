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
import com.bs.sriwilis.data.response.UserData
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.databinding.CardUserListBinding
import com.bs.sriwilis.ui.homepage.operation.EditUserActivity
import com.bs.sriwilis.ui.homepage.operation.ManageUserViewModel
import com.bs.sriwilis.utils.ViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserAdapter(
    private var user: List<UserItem?>,
    private val context: Context

) : RecyclerView.Adapter<UserAdapter.NewsViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var userlist: List<String> = emptyList()
    private lateinit var viewModel: ManageUserViewModel

    inner class NewsViewHolder(private val binding: CardUserListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(users: UserItem?) {
            with(binding) {
                users?.gambarNasabah?.let { gambarNasabah ->
                    val imageBytes = Base64.decode(gambarNasabah, Base64.DEFAULT)
                    Glide.with(itemView.context)
                        .load(imageBytes)
                        .into(ivCategoryListPreview)
                } ?: run {
                    ivCategoryListPreview.setImageResource(R.drawable.ic_profile) // replace with your placeholder resource
                }

                tvUserName.text = users?.namaNasabah
                tvUserAddress.text = users?.alamatNasabah

                itemView.setOnClickListener {
                    users?.id?.let { id ->
                        onItemClick?.invoke(id.toString())
                        val intent = Intent(itemView.context, EditUserActivity::class.java)
                        intent.putExtra("userId", id)
                        itemView.context.startActivity(intent)
                    }
                }

                btnDelete.setOnClickListener {
                    users?.id?.let { id ->
                        showDeleteConfirmationDialog(id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = CardUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val activity = parent.context as AppCompatActivity
        viewModel = ViewModelProvider(activity)[ManageUserViewModel::class.java]

        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(user[position])
    }

    fun updateUsers(newUsers: List<UserItem?>) {
        this.user = newUsers
        notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(userId: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Konfirmasi Penghapusan Akun")
        dialogBuilder.setMessage("Anda yakin ingin menghapus akun ini??")
        dialogBuilder.setPositiveButton("Ya") { _, _ ->
            viewModel.deleteUser(userId)
        }
        dialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}

package com.bs.sriwilis.adapter

import android.content.Intent
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.data.repository.MainRepository
import com.bs.sriwilis.data.response.UserData
import com.bs.sriwilis.data.response.UserItem
import com.bs.sriwilis.databinding.CardUserListBinding
import com.bs.sriwilis.ui.homepage.operation.EditUserActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserAdapter(
    private var user: List<UserItem?>,
) : RecyclerView.Adapter<UserAdapter.NewsViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    private var userlist: List<String> = emptyList()

    inner class NewsViewHolder(private val binding: CardUserListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(users: UserItem?) {
            val imageBytes = Base64.decode(users?.gambarNasabah, Base64.DEFAULT)
            with(binding) {
                Glide.with(itemView.context)
                    .load(imageBytes)
                    .into(ivCategoryListPreview)
                tvUserName.text = users?.namaNasabah
                tvUserAddress.text = users?.alamatNasabah

                itemView.setOnClickListener {
                    users?.id?.let { id ->
                        onItemClick?.invoke(id.toString())
                        val intent = Intent(itemView.context, EditUserActivity::class.java)
                        intent.putExtra("newsId", id)
                        itemView.context.startActivity(intent)
                    }
                }

                /*binding.btnSaveArticle.setOnClickListener {
                    news?.newsId.let { savedNewsId ->
                        CoroutineScope(Dispatchers.Main).launch {
                            val toggleResult = savedNewsId?.let { it1 ->
                                repository.toggleBookmark(it1)
                            }

                            when (toggleResult) {
                                is Result.Success -> {
                                    toggleLoadingProgress(false)
                                    val isBookmarked = toggleResult.data
                                    updateBookmarkIcon(isBookmarked)
                                }
                                is Result.Error -> {
                                    toggleLoadingProgress(false)
                                    updateBookmarkIcon(true)
                                    Log.e("MainRepository", "Failed to toggle bookmark: ${toggleResult.error}")
                                }
                                null -> {
                                    Log.e("MainRepository", "Unexpected null result from toggleBookmark")
                                }

                                Result.Loading -> toggleLoadingProgress(true)
                            }
                        }
                    }
                }*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = CardUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
}

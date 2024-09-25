package com.bs.sriwilis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bs.sriwilis.R
import com.bs.sriwilis.model.CartTransaction
import com.bumptech.glide.Glide

class CartTransactionAdapter(private val cartItems: MutableList<CartTransaction>) : RecyclerView.Adapter<CartTransactionAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCartOrderPreview: ImageView = itemView.findViewById(R.id.iv_cart_order_preview)
        private val tvOrderName: TextView = itemView.findViewById(R.id.tv_order_name_cart)
        private val tvWeight: TextView = itemView.findViewById(R.id.tv_weight_order_cart)

        fun bind(cartTransaction: CartTransaction) {
            tvOrderName.text = cartTransaction.kategori
            tvWeight.text = cartTransaction.berat.toString() + "Kg"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_order_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    fun addCartTransaction(cartTransaction: CartTransaction) {
        cartItems.add(cartTransaction)
        notifyItemInserted(cartItems.size - 1)
    }
}

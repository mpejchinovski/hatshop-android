package com.example.hatshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.R
import com.example.hatshop.model.CartItem
import com.squareup.picasso.Picasso

class CartItemAdapter(private val dataset: ArrayList<CartItem>, private val listener: (CartItem) -> Unit) : RecyclerView.Adapter<CartItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.list_cart_item, parent, false)
        return ItemViewHolder(item)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val cartItem : CartItem = dataset[position]
        holder.name.text = cartItem.name
        holder.price.text = cartItem.price.toString()
        holder.quantity.text = cartItem.quantity.toString()
        Picasso.get().load(cartItem.downloadURL).into(holder.image)
        holder.itemView.setOnClickListener {
            listener(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    public class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.name)
        val price : TextView = view.findViewById(R.id.price)
        val quantity : TextView = view.findViewById(R.id.quantity)
        val image : ImageView = view.findViewById(R.id.image)
    }
}
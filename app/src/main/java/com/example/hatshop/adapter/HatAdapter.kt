package com.example.hatshop.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.R
import com.example.hatshop.model.Hat
import com.squareup.picasso.Picasso

class HatAdapter(private val dataset: ArrayList<Hat>, private val listener: (Hat) -> Unit) : RecyclerView.Adapter<HatAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HatAdapter.ItemViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.list_hat_item, parent, false)
        return ItemViewHolder(item)
    }

    override fun onBindViewHolder(holder: HatAdapter.ItemViewHolder, position: Int) {
        val hat : Hat = dataset[position]
        holder.name.text = hat.name
        holder.price.text = hat.price.toString()
        holder.stock.text = hat.stock.toString()
        Picasso.get().load(hat.downloadURL).into(holder.image)
        holder.itemView.setOnClickListener {
            listener(hat)
        }
    }

    override fun getItemCount(): Int {
        Log.d("DZULA", dataset.size.toString())
        return dataset.size
    }

    public class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.name)
        val price : TextView = view.findViewById(R.id.price)
        val stock : TextView = view.findViewById(R.id.stock)
        val image : ImageView = view.findViewById(R.id.image)
    }

}
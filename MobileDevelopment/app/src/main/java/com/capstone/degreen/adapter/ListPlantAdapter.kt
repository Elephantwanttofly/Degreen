package com.capstone.degreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.degreen.R
import com.capstone.degreen.data.DataPlant

class ListPlantAdapter(private val listPlant : ArrayList<DataPlant>) : RecyclerView.Adapter<ListPlantAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.findViewById(R.id.tv_item_name)
        val ivImg : ImageView = itemView.findViewById(R.id.iv_item_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_plant, parent, false)

        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listPlant.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, img) = listPlant[position]

        Glide.with(holder.itemView.context)
            .load(img) // URL Gambar
            .into(holder.ivImg) // imageView mana yang akan diterapkan
        holder.tvName.text = name
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listPlant[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataPlant)
    }

}
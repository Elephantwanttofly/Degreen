package com.capstone.degreen.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.degreen.R
import com.capstone.degreen.data.DataPlant
import com.capstone.degreen.ui.PlantDetailActivity

class ListPlantAdapter(private val listPlant : ArrayList<DataPlant>) : RecyclerView.Adapter<ListPlantAdapter.ListViewHolder>() {

    class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.findViewById(R.id.tv_item_name)
        val ivImg : ImageView = itemView.findViewById(R.id.iv_item_img)
        val tvDesc : TextView = itemView.findViewById(R.id.tv_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_plant, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listPlant.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, img, desc) = listPlant[position]

        Glide.with(holder.itemView.context)
            .load(img) // URL Gambar
            .into(holder.ivImg) // imageView mana yang akan diterapkan
        holder.tvName.text = name
        holder.tvDesc.text = desc
        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, PlantDetailActivity::class.java)
            intentDetail.putExtra(PlantDetailActivity.EXTRA_NAME, name)
            intentDetail.putExtra(PlantDetailActivity.EXTRA_DESC, desc)
            intentDetail.putExtra(PlantDetailActivity.EXTRA_PHOTO, img)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

}
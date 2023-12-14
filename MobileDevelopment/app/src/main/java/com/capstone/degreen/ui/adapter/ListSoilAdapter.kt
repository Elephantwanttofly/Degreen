package com.capstone.degreen.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.degreen.R
import com.capstone.degreen.data.DataSoil
import com.capstone.degreen.ui.PlantDetailActivity
import com.capstone.degreen.ui.SoilDetailActivity

class ListSoilAdapter(private val listSoil : ArrayList<DataSoil>) : RecyclerView.Adapter<ListSoilAdapter.ListViewHolder>() {

    class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.findViewById(R.id.tv_item_name_soil)
        val ivImg : ImageView = itemView.findViewById(R.id.iv_item_img_soil)
        val tvDesc : TextView = itemView.findViewById(R.id.tv_desc_soil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_soil, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listSoil.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (nameSoil, imgSoil, descSoil) = listSoil[position]

        holder.ivImg.setImageResource(imgSoil)
        val photo = imgSoil
        holder.tvName.text = nameSoil
        holder.tvDesc.text = descSoil
        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, SoilDetailActivity::class.java)
            intentDetail.putExtra(SoilDetailActivity.EXTRA_NAME, nameSoil)
            intentDetail.putExtra(SoilDetailActivity.EXTRA_DESC, descSoil)
            intentDetail.putExtra(SoilDetailActivity.EXTRA_PHOTO, photo)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

}
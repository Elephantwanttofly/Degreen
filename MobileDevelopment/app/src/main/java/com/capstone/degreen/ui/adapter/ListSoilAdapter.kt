package com.capstone.degreen.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.degreen.data.model.Constant
import com.capstone.degreen.data.model.DataItem
import com.capstone.degreen.databinding.ItemRowSoilBinding

class ListSoilAdapter(var listsoil: ArrayList<DataItem>, var listener: OnAdapterListenerSoil) :
    RecyclerView.Adapter<ListSoilAdapter.ViewHolder>() {
    private val TAG: String = "ListSoilAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ListSoilAdapter.ViewHolder {
        return ViewHolder(
            ItemRowSoilBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = listsoil.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val soil = listsoil[position]
        holder.bind(soil)
        val urlSoil = Constant.BASE_URL + soil.urlTanah

        Glide.with(holder.itemView.context)
            .load(urlSoil)
            .into(holder.view.ivItemImgSoil)

        holder.view.itemSoil.setOnClickListener {
            listener.onClick(soil)
        }
    }

    class ViewHolder(private val binding: ItemRowSoilBinding): RecyclerView.ViewHolder(binding.root){
        val view = binding
        fun bind(listSoil: DataItem) {
            binding.tvItemNameSoil.text = listSoil.jenis
        }
    }

    fun setData(newSoil: List<DataItem>) {
        listsoil.clear()
        listsoil.addAll(newSoil)
        notifyDataSetChanged()
    }

    interface OnAdapterListenerSoil{
        fun onClick(soil : DataItem)
    }

}
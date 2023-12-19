package com.capstone.degreen.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.degreen.data.model.DataItem
import com.capstone.degreen.databinding.ItemRowSoilBinding

class ListSoilAdapter(var listsoil : ArrayList<DataItem>, var listener : OnAdapterListener) :
    RecyclerView.Adapter<ListSoilAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        return ViewHolder(
            ItemRowSoilBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = listsoil.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val soil = listsoil[position]
        holder.bind(soil)

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

    fun setData(newMovie: List<DataItem>) {
        listsoil.clear()
        listsoil.addAll(newMovie)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(soil : DataItem)
    }


}
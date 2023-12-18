package com.capstone.degreen.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.degreen.data.model.ListSoilData
import com.capstone.degreen.databinding.ItemRowSoilBinding

class ListSoilAdapter(var listsoil : ArrayList<ListSoilData>) :
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

    }

    class ViewHolder(private val binding: ItemRowSoilBinding): RecyclerView.ViewHolder(binding.root){
        val view = binding
        fun bind(listSoil: ListSoilData) {
            binding.tvItemNameSoil.text = listSoil.jenis
        }
    }

    fun setData(newMovie: List<ListSoilData>) {
        listsoil.clear()
        listsoil.addAll(newMovie)
        notifyDataSetChanged()
    }


}
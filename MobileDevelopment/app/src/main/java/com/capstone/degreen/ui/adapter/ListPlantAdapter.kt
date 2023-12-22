package com.capstone.degreen.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.degreen.data.model.Constant
import com.capstone.degreen.data.model.RekomendasiBibitItem
import com.capstone.degreen.databinding.ItemRowPlantBinding

class ListPlantAdapter(var listplant : ArrayList<RekomendasiBibitItem>, var listener : OnAdapterListenerPlant) :
    RecyclerView.Adapter<ListPlantAdapter.ViewHolder>() {
    private val TAG: String = "ListPlantAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ListPlantAdapter.ViewHolder {
        return ViewHolder(
            ItemRowPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListPlantAdapter.ViewHolder, position: Int) {
        val plant = listplant[position]
        holder.bind(plant)
        val urlPlant = Constant.BASE_URL + plant.urlGambar

        Glide.with(holder.itemView.context)
            .load(urlPlant)
            .into(holder.view.ivItemImg)

        holder.view.itemPlant.setOnClickListener {
            listener.onClick(plant)
        }
    }

    override fun getItemCount() = listplant.size


    class ViewHolder(private val binding: ItemRowPlantBinding): RecyclerView.ViewHolder(binding.root){
        val view = binding
        fun bind(listSoil: RekomendasiBibitItem) {
            binding.tvItemName.text = listSoil.nama
        }
    }

    fun setData(newPlant: List<RekomendasiBibitItem>) {
        listplant.clear()
        listplant.addAll(newPlant)
        notifyDataSetChanged()
    }

    interface OnAdapterListenerPlant{
        fun onClick(soil: RekomendasiBibitItem)
    }

}
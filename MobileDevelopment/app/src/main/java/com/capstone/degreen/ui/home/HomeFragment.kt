package com.capstone.degreen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.degreen.R
import com.capstone.degreen.adapter.ListPlantAdapter
import com.capstone.degreen.data.DataPlant
import com.capstone.degreen.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var rvPlant : RecyclerView
    private val list = ArrayList<DataPlant>()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPlant = requireView().findViewById(R.id.rv_plant)
        rvPlant.setHasFixedSize(true)

        list.addAll(listPlants())
        showRecyclerList()
    }
    private fun listPlants() : ArrayList<DataPlant>{
            val dataName = resources.getStringArray(R.array.data_name)
            val dataPhoto = resources.getStringArray(R.array.data_photo)
            val dataDescription = resources.getStringArray(R.array.data_description)

            val listPlant = ArrayList<DataPlant>()
            for (i in dataName.indices){
                val plant = DataPlant(dataName[i], dataPhoto[i], dataDescription[i])
                listPlant.add(plant)
            }
            return listPlant
        }

    private fun showRecyclerList() {
        rvPlant.layoutManager = LinearLayoutManager(requireActivity())
        val listPlantAdapter = ListPlantAdapter(list)
        rvPlant.adapter = listPlantAdapter

        listPlantAdapter.setOnItemClickCallback(object : ListPlantAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataPlant) {

            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
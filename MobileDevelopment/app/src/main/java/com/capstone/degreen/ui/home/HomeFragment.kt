package com.capstone.degreen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.degreen.R
import com.capstone.degreen.data.DataPlant
import com.capstone.degreen.databinding.FragmentHomeBinding
import com.capstone.degreen.ui.adapter.ListPlantAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvplant : RecyclerView
    private val list = ArrayList<DataPlant>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvplant = binding.rvPlant
        rvplant.setHasFixedSize(true)

        list.addAll(getlistPlants())
        showRecyclerList()
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
        }
    }


    private fun getlistPlants() : ArrayList<DataPlant>{
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
        rvplant.layoutManager = GridLayoutManager(requireActivity(),2)
        val listPlantAdapter = ListPlantAdapter(list)
        rvplant.adapter = listPlantAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
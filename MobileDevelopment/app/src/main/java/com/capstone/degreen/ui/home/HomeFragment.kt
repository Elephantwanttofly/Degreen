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
import com.capstone.degreen.data.DataSoil
import com.capstone.degreen.databinding.FragmentHomeBinding
import com.capstone.degreen.ui.adapter.ListSoilAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvsoil : RecyclerView
    private val list = ArrayList<DataSoil>()
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

        rvsoil = binding.rvSoil
        rvsoil.setHasFixedSize(true)

        list.addAll(getlistSoil())
        showRecyclerList()
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
        }
    }


    private fun getlistSoil() : ArrayList<DataSoil>{
        val dataName = resources.getStringArray(R.array.data_name_soil)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo_soil)
        val dataDescription = resources.getStringArray(R.array.data_description_plant)

        val listSoil = ArrayList<DataSoil>()
        for (i in dataName.indices){
            val soil = DataSoil(dataName[i], dataPhoto.getResourceId(i,-1), dataDescription[i])
            listSoil.add(soil)
        }
        return listSoil
    }

    private fun showRecyclerList() {
        rvsoil.layoutManager = LinearLayoutManager(requireActivity())
        val listPlantAdapter = ListSoilAdapter(list)
        rvsoil.adapter = listPlantAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.capstone.degreen.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.degreen.data.DataSoil
import com.capstone.degreen.data.model.DataItem
import com.capstone.degreen.data.model.ListSoilResponse
import com.capstone.degreen.data.retrofit.ApiConfig
import com.capstone.degreen.databinding.FragmentHomeBinding
import com.capstone.degreen.ui.MainActivity
import com.capstone.degreen.ui.SoilDetailActivity
import com.capstone.degreen.ui.adapter.ListSoilAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvsoil : RecyclerView
    private lateinit var listSoilAdapter: ListSoilAdapter
    private val list = ArrayList<DataSoil>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvsoil = binding.rvSoil
        rvsoil.setHasFixedSize(true)
        
        showRecyclerList()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
        }
    }

    override fun onStart() {
        super.onStart()
        getSoil()
    }

    private fun showRecyclerList() {
        listSoilAdapter = ListSoilAdapter(arrayListOf(), object : ListSoilAdapter.OnAdapterListener{
            override fun onClick(soil: DataItem) {
                SOIL_ID = soil.id!!

                val intent = Intent(context, SoilDetailActivity::class.java)
                startActivity(intent)
            }

        })
        rvsoil.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = listSoilAdapter
        }
    }

    fun getSoil(){
        showLoading(true)
        ApiConfig.getApiService().getSoil()
            .enqueue(object : Callback<ListSoilResponse> {
                override fun onResponse(
                    call: Call<ListSoilResponse>,
                    response: Response<ListSoilResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        showData(response.body()!!)
                    }
                }
                override fun onFailure(call: Call<ListSoilResponse>, t: Throwable) {
                    showLoading(false)
                    Log.d(MainActivity.TAG, "errorbro : ${t.toString()}")
                }

            })
    }

    private fun showData(response: ListSoilResponse) {
        listSoilAdapter.setData(response.data)
    }

    private fun showLoading(loading: Boolean) {
        when(loading) {
            true -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            false -> {
                binding.progressBar.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        var SOIL_ID: String = "soil_id"
    }
}
package com.capstone.degreen.ui.home

import android.content.Context
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
import com.capstone.degreen.data.model.Constant
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
    private val TAG: String = "ListSoilAdapter"
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

        // Mengambil nama pengguna dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("preferensi aplikasi", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("user_name", "GUEST")


        // Tampilkan nama pengguna di TextView atau elemen UI lainnya
        val textViewUsername = binding.nameHome
        textViewUsername.text = username

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvsoil = binding.rvSoil
        rvsoil.setHasFixedSize(true)
        
        showRecyclerList()
        getSoil()
    }

    private fun showRecyclerList() {
        listSoilAdapter = ListSoilAdapter(arrayListOf(), object : ListSoilAdapter.OnAdapterListenerSoil{
            override fun onClick(soil: DataItem) {
                Constant.SOIL_ID = soil.id!!

                val intent = Intent(context, SoilDetailActivity::class.java)
                startActivity(intent)
            }

        })
        rvsoil.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = listSoilAdapter
        }
    }

    private fun getSoil(){
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
                    binding.warning.visibility = View.VISIBLE
                }

            })
    }

    private fun showData(response: ListSoilResponse) {
        listSoilAdapter.setData(response.data)
        Log.d(TAG, "Ambilsemuadata : ${response.data}")
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
}
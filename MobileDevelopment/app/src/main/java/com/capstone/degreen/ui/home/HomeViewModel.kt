//package com.capstone.degreen.ui.home
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.capstone.degreen.data.model.ListSoilData
//
//class HomeViewModel : ViewModel() {
//
//    private val _data = MutableLiveData<List<ListSoilData>?>()
//    val data: LiveData<List<ListSoilData>?> = _data
//
//    private val _loading= MutableLiveData<Boolean>()
//    val loading: LiveData<Boolean> = _loading
//
//    private val _empty = MutableLiveData<Boolean>()
//    val empty: LiveData<Boolean> = _empty
//
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text
//}
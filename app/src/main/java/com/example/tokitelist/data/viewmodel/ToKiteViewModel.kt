package com.example.tokitelist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tokitelist.data.ToKiteDB
import com.example.tokitelist.data.models.KiteItem
import com.example.tokitelist.data.repository.ToKiteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ToKiteViewModel(application: Application):AndroidViewModel(application) {

    private val toKiteDao = ToKiteDB.getDataBase(application).toKiteDao()

    private val repository: ToKiteRepository

    val getAllData: LiveData<List<KiteItem>>

    init {
        repository = ToKiteRepository(toKiteDao)
        getAllData = repository.getAllData
    }

    fun insertData(kiteItem: KiteItem){
        viewModelScope.launch(Dispatchers.IO) { repository.insertData(kiteItem) }
    }

    fun getKitemByName(name:String): Job {
        return viewModelScope.launch(Dispatchers.IO) { repository.getKitemByName(name) }
    }

    fun deleteData(name: String) {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteData(name) }
    }
}
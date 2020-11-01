package com.nk.tokitelist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nk.tokitelist.data.ToKiteDB
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.repository.ToKiteRepository
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

    fun checkKitem(kitem: KiteItem) {
        viewModelScope.launch(Dispatchers.IO) { repository.checkKitem(kitem) }
    }

    fun restart() {
        viewModelScope.launch(Dispatchers.IO) { repository.uncheckAllKitems() }
    }

    fun uncheckKitem(kitemToUncheck: KiteItem) {
        viewModelScope.launch(Dispatchers.IO) { repository.uncheckKitem(kitemToUncheck) }
    }
}
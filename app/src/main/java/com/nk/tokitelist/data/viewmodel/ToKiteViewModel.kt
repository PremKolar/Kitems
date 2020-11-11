package com.nk.tokitelist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nk.tokitelist.data.ToKiteDB
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.KiteSession
import com.nk.tokitelist.data.models.Spot
import com.nk.tokitelist.data.repository.ToKiteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToKiteViewModel(application: Application):AndroidViewModel(application) {

    private val toKiteDao = ToKiteDB.getDataBase(application).toKiteDao()

    private val repository: ToKiteRepository

    val getAllData: LiveData<List<KiteItem>>

    val getAllSpotNames: LiveData<Array<String>>

    val getAllSession: LiveData<Array<KiteSession>>



    init {
        repository = ToKiteRepository(toKiteDao)
        getAllData = repository.getAllData
        getAllSpotNames = repository.getAllSpotNames
        getAllSession = repository.getAllSessions
    }

    fun insertKitem(kiteItem: KiteItem){
        viewModelScope.launch(Dispatchers.IO) { repository.insertData(kiteItem) }
    }

    fun deleteKitem(name: String) {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteKitem(name) }
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

    fun insertKiteSession(kiteSession: KiteSession){
        viewModelScope.launch(Dispatchers.IO) { repository.insertKiteSession(kiteSession) }
    }


    fun updateKiteSession(kiteSession: KiteSession) {
        viewModelScope.launch(Dispatchers.IO) { repository.updateKiteSession(kiteSession) }
    }


    fun addNewSpot(name: String) {
        val spot = Spot(name)
        viewModelScope.launch(Dispatchers.IO) {  repository.insertNewSpot(spot)}
    }

   suspend fun getSpotByName(spotName: String): Spot? {
        return  repository.getSpotByName(spotName)
    }

    fun deleteSpot(spot: String) {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteSpot(spot) }
    }

    fun deleteSession(session: KiteSession) {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteSession(session) }
    }

}
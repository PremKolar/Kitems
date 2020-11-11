package com.nk.tokitelist.data.repository

import androidx.lifecycle.LiveData
import com.nk.tokitelist.data.ToKiteDao
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.KiteSession
import com.nk.tokitelist.data.models.Spot

class ToKiteRepository(private val toKiteDao: ToKiteDao) {
    val getAllSessions: LiveData<Array<KiteSession>> = toKiteDao.getAllSessions()
    val getAllData: LiveData<List<KiteItem>> = toKiteDao.getAllData()
    val getAllSpotNames: LiveData<Array<String>> = toKiteDao.getAllSpotNames()


    suspend fun insertData(kiteItem: KiteItem){
        kiteItem.checked = false
        kiteItem.addedIdx = getMaxIndex()+1
        toKiteDao.insertData(kiteItem)
    }

    private fun getMaxIndex(): Int {
        var m = toKiteDao.getMaxIndex()
        if (m==null){
            m = 1
        }
        return m
    }

    fun getKitemByName(name: String): KiteItem? {
        return toKiteDao.getKitemByName(name)
    }

    fun deleteKitem(name: String) {
        toKiteDao.deleteData(name)
    }

    fun checkKitem(kitem: KiteItem) {
        toKiteDao.checkKitem(kitem.name)
    }

    fun uncheckKitem(kitem: KiteItem) {
        toKiteDao.uncheckKitem(kitem.name)
    }

    fun uncheckAllKitems() {
        toKiteDao.uncheckAllKitems()
    }

    fun insertKiteSession(kiteSession: KiteSession) {
        toKiteDao.insertKiteSession(kiteSession)
    }

    fun updateKiteSession(kiteSession: KiteSession) {
        toKiteDao.updateKiteSession(kiteSession)
    }

    fun insertNewSpot(spot: Spot) {
        toKiteDao.insertNewSpot(spot)
    }

    suspend fun getSpotByName(spotName: String):Spot? {
        return toKiteDao.getSpotByName(spotName)
    }

    suspend fun getNumberOfSpots(): Int {
        return toKiteDao.getNumberOfSpots()
    }

    fun deleteSpot(spot: String) {
        toKiteDao.deleteSpot(spot)
    }

    fun deleteSession(session: KiteSession) {
        toKiteDao.deleteSession(session.id)
    }


}
package com.example.tokitelist.data.repository

import androidx.lifecycle.LiveData
import com.example.tokitelist.data.ToKiteDao
import com.example.tokitelist.data.models.KiteItem

class ToKiteRepository(private val toKiteDao: ToKiteDao) {
    val getAllData: LiveData<List<KiteItem>> = toKiteDao.getAllData()

    suspend fun insertData(kiteItem: KiteItem){
        kiteItem.checked = false
        toKiteDao.insertData(kiteItem)
    }

    fun getKitemByName(name: String): KiteItem? {
        return toKiteDao.getKitemByName(name)
    }

    fun deleteData(name: String) {
                toKiteDao.deleteData(name)
    }

    fun checkKitem(kitem: KiteItem) {
        toKiteDao.checkKitem(kitem.name)
    }

    fun uncheckAllKitems() {
        toKiteDao.uncheckAllKitems()
    }
}
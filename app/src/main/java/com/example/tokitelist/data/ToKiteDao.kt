package com.example.tokitelist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tokitelist.data.models.KiteItem

@Dao
interface ToKiteDao {

    @Query("SELECT * FROM kite_items_table ORDER BY name ASC")
    fun getAllData(): LiveData<List<KiteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(kiteItem: KiteItem)

    @Query("SELECT * FROM kite_items_table WHERE name = :name")
    fun getKitemByName(name: String):KiteItem?

    @Query("DELETE FROM kite_items_table WHERE name = :name")
    fun deleteData(name: String)
}
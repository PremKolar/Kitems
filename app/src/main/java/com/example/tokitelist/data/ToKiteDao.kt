package com.example.tokitelist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tokitelist.data.models.KiteItem

@Dao
interface ToKiteDao {

    @Query("SELECT * FROM kite_items_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<KiteItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(kiteItem: KiteItem)
}
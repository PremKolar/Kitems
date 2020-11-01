package com.nk.tokitelist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nk.tokitelist.data.models.KiteItem

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

    @Query("UPDATE kite_items_table SET checked=1 WHERE name = :name")
    fun checkKitem(name: String)

    @Query("UPDATE kite_items_table SET checked=0 WHERE name = :name")
    fun uncheckKitem(name: String)

    @Query("UPDATE kite_items_table SET checked=0")
    fun uncheckAllKitems()

    @Query("SELECT MAX(addedIdx) FROM kite_items_table")
    fun getMaxIndex(): Int


}
package com.nk.tokitelist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.KiteSession
import com.nk.tokitelist.data.models.Spot
import java.util.*

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKiteSession(kiteSession: KiteSession)

    @Query("UPDATE sessions_table SET date = :date, spot=:spot WHERE id = :id ")
    fun updateKiteSession(id:Int, date: Date, spot: Spot)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateKiteSession(session: KiteSession)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewSpot(spot: Spot)

    @Query("SELECT * FROM spots_table WHERE name = :spotName")
   suspend fun getSpotByName(spotName: String): Spot?

    @Query("SELECT COUNT(name) FROM spots_table")
    suspend fun getNumberOfSpots(): Int

    @Query("SELECT name FROM spots_table")
    fun getAllSpotNames(): LiveData<Array<String>>

    @Query("DELETE FROM spots_table WHERE name = :spot")
    fun deleteSpot(spot: String)

    @Query("SELECT * FROM sessions_table ORDER BY date DESC")
    fun getAllSessions(): LiveData<Array<KiteSession>>

    @Query("DELETE FROM sessions_table WHERE id = :id")
    fun deleteSession(id: Int)


}
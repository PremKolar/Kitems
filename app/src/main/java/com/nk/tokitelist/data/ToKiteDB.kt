package com.nk.tokitelist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nk.tokitelist.data.models.KiteItem
import com.nk.tokitelist.data.models.KiteSession
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.models.Spot
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


@Database(entities = [KiteItem::class, KiteSession::class, Spot::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToKiteDB: RoomDatabase() {
    abstract fun toKiteDao():ToKiteDao

    companion object{

        @Volatile
        private var INSTANCE: ToKiteDB? = null

        fun getDataBase(context: Context): ToKiteDB{



            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){

                fun addKitem(name:String,season: Season,idx:Int):Int{
                    GlobalScope.async {getDataBase(context).toKiteDao().insertData(KiteItem(name,season,idx,false))  }
                    return (idx+1)
                }

                val rdc: Callback = object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        var idx = 1
//                        idx = addKitem("Kite small",Season.always,idx)
//                        idx = addKitem("Kite large",Season.always,idx)
//                        idx = addKitem("Harness",Season.always,idx)
//                        idx = addKitem("Bar",Season.always,idx)
//                        idx = addKitem("Leash",Season.always,idx)
//                        idx = addKitem("Pump",Season.always,idx)
//                        idx = addKitem("Kiteboard",Season.always,idx)
//                        idx = addKitem("Water",Season.always,idx)
//                        idx = addKitem("Food",Season.always,idx)
//                        idx = addKitem("Cash",Season.always,idx)
//                        idx = addKitem("Towel",Season.always,idx)
//                        idx = addKitem("Sunscreen",Season.summer,idx)
//                        idx = addKitem("Surf Shorts",Season.summer,idx)
//                        idx = addKitem("Hood",Season.winter,idx)
//                        idx = addKitem("Booties",Season.winter,idx)
                        idx = addKitem("Gloves",Season.winter,idx)

                    }

//                override fun onOpen(db: SupportSQLiteDatabase) {
//                    // do something every time database is open
//                }
                }


                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ToKiteDB::class.java,
                        "kite_items_table"
                ).addCallback(rdc).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance
            }

        }

    }
}
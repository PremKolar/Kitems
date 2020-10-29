package com.example.tokitelist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tokitelist.data.models.KiteItem
import com.example.tokitelist.data.models.Season
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


@Database(entities = [KiteItem::class], version = 1, exportSchema = false)
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

                fun addKitem(name:String,season: Season){
                    GlobalScope.async {getDataBase(context).toKiteDao().insertData(KiteItem(name,season,false))  }
                }

                val rdc: Callback = object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        addKitem("Kite small",Season.always)
                        addKitem("Kite large",Season.always)
                        addKitem("Harness",Season.always)
                        addKitem("Bar",Season.always)
                        addKitem("Leash",Season.always)
                        addKitem("Pump",Season.always)
                        addKitem("Kiteboard",Season.always)
                        addKitem("Water",Season.always)
                        addKitem("Food",Season.always)
                        addKitem("Cash",Season.always)
                        addKitem("Sunscreen",Season.summer)
                        addKitem("Hood",Season.winter)
                        addKitem("Booties",Season.winter)
                        addKitem("Gloves",Season.winter)

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
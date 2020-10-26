package com.example.tokitelist.data

import android.content.Context
import androidx.room.*
import com.example.tokitelist.data.models.KiteItem

@Database(entities = [KiteItem::class],version = 1,exportSchema = false)
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
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ToKiteDB::class.java,
                        "kite_items_table"
                ).build()
                INSTANCE = instance
                return instance
            }


        }

    }
}
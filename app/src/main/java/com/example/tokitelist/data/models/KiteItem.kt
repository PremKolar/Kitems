package com.example.tokitelist.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kite_items_table")
data class KiteItem (
        @PrimaryKey(autoGenerate = true)
        var id:Int,
        var name:String,
        var season: Season
)
package com.example.tokitelist.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "kite_items_table")
@Parcelize
data class KiteItem (
        @PrimaryKey()
        var name:String,
        var season: Season
): Parcelable
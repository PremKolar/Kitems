package com.nk.tokitelist.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "spots_table")
@Parcelize
data class Spot(
        @PrimaryKey()
        var name:String
): Parcelable
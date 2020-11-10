package com.nk.tokitelist.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "sessions_table")
@Parcelize
data class KiteSession(
        @PrimaryKey(autoGenerate = true)
        var id:Int,
        var date: Date,
        var spot: Spot
): Parcelable
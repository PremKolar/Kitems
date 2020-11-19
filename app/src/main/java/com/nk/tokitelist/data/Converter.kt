package com.nk.tokitelist.data

import androidx.room.TypeConverter
import com.nk.tokitelist.data.models.Rating
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.models.Spot
import java.util.*

class Converter {

    @TypeConverter
    fun fromSeason(season: Season):String{
        return season.name
    }

    @TypeConverter
    fun toSeason(season:String): Season {
        return Season.valueOf(season)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromSpot(spot: Spot?): String? {
        return spot?.name
    }

    @TypeConverter
    fun toSpot(s: String?): Spot? {
        return s?.let { Spot(name = it) }
    }

    @TypeConverter
    fun fromRating(r: Rating?): Int {
        if (r != null) {
            return (r.ordinal)
        }
        return 0
    }

    @TypeConverter
    fun toRating(i: Int?): Rating? {
        return i?.let { Rating.fromInt(it) }
    }



}
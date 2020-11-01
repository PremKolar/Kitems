package com.nk.tokitelist.data

import androidx.room.TypeConverter
import com.nk.tokitelist.data.models.Season
import com.nk.tokitelist.data.models.SortMode

class Converter {

    @TypeConverter
    fun fromSeason(season: Season):String{
        return season.name
    }

    @TypeConverter
    fun toSeason(season:String): Season {
        return Season.valueOf(season)
    }

}
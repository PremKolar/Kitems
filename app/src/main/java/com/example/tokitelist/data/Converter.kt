package com.example.tokitelist.data

import androidx.room.TypeConverter
import com.example.tokitelist.data.models.Season

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
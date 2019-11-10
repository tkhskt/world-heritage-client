package com.github.gericass.world_heritage_client.data.local

import androidx.room.TypeConverter
import java.util.*


object DateConverter {
    @TypeConverter
    @JvmStatic
    fun fromTimeToDate(time: Long?): Date? {
        return if (time == null) null else Date(time)
    }

    @TypeConverter
    @JvmStatic
    fun fromDateToTime(date: Date?): Long? {
        return date?.time
    }
}
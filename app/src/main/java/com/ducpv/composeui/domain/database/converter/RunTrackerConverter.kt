package com.ducpv.composeui.domain.database.converter

import androidx.room.TypeConverter
import com.ducpv.composeui.domain.database.entity.PointEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by pvduc9773 on 12/05/2023.
 */
class RunTrackerConverter {
    @TypeConverter
    fun fromStringToPoint(pointString: String?): List<PointEntity>? {
        return try {
            val pointsType = object : TypeToken<List<PointEntity>>() {}.type
            Gson().fromJson<List<PointEntity>>(pointString, pointsType)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun fromPointsToString(points: List<PointEntity>?): String? {
        return Gson().toJson(points)
    }
}

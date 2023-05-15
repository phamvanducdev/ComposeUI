package com.ducpv.composeui.domain.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by pvduc9773 on 12/05/2023.
 */

@Entity(tableName = "run_tracker")
data class RunTrackerEntity(
    val startTime: Long,
    val endTime: Long,
    val runTime: Long,
    val points: List<PointEntity>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}

data class PointEntity(
    val latitude: Double,
    val longitude: Double
)

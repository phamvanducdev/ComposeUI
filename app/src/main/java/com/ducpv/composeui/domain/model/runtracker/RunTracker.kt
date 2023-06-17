package com.ducpv.composeui.domain.model

import com.ducpv.composeui.domain.database.entity.PointEntity
import com.ducpv.composeui.domain.database.entity.RunTrackerEntity
import com.google.android.gms.maps.model.LatLng

/**
 * Created by pvduc9773 on 15/05/2023.
 */
data class RunTracker(
    val startTime: Long,
    val endTime: Long,
    val runTime: Long,
    val points: List<Point>
)

data class Point(
    val latitude: Double,
    val longitude: Double
)

fun Point.toLocation(): LatLng {
    return LatLng(
        this.latitude,
        this.longitude,
    )
}

fun PointEntity.toPoint(): Point {
    return Point(
        latitude = this.latitude,
        longitude = this.longitude,
    )
}

fun RunTrackerEntity.toRunTracker(): RunTracker {
    return RunTracker(
        startTime = this.startTime,
        endTime = this.endTime,
        runTime = this.runTime,
        points = this.points.map { it.toPoint() },
    )
}

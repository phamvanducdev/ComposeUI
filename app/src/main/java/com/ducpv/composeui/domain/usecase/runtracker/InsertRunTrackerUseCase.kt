package com.ducpv.composeui.domain.usecase.runtracker

import com.ducpv.composeui.domain.database.entity.PointEntity
import com.ducpv.composeui.domain.database.entity.RunTrackerEntity
import com.ducpv.composeui.domain.repository.RunTrackerRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

/**
 * Created by pvduc9773 on 10/06/2023.
 */
class InsertRunTrackerUseCase @Inject constructor(
    private val runTrackerRepository: RunTrackerRepository
) {
    suspend operator fun invoke(
        startTime: Long,
        runTime: Long,
        points: List<LatLng>
    ) {
        runTrackerRepository.insertRunTracker(
            RunTrackerEntity(
                startTime = startTime,
                endTime = System.currentTimeMillis(),
                runTime = runTime,
                points = points.map {
                    PointEntity(
                        latitude = it.latitude,
                        longitude = it.longitude,
                    )
                },
            ),
        )
    }
}

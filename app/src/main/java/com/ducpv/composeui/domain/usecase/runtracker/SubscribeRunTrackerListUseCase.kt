package com.ducpv.composeui.domain.usecase.runtracker

import com.ducpv.composeui.domain.model.RunTracker
import com.ducpv.composeui.domain.repository.RunTrackerRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Created by pvduc9773 on 15/05/2023.
 */
class SubscribeRunTrackerListUseCase @Inject constructor(
    private val runTrackerRepository: RunTrackerRepository
) {
    operator fun invoke(): Flow<List<RunTracker>> = runTrackerRepository.runTrackers
}

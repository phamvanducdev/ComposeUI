package com.ducpv.composeui.domain.repository

import com.ducpv.composeui.domain.database.dao.RunTrackerDao
import com.ducpv.composeui.domain.model.RunTracker
import com.ducpv.composeui.domain.model.toRunTracker
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 15/05/2023.
 */
interface RunTrackerRepository {
    val runTrackers: Flow<List<RunTracker>>
}

class RunTrackerRepositoryImpl @Inject constructor(
    private val runTrackerDao: RunTrackerDao
) : RunTrackerRepository {
    override val runTrackers = MutableStateFlow<List<RunTracker>>(emptyList())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            runTrackerDao.getRunTrackers().collect { runTrackerEntityList ->
                runTrackers.value = runTrackerEntityList.map { it.toRunTracker() }
            }
        }
    }
}

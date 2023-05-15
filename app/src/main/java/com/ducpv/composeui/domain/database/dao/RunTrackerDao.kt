package com.ducpv.composeui.domain.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ducpv.composeui.domain.database.entity.RunTrackerEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by pvduc9773 on 12/05/2023.
 */

@Dao
interface RunTrackerDao {
    @Query(value = "SELECT * FROM run_tracker")
    fun getRunTrackers(): Flow<List<RunTrackerEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRunTracker(entity: RunTrackerEntity)
}

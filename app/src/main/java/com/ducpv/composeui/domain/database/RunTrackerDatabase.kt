package com.ducpv.composeui.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ducpv.composeui.domain.database.converter.RunTrackerConverter
import com.ducpv.composeui.domain.database.dao.RunTrackerDao
import com.ducpv.composeui.domain.database.entity.RunTrackerEntity

/**
 * Created by pvduc9773 on 11/05/2023.
 */
@Database(
    entities = [
        RunTrackerEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    RunTrackerConverter::class,
)
abstract class RunTrackerDatabase : RoomDatabase() {
    abstract fun runTrackerDao(): RunTrackerDao

    companion object {
        private const val databaseName = "run-tracker-db"

        fun buildDatabase(context: Context): RunTrackerDatabase {
            return Room.databaseBuilder(context, RunTrackerDatabase::class.java, databaseName).build()
        }
    }
}

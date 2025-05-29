package com.example.test1clock.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): LiveData<List<AlarmEntity>>

    @Insert
    fun insertAlarm(alarm: AlarmEntity)

    @Update
    fun updateAlarm(alarm: AlarmEntity)

    @Delete
    fun deleteAlarm(alarm: AlarmEntity)

    @Query("SELECT * FROM alarms WHERE id = :id LIMIT 1")
    fun getAlarmById(id: Int): AlarmEntity?
}

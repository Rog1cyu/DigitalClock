package com.example.test1clock.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: String,
    val date: String,
    val repeat: String,
    var enabled: Boolean
)
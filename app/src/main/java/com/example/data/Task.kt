package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val course: String,
    val deadlineDate: String, // e.g. "12 Okt 2023" or "dd/mm/yyyy"
    val deadlineTime: String = "", // e.g. "23:59"
    val priority: String, // "Low", "Mid", "High"
    val notes: String = "",
    val isCompleted: Boolean = false
)

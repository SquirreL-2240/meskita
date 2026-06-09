package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Task
import com.example.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TaskRepository(database.taskDao())

        // Prepopulate database if empty with custom tasks from the design screens
        viewModelScope.launch {
            if (repository.getCount() == 0) {
                val presets = listOf(
                    Task(
                        title = "Laporan Probabilitas Bab 4",
                        course = "Matematika Diskrit",
                        deadlineDate = "12 Okt 2023",
                        deadlineTime = "23:59 WIB",
                        priority = "High",
                        notes = "Probabilitas Bayes dan distribusi diskrit.",
                        isCompleted = false
                    ),
                    Task(
                        title = "Implementasi Binary Search Tree",
                        course = "Algoritma & Struktur Data",
                        deadlineDate = "13 Okt 2023",
                        deadlineTime = "23:59",
                        priority = "Mid",
                        notes = "Tuliskan implementasi kode traversal pre-order, in-order, post-order.",
                        isCompleted = false
                    ),
                    Task(
                        title = "Essay: Digital Transformation",
                        course = "Bahasa Inggris",
                        deadlineDate = "10 Okt 2023",
                        deadlineTime = "23:59",
                        priority = "Low",
                        notes = "Analisis dampak AI terhadap karir masa depan.",
                        isCompleted = true
                    ),
                    Task(
                        title = "High-Fidelity Prototyping",
                        course = "UI/UX Design",
                        deadlineDate = "24 Okt 2023",
                        deadlineTime = "23:59",
                        priority = "High",
                        notes = "Buat wireframe interaktif lengkap di Figma.",
                        isCompleted = false
                    ),
                    Task(
                        title = "Usability Testing Report",
                        course = "UI/UX Design",
                        deadlineDate = "30 Okt 2023",
                        deadlineTime = "12:00",
                        priority = "Mid",
                        notes = "Lakukan pengujian usability kepada 5 responden utama.",
                        isCompleted = false
                    ),
                    Task(
                        title = "Calculus III Problem Set",
                        course = "Advanced Mathematics",
                        deadlineDate = "25 Okt 2023",
                        deadlineTime = "08:00",
                        priority = "High",
                        notes = "Selesaikan kalkulus multivariabel dan integral lipat.",
                        isCompleted = false
                    ),
                    Task(
                        title = "Cisco Packet Tracer Lab 04",
                        course = "Computer Networks",
                        deadlineDate = "02 Nov 2023",
                        deadlineTime = "17:00",
                        priority = "Low",
                        notes = "Konfigurasi routing statis dan dinamis OSPF.",
                        isCompleted = false
                    )
                )
                repository.insertAll(presets)
            }
        }
    }

    val tasksState: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(title: String, course: String, deadlineDate: String, deadlineTime: String, priority: String, notes: String) {
        viewModelScope.launch {
            repository.insert(
                Task(
                    title = title,
                    course = course,
                    deadlineDate = deadlineDate,
                    deadlineTime = if (deadlineTime.isEmpty()) "23:59" else deadlineTime,
                    priority = priority,
                    notes = notes,
                    isCompleted = false
                )
            )
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            repository.update(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteById(task.id)
        }
    }
}

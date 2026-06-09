package com.example.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun insertAll(tasks: List<Task>) {
        taskDao.insertTasks(tasks)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteById(id: Int) {
        taskDao.deleteById(id)
    }

    suspend fun getCount(): Int {
        return taskDao.getCount()
    }
}

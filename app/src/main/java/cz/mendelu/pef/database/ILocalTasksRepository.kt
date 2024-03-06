package cz.mendelu.pef.database

import cz.mendelu.pef.model.Task
import kotlinx.coroutines.flow.Flow

interface ILocalTasksRepository {
    fun getAll(): Flow<List<Task>>

    suspend fun insert(task: Task) : Long

}
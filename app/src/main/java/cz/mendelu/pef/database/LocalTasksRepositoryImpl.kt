package cz.mendelu.pef.database

import cz.mendelu.pef.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalTasksRepositoryImpl @Inject constructor(private val dao: TasksDao) : ILocalTasksRepository{
    override fun getAll(): Flow<List<Task>> {
        return dao.getAll()
    }

    override suspend fun insert(task: Task): Long {
        return dao.insert(task)
    }

}
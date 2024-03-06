package cz.mendelu.pef.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cz.mendelu.pef.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<Task>>

    @Insert
    suspend fun insert(task: Task) : Long

}
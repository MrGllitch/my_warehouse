package eu.glitch.mywarehouse.database.type

import eu.glitch.mywarehouse.model.Type
import kotlinx.coroutines.flow.Flow


interface ILocalTypeRepository {
    fun getTypes(): Flow<List<Type>>

    suspend fun insertType(type: Type): Long

    suspend fun deleteType(type: Type)

    suspend fun updateType(type: Type)

    suspend fun getType(id: Long): Type

}
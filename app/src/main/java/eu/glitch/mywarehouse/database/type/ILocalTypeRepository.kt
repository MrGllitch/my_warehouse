package eu.glitch.mywarehouse.database.type

import eu.glitch.mywarehouse.model.Type
import kotlinx.coroutines.flow.Flow


interface ILocalTypeRepository {
    fun getTypes(): Flow<Type>

    suspend fun insertType(type: Type) : Long

}
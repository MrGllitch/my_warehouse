package eu.glitch.mywarehouse.database.type

import eu.glitch.mywarehouse.model.Type
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalTypeRepositoryImpl @Inject constructor(private val typeDao: TypeDao) :
    ILocalTypeRepository {
    override fun getTypes(): Flow<Type> {
        return typeDao.getAllTypes()
    }

    override suspend fun insertType(type: Type): Long {
        return typeDao.insertType(type = type)
    }


}
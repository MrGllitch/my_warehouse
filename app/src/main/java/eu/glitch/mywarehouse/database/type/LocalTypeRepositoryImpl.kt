package eu.glitch.mywarehouse.database.type

import eu.glitch.mywarehouse.model.Type
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalTypeRepositoryImpl @Inject constructor(private val typeDao: TypeDao) :
    ILocalTypeRepository {
    override fun getTypes(): Flow<List<Type>> {
        return typeDao.getAllTypes()
    }

    override suspend fun insertType(type: Type): Long {
        return typeDao.insertType(type = type)
    }

    override suspend fun deleteType(type: Type) {
        typeDao.deleteType(type)
    }

    override suspend fun updateType(type: Type) {
        typeDao.updateType(type)
    }

    override suspend fun getType(id: Long): Type {
        return typeDao.getType(id)
    }


}
package dao

import api.LocalBase
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import models.Operation

class OperationDAO(private val connectionSource: ConnectionSource) {
    private val modelClass = Operation::class.java
    private var dao = DaoManager.createDao(connectionSource, modelClass)

    init {
        TableUtils.createTableIfNotExists(connectionSource, modelClass)
    }

    fun save(operation: Operation) {
        dao.create(operation)
        operation.positions.forEach {
            it.setOperation(operation)
            LocalBase.positionDao.save(it)
        }
    }

    fun load() = dao.queryForAll().map { it.setPositions(LocalBase.positionDao.getFromOperation(it)) }

    fun clear() {
        LocalBase.positionDao.clear()
        TableUtils.clearTable(connectionSource, modelClass)
    }
}
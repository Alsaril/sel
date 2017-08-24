package dao

import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import models.Operation
import models.Position

class PositionDAO(private val connectionSource: ConnectionSource) {
    private val modelClass = Position::class.java
    private var dao = DaoManager.createDao(connectionSource, modelClass)

    init {
        TableUtils.createTableIfNotExists(connectionSource, modelClass)
    }

    fun save(position: Position) = dao.create(position)

    fun getFromOperation(operation: Operation) = dao.queryForEq("operation", operation.id)

    fun clear() = TableUtils.clearTable(connectionSource, modelClass)
}

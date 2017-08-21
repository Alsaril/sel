package dao

import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import models.Operation
import models.Position

class PositionDAO(private val connectionSource: ConnectionSource) {
    private var dao = DaoManager.createDao(connectionSource, Position::class.java)

    init {
        TableUtils.createTableIfNotExists(connectionSource, Position::class.java)
    }

    fun save(position: Position) = dao.create(position)

    fun getFromOperation(operation: Operation) = dao.queryForEq("operation", operation.id)

    fun clear() = TableUtils.clearTable(connectionSource, Position::class.java)
}
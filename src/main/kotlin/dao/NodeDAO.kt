package dao

import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import models.Node


class NodeDAO(private val connectionSource: ConnectionSource) {
    private val modelClass = Node::class.java
    private val dao = DaoManager.createDao(connectionSource, modelClass)

    init {
        TableUtils.createTableIfNotExists(connectionSource, modelClass)
    }

    fun saveList(nodes: List<Node>) {
        TableUtils.clearTable(connectionSource, modelClass)
        nodes.forEach { dao.createOrUpdate(it) }
    }

    fun loadList() = dao.queryForAll()
}
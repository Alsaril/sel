package dao

import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import models.Category

class CategoryDAO(private val connectionSource: ConnectionSource) {
    private var dao = DaoManager.createDao(connectionSource, Category::class.java)

    init {
        TableUtils.createTableIfNotExists(connectionSource, Category::class.java)
    }

    fun saveList(categories: List<Category>) {
        TableUtils.clearTable(connectionSource, Category::class.java)
        categories.forEach { dao.create(it) }
    }

    fun loadList() = dao.queryForAll()
}
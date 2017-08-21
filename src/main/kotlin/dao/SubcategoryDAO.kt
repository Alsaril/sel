package dao

import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import models.Subcategory


class SubcategoryDAO(private val connectionSource: ConnectionSource) {
    private val dao = DaoManager.createDao(connectionSource, Subcategory::class.java)

    init {
        TableUtils.createTableIfNotExists(connectionSource, Subcategory::class.java)
    }

    fun saveList(subcategories: List<Subcategory>) {
        TableUtils.clearTable(connectionSource, Subcategory::class.java)
        subcategories.forEach { dao.createOrUpdate(it) }
    }

    fun loadList() = dao.queryForAll()
}
package dao

import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import models.Product


class ProductDAO(private val connectionSource: ConnectionSource) {
    private val modelClass = Product::class.java
    private val dao = DaoManager.createDao(connectionSource, modelClass)

    init {
        TableUtils.createTableIfNotExists(connectionSource, modelClass)
    }

    fun saveList(products: List<Product>) {
        TableUtils.clearTable(connectionSource, Product::class.java)
        products.forEach { dao.createOrUpdate(it) }
    }

    fun loadList() = dao.queryForAll()

    fun fromCategory(category: Int) = dao.queryForEq("category", category)

    fun fromSubcategory(subcategory: Int) = dao.queryForEq("sub_category", subcategory)

    fun fromName(name: String) = dao.queryBuilder().where().like("name", name).query()

    fun fromBarcode(barcode: String) = dao.queryBuilder().where().like("barcode", barcode).query()
}

package api

import com.j256.ormlite.jdbc.JdbcConnectionSource
import com.j256.ormlite.logger.LocalLog
import com.j256.ormlite.support.ConnectionSource
import dao.NodeDAO
import dao.OperationDAO
import dao.PositionDAO
import dao.ProductDAO
import models.Operation
import models.ProductsData


object LocalBase : ILocalBase {

    private val DATABASE_URL = "jdbc:sqlite:main.db"
    val nodeDao: NodeDAO
    val productDao: ProductDAO
    val operationDao: OperationDAO
    val positionDao: PositionDAO

    init {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "warning")
        val connectionSource: ConnectionSource = JdbcConnectionSource(DATABASE_URL);
        nodeDao = NodeDAO(connectionSource)
        productDao = ProductDAO(connectionSource)
        operationDao = OperationDAO(connectionSource)
        positionDao = PositionDAO(connectionSource)
    }

    override fun save(data: ProductsData) {
        nodeDao.saveList(data.nodes)
        productDao.saveList(data.products)
    }

    override fun products() = productDao.loadList()

    override fun nodes() = nodeDao.loadList()

    override fun productsFromName(name: String) = productDao.fromName(name)

    override fun productsFromBarcode(barcode: String) = productDao.fromBarcode(barcode)

    override fun saveOperation(operation: Operation) = operationDao.save(operation)

    override fun operations() = operationDao.load()

    override fun clearOperations() {
        operationDao.clear()
        positionDao.clear()
    }

    override fun productsData() = ProductsData(nodes(), products())
}
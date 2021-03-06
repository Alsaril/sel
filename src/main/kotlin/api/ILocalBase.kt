package api

import models.Node
import models.Product
import models.ProductsData
import models.operation.Operation

interface ILocalBase {
    fun save(data: ProductsData)
    fun products(): List<Product>
    fun nodes(): List<Node>
    fun productsData(): ProductsData
    fun productsFromName(name: String): List<Product>
    fun productsFromBarcode(barcode: String): List<Product>
    fun saveOperation(operation: Operation)
    fun operations(): List<Operation>
    fun clearOperations()
}
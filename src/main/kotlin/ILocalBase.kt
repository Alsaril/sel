package localBase

import models.*

interface ILocalBase {
    fun save(data: ProductsData)
    fun getProducts(): List<Product>
    fun getCategories(): List<Category>
    fun getSubcategories(): List<Subcategory>
    fun getProductsFromCategory(category: Int): List<Product>
    fun getProductsFromSubcategory(subcategory: Int): List<Product>
    fun getProductsFromName(name: String): List<Product>
    fun getProductsFromBarcode(barcode: String): List<Product>
    fun saveOperation(operation: Operation)
    fun getOperations(): List<Operation>
    fun clearOperations()
}
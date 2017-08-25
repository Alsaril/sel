package api

import kotlinx.coroutines.experimental.Deferred
import models.*
import retrofit2.http.Body
import retrofit2.http.Path

class Result<T>(val result: T?, val state: State, private val success: Boolean, val error: String) {
    constructor(result: T, state: State) : this(result, state, true, "")
    constructor(error: String, state: State) : this(null, state, false, error)

    fun isSuccessful() = success

    fun notNullResult(): T = result!!

    companion object {
        fun successVoidResult(state: State) = Result<Void>(null, state, true, "")
    }
}

enum class State {ONLINE, OFFLINE }

typealias StateListener = (state: State) -> Unit

interface API {
    fun productsData(): Deferred<Result<ProductsData>>

    fun operations(): Deferred<Result<List<Operation>>>

    fun operationsByDate(start: String, end: String): Deferred<Result<List<Operation>>>

    fun addOperation(@Body operation: Operation): Deferred<Result<Void>>

    fun suppliers(): Deferred<Result<List<Supplier>>>

    fun addSupplier(@Body supplier: Supplier): Deferred<Result<Void>>

    fun editSupplier(@Path("id") id: String, @Body supplier: Supplier): Deferred<Result<Void>>

    fun delSupplier(@Path("id") id: String): Deferred<Result<Void>>

    fun barcode(): Deferred<Result<Barcode>>

    fun addProduct(@Body product: Product): Deferred<Result<Product>>

    fun editProduct(@Path("id") id: String, @Body product: Product): Deferred<Result<Void>>

    fun delProduct(@Path("id") id: String): Deferred<Result<Void>>

    fun addNode(@Body node: Node): Deferred<Result<Void>>

    fun editNode(@Path("id") id: String, @Body node: Node): Deferred<Result<Void>>

    fun delNode(@Path("id") id: String): Deferred<Result<Void>>

    fun supplies(): Deferred<Result<List<Supply>>>

    fun productSupplies(id: String): Deferred<Result<List<PositionSupplyFull>>>

    fun addSupply(@Body supplyMin: SupplyMin): Deferred<Result<Void>>

    fun editSupply(@Path("id") id: String, @Body supplyMin: SupplyMin): Deferred<Result<Void>>

    fun delSupply(@Path("id") id: String): Deferred<Result<Void>>

    fun addStateListener(stateListener: StateListener)
}
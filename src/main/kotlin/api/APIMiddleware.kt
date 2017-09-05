package api

import kotlinx.coroutines.experimental.Deferred
import models.Barcode
import models.Node
import models.Product
import models.ProductsData
import models.operation.Operation
import models.reserve.Client
import models.reserve.Reserve
import models.reserve.ReserveMin
import models.supply.PositionSupplyFull
import models.supply.Supplier
import models.supply.Supply
import models.supply.SupplyMin
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

typealias DeferredResult<T> = Deferred<Result<T>>

interface API {
    fun productsData(): DeferredResult<ProductsData>

    fun operations(): DeferredResult<List<Operation>>

    fun nodes(): DeferredResult<List<Node>>

    fun operationsByDate(start: String, end: String): DeferredResult<List<Operation>>

    fun addOperation(@Body operation: Operation): DeferredResult<Void>

    fun suppliers(): DeferredResult<List<Supplier>>

    fun addSupplier(@Body supplier: Supplier): DeferredResult<Void>

    fun editSupplier(@Path("id") id: String, @Body supplier: Supplier): DeferredResult<Void>

    fun delSupplier(@Path("id") id: String): DeferredResult<Void>

    fun barcode(): DeferredResult<Barcode>

    fun addProduct(@Body product: Product): DeferredResult<Product>

    fun editProduct(@Path("id") id: String, @Body product: Product): DeferredResult<Void>

    fun delProduct(@Path("id") id: String): DeferredResult<Void>

    fun addNode(@Body node: Node): DeferredResult<Void>

    fun editNode(@Path("id") id: String, @Body node: Node): DeferredResult<Void>

    fun delNode(@Path("id") id: String): DeferredResult<Void>

    fun supplies(): DeferredResult<List<Supply>>

    fun productSupplies(id: String): DeferredResult<List<PositionSupplyFull>>

    fun addSupply(@Body supplyMin: SupplyMin): DeferredResult<Void>

    fun drafts(): DeferredResult<List<Supply>>

    fun addDraft(@Body supplyMin: SupplyMin): DeferredResult<Void>

    fun delDraft(@Path("id") id: String): DeferredResult<Void>

    fun clients(): DeferredResult<List<Client>>

    fun addClient(@Body client: Client): DeferredResult<Void>

    fun editClient(@Path("id") id: String, @Body client: Client): DeferredResult<Void>

    fun delClient(@Path("id") id: String): DeferredResult<Void>

    fun reserves(): DeferredResult<List<Reserve>>

    fun addReserve(@Body reserveMin: ReserveMin): DeferredResult<Void>

    fun editReserve(@Path("id") id: String, @Body reserveMin: ReserveMin): DeferredResult<Void>

    fun delReserve(@Path("id") id: String): DeferredResult<Void>

    fun expiringProducts(): DeferredResult<List<Product>>

    fun addStateListener(stateListener: StateListener)
}
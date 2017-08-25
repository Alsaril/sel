package api

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import models.*
import network.RetrofitClient
import retrofit2.Response
import utils.awaitResponse
import java.lang.ref.WeakReference

object APIMiddlewareImpl : API {

    private val OK = 200
    private val CREATED = 201
    private val DELETED = 204

    private val networkAPI = RetrofitClient.apiService
    private val localAPI: ILocalBase = LocalBase

    private var state = State.OFFLINE
        set(value) {
            if (field != value) notifyState(value)
            field = value
        }
    private val stateListeners = ArrayList<WeakReference<StateListener>>()

    private fun notifyState(state: State) {
        val i = stateListeners.iterator()
        while (i.hasNext()) {
            val ref = i.next()
            if (ref.get() == null) {
                i.remove()
            } else {
                ref.get()?.invoke(state)
            }
        }
        if (state == State.ONLINE) {
            synchronize()
        }
    }

    private fun synchronize() {
        val operations = localAPI.operations()
        localAPI.clearOperations()

        launch(CommonPool) {
            operations.map { addOperation(it) }.forEach { it.await() }
        }
    }

    override fun addStateListener(stateListener: StateListener) {
        stateListeners.add(WeakReference(stateListener))
    }

    override fun productsData(): Deferred<Result<ProductsData>> = async(CommonPool) {
        val response: Response<ProductsData>
        try {
            response = networkAPI.productsData().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result(localAPI.productsData(), State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            localAPI.save(data) // save responce to local base
            Result(data, State.ONLINE)
        } else {
            Result<ProductsData>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun operations(): Deferred<Result<List<Operation>>> = async(CommonPool) {
        val response: Response<List<Operation>>
        try {
            response = networkAPI.operations().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result(localAPI.operations(), State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<Operation>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun operationsByDate(start: String, end: String): Deferred<Result<List<Operation>>> = async(CommonPool) {
        val response: Response<List<Operation>>
        try {
            response = networkAPI.operationsByDate(start, end).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result(localAPI.operations(), State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<Operation>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun addOperation(operation: Operation): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addOperation(operation).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            localAPI.saveOperation(operation)
            return@async Result.successVoidResult(State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == CREATED) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${CREATED} or response body == null", State.ONLINE)
        }
    }

    override fun suppliers(): Deferred<Result<List<Supplier>>> = async(CommonPool) {
        val response: Response<List<Supplier>>
        try {
            response = networkAPI.suppliers().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<List<Supplier>>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<Supplier>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun addSupplier(supplier: Supplier): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addSupplier(supplier).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == CREATED) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${CREATED} or response body == null", State.ONLINE)
        }
    }

    override fun editSupplier(id: String, supplier: Supplier): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.editSupplier(id, supplier).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == OK) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun delSupplier(id: String): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.delSupplier(id).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == DELETED) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${DELETED} or response body == null", State.ONLINE)
        }
    }

    override fun barcode(): Deferred<Result<Barcode>> = async(CommonPool) {
        val response: Response<Barcode>
        try {
            response = networkAPI.barcode().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Barcode>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<Barcode>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun addProduct(product: Product): Deferred<Result<Product>> = async(CommonPool) {
        val response: Response<Product>
        try {
            response = networkAPI.addProduct(product).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Product>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == CREATED && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<Product>("response code != ${CREATED} or response body == null", State.ONLINE)
        }
    }

    override fun editProduct(id: String, product: Product): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.editProduct(id, product).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == OK) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun delProduct(id: String): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.delProduct(id).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == DELETED) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${DELETED} or response body == null", State.ONLINE)
        }
    }

    override fun addNode(node: Node): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addNode(node).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == CREATED) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${CREATED} or response body == null", State.ONLINE)
        }
    }

    override fun editNode(id: String, node: Node): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.editNode(id, node).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == OK) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun delNode(id: String): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.delNode(id).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == DELETED) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${DELETED} or response body == null", State.ONLINE)
        }
    }

    override fun supplies(): Deferred<Result<List<Supply>>> = async(CommonPool) {
        val response: Response<List<Supply>>
        try {
            response = networkAPI.supplies().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<List<Supply>>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<Supply>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun productSupplies(id: String): Deferred<Result<List<PositionSupplyFull>>> = async(CommonPool) {
        val response: Response<List<PositionSupplyFull>>
        try {
            response = networkAPI.productSupplies(id).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<List<PositionSupplyFull>>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<PositionSupplyFull>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun addSupply(supplyMin: SupplyMin): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addSupply(supplyMin).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == CREATED) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${CREATED} or response body == null", State.ONLINE)
        }
    }

    override fun editSupply(id: String, supplyMin: SupplyMin): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.editSupply(id, supplyMin).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == OK) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun delSupply(id: String): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.delSupply(id).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        state = State.ONLINE
        if (response.code() == DELETED) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${DELETED} or response body == null", State.ONLINE)
        }
    }
}

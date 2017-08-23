package api

/**
 * Created by igor on 23.08.17.
 */
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import models.*
import retrofit2.Response
import java.lang.ref.WeakReference

object APIMiddlewareImpl : API {

    private val OK = 200
    private val CREATED = 201
    private val DELETED = 201

    private val networkAPI = NetworkHelper.API
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
                ref.get()?.stateChanged(state)
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

    override fun addOperation(operation: Operation): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addOperation(operation).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            localAPI.saveOperation(operation)
            return@async Result.successVoidResult(State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == CREATED && data != null) {
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == CREATED && data != null) {
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == DELETED && data != null) {
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == DELETED && data != null) {
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == CREATED && data != null) {
            Result(data, State.ONLINE)
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == DELETED && data != null) {
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

    override fun addSupply(supplyMin: SupplyMin): Deferred<Result<Void>> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addSupply(supplyMin).awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<Void>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == CREATED && data != null) {
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
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
        val data = response.body()
        state = State.ONLINE
        if (response.code() == DELETED && data != null) {
            Result.successVoidResult(State.ONLINE)
        } else {
            Result<Void>("response code != ${DELETED} or response body == null", State.ONLINE)
        }
    }
}

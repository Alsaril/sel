package api

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
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
import network.RetrofitClient
import retrofit2.Response
import utils.awaitResponse
import java.lang.ref.WeakReference

object APIMiddlewareImpl : API {

    private val OK = 200
    private val CREATED = 201
    private val DELETED = 204
    private val IM_TEAPOT = 418

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

    override fun productsData(): DeferredResult<ProductsData> = async(CommonPool) {
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

    override fun operations(): DeferredResult<List<Operation>> = async(CommonPool) {
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

    override fun nodes(): DeferredResult<List<Node>> = async(CommonPool) {
        val response: Response<List<Node>>
        try {
            response = networkAPI.nodes().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result(localAPI.nodes(), State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<Node>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun operationsByDate(start: String, end: String): DeferredResult<List<Operation>> = async(CommonPool) {
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

    override fun addOperation(operation: Operation): DeferredResult<Void> = async(CommonPool) {
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
            Result<Void>(if (response.code() == IM_TEAPOT) "Недостаточно товара на складе" else "response code != ${CREATED} or response body == null", State.ONLINE)
        }
    }

    override fun suppliers(): DeferredResult<List<Supplier>> = async(CommonPool) {
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

    override fun addSupplier(supplier: Supplier): DeferredResult<Void> = async(CommonPool) {
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

    override fun editSupplier(id: String, supplier: Supplier): DeferredResult<Void> = async(CommonPool) {
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

    override fun delSupplier(id: String): DeferredResult<Void> = async(CommonPool) {
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

    override fun barcode(): DeferredResult<Barcode> = async(CommonPool) {
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

    override fun addProduct(product: Product): DeferredResult<Product> = async(CommonPool) {
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

    override fun editProduct(id: String, product: Product): DeferredResult<Void> = async(CommonPool) {
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

    override fun delProduct(id: String): DeferredResult<Void> = async(CommonPool) {
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

    override fun addNode(node: Node): DeferredResult<Void> = async(CommonPool) {
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

    override fun editNode(id: String, node: Node): DeferredResult<Void> = async(CommonPool) {
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

    override fun delNode(id: String): DeferredResult<Void> = async(CommonPool) {
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

    override fun supplies(): DeferredResult<List<Supply>> = async(CommonPool) {
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

    override fun productSupplies(id: String): DeferredResult<List<PositionSupplyFull>> = async(CommonPool) {
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

    override fun addSupply(supplyMin: SupplyMin): DeferredResult<Void> = async(CommonPool) {
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

    override fun drafts(): DeferredResult<List<Supply>> = async(CommonPool) {
        val response: Response<List<Supply>>
        try {
            response = networkAPI.drafts().awaitResponse()
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

    override fun addDraft(supplyMin: SupplyMin): DeferredResult<Void> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addDraft(supplyMin).awaitResponse()
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

    override fun delDraft(id: String): DeferredResult<Void> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.delDraft(id).awaitResponse()
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

    override fun clients(): DeferredResult<List<Client>> = async(CommonPool) {
        val response: Response<List<Client>>
        try {
            response = networkAPI.clients().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<List<Client>>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<Client>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }

    override fun addClient(client: Client): DeferredResult<Void> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addClient(client).awaitResponse()
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

    override fun editClient(id: String, client: Client): DeferredResult<Void> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.editClient(id, client).awaitResponse()
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

    override fun delClient(id: String): DeferredResult<Void> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.delClient(id).awaitResponse()
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


    override fun reserves(): DeferredResult<List<Reserve>> = async(CommonPool) {
        val response: Response<List<Reserve>>
        try {
            response = networkAPI.reserves().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<List<Reserve>>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<Reserve>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }


    override fun addReserve(reserveMin: ReserveMin): DeferredResult<Void> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.addReserve(reserveMin).awaitResponse()
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

    override fun editReserve(id: String, reserveMin: ReserveMin): DeferredResult<Void> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.editReserve(id, reserveMin).awaitResponse()
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

    override fun delReserve(id: String): DeferredResult<Void> = async(CommonPool) {
        val response: Response<Void>
        try {
            response = networkAPI.delReserve(id).awaitResponse()
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

    override fun expiringProducts(): DeferredResult<List<Product>> = async(CommonPool) {
        val response: Response<List<Product>>
        try {
            response = networkAPI.expiringProducts().awaitResponse()
        } catch (t: Throwable) {
            state = State.OFFLINE
            return@async Result<List<Product>>("Exception: ${t.message}", State.OFFLINE)
        }
        val data = response.body()
        state = State.ONLINE
        if (response.code() == OK && data != null) {
            Result(data, State.ONLINE)
        } else {
            Result<List<Product>>("response code != ${OK} or response body == null", State.ONLINE)
        }
    }
}

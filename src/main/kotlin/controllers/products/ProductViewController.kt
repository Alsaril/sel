package controllers.products

import controllers.LoadController
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Node
import models.Product
import models.operation.Operation
import utils.CloseListener
import utils.Dialogs
import utils.makeMenu


class ProductViewController : LoadController<Product?>() {

    private var operationOL = FXCollections.observableArrayList<Operation>()
    private var products: List<Product> = listOf()

    private var productsOL = FXCollections.observableArrayList<Product>()
    private var productsFiltredOL = FXCollections.observableArrayList<Product>()
    private var productsList: List<Product>? = null
    lateinit var selectProduct: Product

    @FXML private lateinit var productTable: TableView<Product>
    @FXML private lateinit var nameColumn: TableColumn<Product, String>
    @FXML private lateinit var barcodeColumn: TableColumn<Product, String>
    @FXML private lateinit var measurementColumn: TableColumn<Product, String>
    @FXML private lateinit var countColumn: TableColumn<Product, String>
    @FXML private lateinit var reserveColumn: TableColumn<Product, String>
    @FXML private lateinit var priceColumn: TableColumn<Product, String>
    @FXML private lateinit var nodeTreeView: TreeView<Node>
    @FXML private lateinit var search: TextField

    @FXML private fun initialize() {
        nameColumn.cellValueFactory = PropertyValueFactory("name")
        measurementColumn.cellValueFactory = PropertyValueFactory("unit")
        barcodeColumn.cellValueFactory = PropertyValueFactory("barcode")
        countColumn.cellValueFactory = PropertyValueFactory("strCount")
        reserveColumn.cellValueFactory = PropertyValueFactory("strReserved")
        priceColumn.cellValueFactory = PropertyValueFactory("priceFormat")

        makeMenu(productTable) {
            "Редактировать" to { editProduct(it) }
            "Удалить" to { delProduct(it) }
            "Информация" to { infoProduct(it) }
        }

        makeMenu(nodeTreeView) {
            "Добавить раздел" to { createNode(it) }
            "Редактировать раздел" to { renameNode(it) }
            "Удалить раздел" to { deleteNode(it) }
            "Добавить товар" to { addProduct(it) }
        }

        search.setOnKeyReleased {
            showProducts(products, nodeTreeView.selectionModel.selectedItem) {
                it.name.contains(search.text, ignoreCase = true) ||
                        it.barcode.contains(search.text, ignoreCase = true) ||
                        it.vendor.contains(search.text, ignoreCase = true)
            }
        }

        nodeTreeView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            showProducts(products, newValue)
        }

        loadProductsData()
    }

    fun selectMode() {
        productTable.setRowFactory { tv ->
            val row = TableRow<Product>()
            row.setOnMouseClicked({ event ->
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    close(productTable.selectionModel.selectedItem)
                }
            })
            row
        }
    }

    private fun addProduct(node: Node) {
        if (node.id == -1) {
            Dialogs.showErrorDialog("Невозможно добавить товар в этот раздел!")
        } else {
            ProductsEditController.show(node = node, owner = productTable as javafx.scene.Node) { result ->
                if (result) {
                    loadProductsData(false)
                }
            }
        }

    }

    private fun editProduct(product: Product) {
        ProductsEditController.show(product = product, owner = productTable as javafx.scene.Node) { result ->
            if (result) {
                loadProductsData(false)
            }
        }

    }

    private fun deleteProduct(product: Product) {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Удаление товарв"
        alert.headerText = "Удалить товар: ${product.name}?"

        val result = alert.showAndWait()
        if (result.get() === ButtonType.OK) {
            delProduct(product)
        } else {

        }
    }

    private fun infoProduct(product: Product) {

    }


    private fun createNode(parentNode: Node) {
        val dialog = TextInputDialog()
        dialog.title = "Новый раздел"
        dialog.headerText = "Добавление раздела"
        dialog.contentText = "Имя:"
        val result = dialog.showAndWait()

        result.ifPresent({ name -> addNode(name, parentNode) })
    }

    private fun renameNode(node: Node) {
        val dialog = TextInputDialog(node.name)
        with(dialog) {
            title = "Изменить раздел"
            headerText = "Изменение раздела"
            contentText = "Имя:"
        }
        val result = dialog.showAndWait()

        result.ifPresent({//
            name ->
            editNode(name, node)
        })
    }


    private fun addNode(name: String, parentNode: Node) = launch(JavaFx) {
        val node = Node()
        if (parentNode.id == -1) {
            node.parent = null
        } else {
            node.parent = parentNode.id
        }
        node.name = name
        val result = api.addNode(node).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Раздел добавлен!")
            loadProductsData()
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    private fun deleteNode(node: Node) {
        val alert = Alert(AlertType.CONFIRMATION)
        alert.title = "Удаление раздела"
        alert.headerText = "Удалить раздел: ${node.name}?"

        val result = alert.showAndWait()
        if (result.get() === ButtonType.OK) {
            delNode(node)
        } else {

        }
    }

    private fun delNode(node: Node) = launch(JavaFx) {
        val id = node.id.toString()
        val result = api.delNode(id).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Раздел удален!")
            loadProductsData()
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    private fun delProduct(product: Product) = launch(JavaFx) {
        val id = product.id.toString()
        val result = api.delProduct(id).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Товар удален!")
            loadProductsData(false)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    private fun editNode(name: String, node: Node) = launch(JavaFx) {
        node.name = name
        val id = node.id.toString()
        val result = api.editNode(id, node = node).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Раздел изменен!")
            loadProductsData()
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }


    private fun loadProductsData(updateTree: Boolean = true) = launch(JavaFx) {
        val result = api.productsData().await()
        if (!result.isSuccessful()) return@launch
        products = result.notNullResult().products
        productsOL = FXCollections.observableArrayList(products)
        productTable.setItems(productsOL)

        if (!updateTree) {
            showProducts(products, nodeTreeView.selectionModel.selectedItem)
            return@launch
        }
        val items = HashMap<Int, TreeItem<Node>>()
        result.notNullResult().nodes.forEach {
            items[it.id] = TreeItem(it)
        }
        val roots = mutableListOf<TreeItem<Node>>()
        items.values.forEach {
            val parent = it.value.parent
            if (parent == null) {
                roots.add(it)
            } else {
                items[parent]?.children?.add(it)
            }
        }
        val root = TreeItem<Node>(Node("Все"))
        root.children.addAll(roots)
        nodeTreeView.root = root
    }

    private fun subProducts(products: List<Product>, node: TreeItem<Node>?): List<Product> {
        if (node == null) return listOf()
        val result = mutableListOf<Product>()
        result.addAll(products.filter { it.parent == node.value.id })
        result.addAll(node.children.flatMap { subProducts(products, it) })
        return result
    }

    private fun showProducts(products: List<Product>, node: TreeItem<Node>?, predicate: ((product: Product) -> Boolean)? = null) {
        var result = subProducts(products, node)
        predicate?.let {
            result = result.filter(it)
        }
        productTable.items.setAll(result)
    }


    fun selectHandle() {
        selectProduct = productTable.selectionModel.selectedItem
        close(selectProduct)
    }

    companion object {
        fun show(select: Boolean,
                 owner: javafx.scene.Node,
                 callback: CloseListener<Product?>? = null) {
            LoadController.show<Product?, ProductViewController>(owner, callback,
                    path = "/view/products/ProductsView.fxml",
                    title = "Товары",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL) {
                if (select) {
                    selectMode()
                }
            }
        }
    }
}


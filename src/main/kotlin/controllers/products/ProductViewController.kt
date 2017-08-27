package controllers.products

import controllers.LoadController
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Node
import models.operation.Operation
import models.Product
import utils.CloseListener
import utils.Dialogs
import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table




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
        nameColumn.setCellValueFactory(PropertyValueFactory("name"))
        measurementColumn.setCellValueFactory(PropertyValueFactory("unit"))
        barcodeColumn.setCellValueFactory(PropertyValueFactory("barcode"))
        countColumn.setCellValueFactory(PropertyValueFactory("strCount"))
        reserveColumn.setCellValueFactory(PropertyValueFactory("strReserved"))
        priceColumn.setCellValueFactory(PropertyValueFactory("priceFormat"))

        val productTableContextMenu = ContextMenu()
        val editProduct = MenuItem("Редактировать")
        editProduct.onAction = EventHandler {
            val item = productTable.selectionModel.selectedItem
            editProduct(item)
        }

        val delProduct = MenuItem("Удалить")
        delProduct.onAction = EventHandler {
            val item = productTable.selectionModel.selectedItem
            delProduct(item)
        }
        val infoProduct = MenuItem("Информация")
        infoProduct.onAction = EventHandler {
            val item = productTable.selectionModel.selectedItem
            infoProduct(item)
        }
        productTableContextMenu.items.setAll(editProduct, delProduct, infoProduct)
        productTable.contextMenu = productTableContextMenu

        val treeItemContextMenu = ContextMenu()
        val addNode = MenuItem("Добавить раздел")
        addNode.onAction = EventHandler {
            val item = nodeTreeView.selectionModel.selectedItem.value
            createNode(item)
        }
        val delNode = MenuItem("Удалть раздел")
        delNode.onAction = EventHandler {
            val item = nodeTreeView.selectionModel.selectedItem.value
            deleteNode(item)
        }
        val addProduct = MenuItem("Добавить товар")
        addProduct.onAction = EventHandler {
            val item = nodeTreeView.selectionModel.selectedItem.value
            addProduct(item)
        }

        treeItemContextMenu.items.setAll(addNode, delNode, addProduct)
        nodeTreeView.contextMenu = treeItemContextMenu

        search.setOnKeyReleased {
            showProducts(products, nodeTreeView.selectionModel.selectedItem) {
                it.name.contains(search.text, ignoreCase = true)
            }
        }

        loadProductsData()

    }

    fun selectMode(){
        productTable.setRowFactory { tv ->
            var row = TableRow<Product>()
            row.setOnMouseClicked({ event ->
                if (event.getClickCount() === 2 && !row.isEmpty()) {
                    close(productTable.selectionModel.selectedItem)
                }
            })
            row
        }
    }

    private fun addProduct(node: Node) {
        ProductsEditController.show(node = node, owner = productTable as javafx.scene.Node) { result ->
            if (result) {
                loadProductsData()
            }
        }
    }

    private fun editProduct(product: Product) {
        ProductsEditController.show(product = product, owner = productTable as javafx.scene.Node) { result ->
            if (result) {
                loadProductsData()
            }
        }

    }

    private fun delProduct(product: Product) {

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


    private fun addNode(name: String, parentNode: Node) = launch(JavaFx) {
        val node = Node()
        node.parent = parentNode.id
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


    private fun loadProductsData() = launch(JavaFx) {
        val result = api.productsData().await()
        if (!result.isSuccessful()) return@launch
        products = result.notNullResult().products
        productsOL = FXCollections.observableArrayList(result.notNullResult().products)
        productTable.setItems(productsOL)

        val items = result.notNullResult().nodes.map { TreeItem(it) }
        val roots = mutableListOf<TreeItem<Node>>()
        items.forEach {
            if (it.value.parent == null) {
                roots.add(it)
            } else {
                items[it.value.parent!! - 1].children.add(it)
            }
        }
        val root = TreeItem<Node>(Node("Все"))
        root.children.addAll(roots)
        nodeTreeView.root = root
        nodeTreeView.selectionModel.select(0)

        nodeTreeView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            showProducts(result.notNullResult().products, newValue)
        }
    }

    private fun subProducts(products: List<Product>, node: TreeItem<Node>?): List<Product> {
        if (node == null) return listOf()
        val result = mutableListOf<Product>()
        result.addAll(products.filter { it.parent == node.value.id })
        result.addAll(node.children.flatMap { subProducts(products, it) })
        return result
    }

    private fun showProducts(products: List<Product>, node: TreeItem<Node>, predicate: ((product: Product) -> Boolean)? = null) {
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
                 callback: CloseListener<Product?>) {
            LoadController.show<Product?, ProductViewController>(owner, callback,
                    path = "/view/products/ProductsView.fxml",
                    title = "Товары",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL) {
                if(select){
                    selectMode()
                }
            }
        }
    }
}


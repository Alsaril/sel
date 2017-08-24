package controllers.products

import api.APIMiddlewareImpl
import api.API
import controllers.LoadController
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import javafx.stage.Stage
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Operation
import models.Product
import network.RetrofitClient
import utils.CloseListener
import java.io.IOException


class ProductViewController: LoadController<Boolean>() {

    private var operationOL = FXCollections.observableArrayList<Operation>()
    private var productList: List<Product>? = null

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

    @FXML private fun initialize() {
        nameColumn.setCellValueFactory(PropertyValueFactory("productName"))
        measurementColumn.setCellValueFactory(PropertyValueFactory("unit"))
        barcodeColumn.setCellValueFactory(PropertyValueFactory("barcode"))
        countColumn.setCellValueFactory(PropertyValueFactory("count"))
        reserveColumn.setCellValueFactory(PropertyValueFactory("reserved"))
        priceColumn.setCellValueFactory(PropertyValueFactory("priceFormat"))

        val productTableContextMenu = ContextMenu()
        val editProduct = MenuItem("Редактировать")
        editProduct.onAction = EventHandler {
            val item = productTable.selectionModel.selectedItem
            editProduct(item)
        }

        val delProduct = MenuItem("Удалить")
        delProduct.onAction = EventHandler { val item = productTable.selectionModel.selectedItem }
        val infoProduct = MenuItem("Информация")
        infoProduct.onAction = EventHandler { val item = productTable.selectionModel.selectedItem }
        productTableContextMenu.items.setAll(editProduct, delProduct, infoProduct)
        productTable.contextMenu = productTableContextMenu


        loadProductsData()

    }

    fun showAddProductDialog(actionEvent: ActionEvent) {
        try {
            val stage = Stage()
            val loader = FXMLLoader()
            loader.location = javaClass.getResource("/view/products/ProductEditView.fxml")
            val categoryAddFXML = loader.load<Parent>()
            stage.title = "Новый товар"
            stage.minHeight = 150.0
            stage.minWidth = 400.0
            stage.scene = Scene(categoryAddFXML)
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner((actionEvent.source as Node).scene.window)

            val controller = loader.getController<ProductsEditController>()
            //controller.setCategoriesAndSubcategories(categoryList, subcategoryList);

            stage.showAndWait()

            if (controller.isOk) {
                loadProductsData()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun editProduct(product: Product) {
        try {
            val stage = Stage()
            val loader = FXMLLoader()
            loader.location = javaClass.getResource("/view/products/ProductEditView.fxml")
            val categoryAddFXML = loader.load<Parent>()
            stage.title = "Редактирование товара"
            stage.minHeight = 150.0
            stage.minWidth = 400.0
            stage.scene = Scene(categoryAddFXML)
            stage.initModality(Modality.WINDOW_MODAL)

            val controller = loader.getController<ProductsEditController>()
            //controller.setCategoriesAndSubcategories(categoryList, subcategoryList);
            controller.setProduct(product)

            stage.showAndWait()

            if (controller.isOk) {
                loadProductsData()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun delProduct() {

    }


    private fun loadProductsData() = launch(JavaFx) {
        val result = api.productsData().await()
        if (result.isSuccessful()) {
            productsOL = FXCollections.observableArrayList(result.result?.products)
            productTable.setItems(productsOL)
        }
    }


    fun selectHandle() {
        selectProduct = productTable.selectionModel.selectedItem
        close(true)
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/products/ProductsView.fxml",
                    title = "Товары",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}

package controllers.products

import controllers.LoadController
import controllers.supply.FirstSupplyController
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Modality
import javafx.stage.Stage
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Node
import models.Product
import utils.CloseListener
import utils.Dialogs
import utils.Measure
import utils.Printer
import java.io.IOException


class ProductsEditController : LoadController<Boolean>() {

    var edit: Boolean = false
    var editProduct: Product = Product()
    lateinit var parentNode: Node


    @FXML private lateinit var productName: TextField
    @FXML private lateinit var productLName: TextField
    @FXML private lateinit var unitComboBox: ComboBox<Measure>
    @FXML private lateinit var productBarcode: TextField
    @FXML private lateinit var productVendor: TextField
    @FXML private lateinit var productProducer: TextField
    @FXML private lateinit var productIntegerOnly: CheckBox
    @FXML private lateinit var productMinCount: TextField
    @FXML private lateinit var nodeLabel: Label

    @FXML private fun initialize() {
        unitComboBox.items = FXCollections.observableArrayList(Measure.items())
        unitComboBox.selectionModel.select(0)
    }

    fun handleBarcode() {
        loadBarcode()
    }

    private fun loadBarcode() = launch(JavaFx) {
        val result = api.barcode().await()
        if (result.isSuccessful()) {
            productBarcode.text = result.result.toString()
        }
    }

    fun handleAdd() {
        if (unitComboBox.selectionModel.selectedItem == null) {
            Dialogs.showErrorDialog("Не выбрана единица измерения товара")
            return
        }

        var valid = true
        val product = Product()
        product.name = productName.text
        product.shortName = productLName.text
        product.unit = unitComboBox.selectionModel.selectedItem.str
        product.barcode = productBarcode.text
        product.vendor = productVendor.text
        product.producer = productProducer.text
        product.isInteger = productIntegerOnly.isSelected
        try {
            product.minCount = productMinCount.text.toDouble()
        } catch (e: NumberFormatException) {
            Dialogs.showErrorDialog("Ошибка в поле: минимальное кол-во!")
            valid = false
        }
        product.parent = editProduct.parent
        if (valid) {
            if (edit) {
                editProduct(editProduct.id.toString(), product)
            } else {
                addProduct(product)
            }
        }
    }

    fun handlePrint() {
        Printer.printBarcode(productBarcode.text)
    }

    fun handleNode(actionEvent: ActionEvent) {
        SelectParentController.show(actionEvent.source as javafx.scene.Node) { result ->
            editProduct.parent = result
            nodeLabel.text = result.toString()
        }
    }

    fun edit(product: Product) {
        productName.text = product.name
        productLName.text = product.shortName
        unitComboBox.selectionModel.select(Measure.fromName(product.unit))
        productBarcode.text = product.barcode
        productVendor.text = product.vendor
        productProducer.text = product.producer
        productIntegerOnly.isSelected = product.isInteger
        productMinCount.text = product.getStrMinCount()
        nodeLabel.text = product.parent.toString()


        edit = true
        editProduct = product
    }


    private fun editProduct(id: String, product: Product) = launch(JavaFx) {
        val result = api.editProduct(id, product).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Товар успешно изменен!")
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    private fun addProduct(product: Product) = launch(JavaFx) {
        val result = api.addProduct(product).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Товар успешно добавлен!")
            supply(result.result)
            close(true)

        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    fun supply(product: Product?) {
        if (product != null) {
            try {
                val stage = Stage()
                val loader = FXMLLoader()
                loader.location = javaClass.getResource("/view/supply/FirstSupply.fxml")
                val categoryAddFXML = loader.load<Parent>()
                stage.title = "Новая поставка"
                stage.scene = Scene(categoryAddFXML)
                stage.initOwner(nodeLabel.scene.window)
                stage.initModality(Modality.WINDOW_MODAL)

                val controller = loader.getController<FirstSupplyController>()
                controller.setProduct(product)

                stage.showAndWait()

            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }


    companion object {
        fun show(node: models.Node? = null,
                 product: Product? = null,
                 owner: javafx.scene.Node,
                 callback: CloseListener<Boolean>) {
            LoadController.show<Boolean, ProductsEditController>(owner, callback,
                    path = "/view/products/ProductEditView.fxml",
                    title = "Товары",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL) {
                if (product != null) {
                    edit(product)
                }
                if (node != null) {
                    editProduct.parent = node.id
                    nodeLabel.text = editProduct.parent.toString()
                }
            }
        }
    }

}

package controllers.products

import controllers.LoadController
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Node
import models.Product
import utils.CloseListener
import utils.Dialogs


class ProductsEditController : LoadController<Boolean>() {

    var edit: Boolean = false
    var editProduct: Product = Product()
    lateinit var parentNode: Node


    @FXML private lateinit var productName: TextField
    @FXML private lateinit var productLName: TextField
    @FXML private lateinit var productMeasurement: TextField
    @FXML private lateinit var productBarcode: TextField
    @FXML private lateinit var productVendor: TextField
    @FXML private lateinit var productProducer: TextField
    @FXML private lateinit var productIntegerOnly: CheckBox
    @FXML private lateinit var productMinCount: TextField
    @FXML private lateinit var nodeLabel: Label

    @FXML private fun initialize() {

    }

    fun handleBarcode() {}
    fun handleAdd() {
        val product = Product()
        product.name = productName.text
        product.shortName = productLName.text
        product.unit = productMeasurement.text
        product.barcode = productBarcode.text
        product.vendor = productVendor.text
        product.producer = productProducer.text
        product.isInteger = productIntegerOnly.isSelected
        try {
            product.minCount = productMinCount.text.toDouble()
        } catch (e: NumberFormatException) {
            Dialogs.showExeptionDialog("Ошибка в поле: минимальное кол-во!")
        }
        product.parent = editProduct.parent
        if (edit) {
            editProduct(editProduct.id.toString(), product)
        } else {
            addProduct(product)
        }
    }

    fun handlePrint() {}

    fun handleNode(actionEvent: ActionEvent) {
        SelectParentController.show(actionEvent.source as javafx.scene.Node) { result ->
            editProduct.parent = result
        }
    }

    fun edit(product: Product) {
        productName.text = product.name
        productLName.text = product.shortName
        productMeasurement.text = product.unit
        productBarcode.text = product.barcode
        productVendor.text = product.vendor
        productProducer.text = product.producer
        productIntegerOnly.isSelected = product.isInteger
        productMinCount.text = product.minCountFormat()
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
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
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

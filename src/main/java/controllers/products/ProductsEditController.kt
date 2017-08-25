package controllers.products

import controllers.LoadController
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.stage.Modality
import models.Product
import utils.CloseListener


class ProductsEditController : LoadController<Boolean>() {

    var edit: Boolean = false
    lateinit var editProduct: Product


    @FXML private lateinit var productName: TextField
    @FXML private lateinit var productLName: TextField
    @FXML private lateinit var productMeasurement: TextField
    @FXML private lateinit var productBarcode: TextField
    @FXML private lateinit var productVendor: TextField
    @FXML private lateinit var productProducer: TextField
    @FXML private lateinit var productIntegerOnly: CheckBox
    @FXML private lateinit var productPrice: TextField
    @FXML private lateinit var categoryLable: Label
    @FXML private lateinit var subcategoryLable: Label
    @FXML private lateinit var productMinCount: TextField

    @FXML private fun initialize() {

    }

    fun handleBarcode() {}
    fun handleAdd() {}
    fun handlePrint() {}

    public fun edit(product: Product) {
        edit = true
        editProduct = product
    }


    companion object {
        fun show(node: models.Node? = null,
                 product: Product? = null,
                 owner: Node,
                 callback: CloseListener<Boolean>) {
            LoadController.show<Boolean, ProductsEditController>(owner, callback,
                    path = "/view/products/ProductEditView.fxml",
                    title = "Товары",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL) {
                edit(product!!)
            }
        }
    }

}

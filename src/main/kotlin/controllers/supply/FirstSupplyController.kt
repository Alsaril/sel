package controllers.supply

import api.API
import api.APIMiddlewareImpl
import controllers.LoadController
import controllers.products.ProductsEditController
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.stage.Modality
import javafx.stage.Stage
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Product
import models.supply.PositionSupplyMin
import models.supply.SupplyMin
import utils.CloseListener
import utils.Dialogs
import utils.Position

class FirstSupplyController{

    protected var api: API = APIMiddlewareImpl

    @FXML private lateinit var countField: TextField
    @FXML private lateinit var priceField: TextField

    var editProduct = Product()


    fun handleOk() {
        var valid = true
        var count = 0.0
        var price = 0.0
        try {
            count = countField.text.toDouble()
            price = countField.text.toDouble()
        } catch (e: NumberFormatException) {
            Dialogs.showErrorDialog("Введите в поля числа!")
            valid = false
        }

        val supply = SupplyMin()
        supply.document = "Добавление"
        supply.documentInfo = "Поставка при создании товара"
        supply.documentDate = "нет данных"
        supply.supplier = 1

        val position = PositionSupplyMin(0, count, 0.0, price, editProduct.id)
        val positions = FXCollections.observableArrayList<PositionSupplyMin>(position)
        supply.positions = positions

        newSupply(supply)
    }

    private fun newSupply(supplyMin: SupplyMin) = launch(JavaFx) {
        val result = api.addSupply(supplyMin).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Поставка добавлена успешно!")
            close()
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    fun close() {
        val stage = countField.scene.window as Stage
        stage.close()
    }

    fun setProduct(product: Product){
        editProduct = product
    }


}

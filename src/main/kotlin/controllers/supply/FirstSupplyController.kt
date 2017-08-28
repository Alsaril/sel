package controllers.supply

import controllers.LoadController
import controllers.products.ProductsEditController
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.Product
import models.supply.PositionSupplyMin
import models.supply.SupplyMin
import utils.CloseListener
import utils.Dialogs
import utils.Position

class FirstSupplyController : LoadController<Boolean>() {

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

        addSupply(supply)
    }

    private fun addSupply(supplyMin: SupplyMin) = launch(JavaFx) {
        val result = api.addSupply(supplyMin).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Поставка добавлена успешно!")
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }


    companion object {
        fun show(product: Product? = null,
                 owner: javafx.scene.Node,
                 callback: CloseListener<Boolean>) {
            LoadController.show<Boolean, FirstSupplyController>(owner, callback,
                    path = "/view/supply/FirstSupply.fxml",
                    title = "Новая поставка",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL) {
                if (product != null) {
                    editProduct = product
                } else {
                    Dialogs.showErrorDialog("Товар не найден!")
                    close(false)
                }

            }
        }
    }
}

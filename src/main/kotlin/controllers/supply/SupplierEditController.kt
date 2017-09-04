package controllers.supply

import controllers.LoadController
import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.stage.Modality
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.supply.Supplier
import utils.CloseListener
import utils.Dialogs
import utils.Utils

/**
 * Created by andrey on 25.07.17.
 */
class SupplierEditController : LoadController<Boolean>() {

    @FXML private lateinit var nameField: TextField
    @FXML private lateinit var innField: TextField
    @FXML private lateinit var urAddressField: TextArea
    @FXML private lateinit var physAddressField: TextArea
    @FXML private lateinit var requisitesField: TextArea
    @FXML private lateinit var name1Field: TextField
    @FXML private lateinit var phone1Field: TextField
    @FXML private lateinit var name2Field: TextField
    @FXML private lateinit var phone2Field: TextField

    @FXML
    private fun initialize() {

    }

    var editSupplier = Supplier()
    private var edit = false


    fun edit(supplier: Supplier) {

        nameField.text = supplier.name
        innField.text = supplier.inn
        urAddressField.text = Utils.dataFormat(supplier.urAddress)
        physAddressField.text = Utils.dataFormat(supplier.physAddress)
        requisitesField.text = Utils.dataFormat(supplier.requisites)
        name1Field.text = Utils.dataFormat(supplier.name1)
        phone1Field.text = Utils.dataFormat(supplier.phone1)
        name2Field.text = Utils.dataFormat(supplier.name2)
        phone2Field.text = Utils.dataFormat(supplier.phone2)


        editSupplier = supplier
        edit = true
    }


    fun handleOk() {
        editSupplier.name = nameField.text
        editSupplier.inn = innField.text
        editSupplier.urAddress = Utils.fieldCheck(urAddressField.text)
        editSupplier.physAddress = Utils.fieldCheck(physAddressField.text)
        editSupplier.requisites = Utils.fieldCheck(requisitesField.text)
        editSupplier.name1 = Utils.fieldCheck(name1Field.text)
        editSupplier.name2 = Utils.fieldCheck(name2Field.text)
        editSupplier.phone1 = Utils.fieldCheck(phone1Field.text)
        editSupplier.phone2 = Utils.fieldCheck(phone2Field.text)

        if (edit) {
            editSupplier(editSupplier.id.toString(), editSupplier)
        } else {
            addSupplier(editSupplier)
        }
    }

    fun editSupplier(id: String, supplier: Supplier) = launch(JavaFx) {
        val result = api.editSupplier(id, supplier).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Поставщик успешно отредактирован!")
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }

    fun addSupplier(supplier: Supplier) = launch(JavaFx) {
        val result = api.addSupplier(supplier).await()
        if (result.isSuccessful()) {
            Dialogs.showDialog("Поставщик успешно добавлен!")
            close(true)
        } else {
            Dialogs.showExeptionDialog(result.error)
        }
    }


    companion object {
        fun show(supplier: Supplier? = null,
                 owner: javafx.scene.Node,
                 callback: CloseListener<Boolean>) {
            LoadController.show<Boolean, SupplierEditController>(owner, callback,
                    path = "/view/supply/SuppliersEditView.fxml",
                    title = "Поставщик",
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL) {
                if (supplier != null) {
                    edit(supplier)
                }

            }
        }
    }
}

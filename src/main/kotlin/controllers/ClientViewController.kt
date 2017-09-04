package controllers

import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import javafx.stage.Window
import kotlinx.coroutines.experimental.javafx.JavaFx
import kotlinx.coroutines.experimental.launch
import models.reserve.Client
import utils.CloseListener

class ClientViewController : LoadController<Void>() {

    @FXML private lateinit var clientTable: TableView<Client>
    @FXML private lateinit var nameColumn: TableColumn<Client, String>
    @FXML private lateinit var phoneColumn: TableColumn<Client, String>
    @FXML private lateinit var nameLabel: Label
    @FXML private lateinit var phoneLabel: Label
    @FXML private lateinit var addressLabel: Label
    @FXML private lateinit var commentLabel: Label

    @FXML
    private fun initialize() {
        nameColumn.setCellValueFactory(PropertyValueFactory("name"))
        phoneColumn.setCellValueFactory(PropertyValueFactory("phone"))

        val clientTableContextMenu = ContextMenu()
        val editClient = MenuItem("Редактировать")
        editClient.onAction = EventHandler {
            val item = clientTable.selectionModel.selectedItem
            edit(item)
        }

        clientTableContextMenu.items.add(editClient)
        clientTable.contextMenu = clientTableContextMenu

        clientTable.selectionModel.selectedItemProperty().addListener { _, _, newValue -> select(newValue) }

        update()
    }

    private fun update() = launch(JavaFx) {
        val result = api.clients().await()
        if (!result.isSuccessful()) return@launch

        val clients = result.notNullResult()

        clientTable.items = FXCollections.observableArrayList(clients)
    }

    private fun select(client: Client?) {
        if (client != null) {
            nameLabel.text = client.name
            phoneLabel.text = client.phone
            addressLabel.text = client.address
            commentLabel.text = client.comment
        } else {
            nameLabel.text = "Нет данных"
            phoneLabel.text = "Нет данных"
            addressLabel.text = "Нет данных"
            commentLabel.text = "Нет данных"
        }
    }

    fun add() {
        NewClientController.show(stage) {
            if (it) {
                update()
            }
        }
    }

    fun edit(client: Client) {
        NewClientController.show(stage, client) {
            if (it) {
                update()
            }
        }
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Void>? = null) {
            show(owner.scene.window, callback)
        }


        fun show(owner: Window, callback: CloseListener<Void>? = null) {
            LoadController.show(owner, callback,
                    path = "/view/reserves/ClientView.fxml",
                    title = "Клиенты",
                    minHeight = 600.0,
                    minWidth = 800.0,
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
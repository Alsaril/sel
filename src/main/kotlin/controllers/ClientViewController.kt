package controllers

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
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

        clientTable.selectionModel.selectedItemProperty().addListener { _, _, newValue -> select(newValue) }

        update()
    }

    private fun update() = launch(JavaFx) {
        val result = api.clients().await()
        if (!result.isSuccessful()) return@launch

        val clients = result.notNullResult()

        clientTable.items = FXCollections.observableArrayList(clients)
    }

    private fun select(client: Client) {
        nameLabel.text = client.name
        phoneLabel.text = client.phone
        addressLabel.text = client.address
        commentLabel.text = client.comment
    }

    fun add() {
        NewClientController.show(stage) {
            if (it) {
                update()
            }
        }
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Void>) {
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
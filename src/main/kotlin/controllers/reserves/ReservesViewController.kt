package controllers.reserves

import controllers.ClientViewController
import controllers.LoadController
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
import models.reserve.Reserve
import models.reserve.ReservePositionFull
import utils.CloseListener

class ReservesViewController : LoadController<Boolean>() {
    @FXML private lateinit var reservesTable: TableView<Reserve>
    @FXML private lateinit var dateColumn: TableColumn<Reserve, String>
    @FXML private lateinit var clientColumn: TableColumn<Reserve, String>

    @FXML private lateinit var clientLabel: Label
    @FXML private lateinit var dateLabel: Label
    @FXML private lateinit var commentLabel: Label

    @FXML private lateinit var positionTable: TableView<ReservePositionFull>
    @FXML private lateinit var productColumn: TableColumn<ReservePositionFull, String>
    @FXML private lateinit var countColumn: TableColumn<ReservePositionFull, String>
    @FXML private lateinit var priceColumn: TableColumn<ReservePositionFull, String>

    private lateinit var reserves: List<Reserve>

    @FXML
    fun initialize() {
        dateColumn.setCellValueFactory(PropertyValueFactory("date"))
        clientColumn.setCellValueFactory(PropertyValueFactory("client"))

        productColumn.setCellValueFactory(PropertyValueFactory("product"))
        countColumn.setCellValueFactory(PropertyValueFactory("count"))
        priceColumn.setCellValueFactory(PropertyValueFactory("price"))

        reservesTable.selectionModel.selectedItemProperty().addListener { _, _, newValue -> select(newValue) }

        update()
    }

    private fun select(reserve: Reserve?) {
        if (reserve == null) {
            clientLabel.text = "Нет данных"
            dateLabel.text = "Нет данных"
            commentLabel.text = "Нет данных"

            positionTable.items = FXCollections.observableArrayList()
        } else {
            clientLabel.text = reserve.client.name
            dateLabel.text = reserve.date
            commentLabel.text = reserve.client.comment

            positionTable.items = FXCollections.observableArrayList(reserve.positions)
        }
    }

    private fun update() = launch(JavaFx) {
        val result = api.reserves().await()
        if (!result.isSuccessful()) return@launch

        reserves = result.notNullResult()
        reservesTable.items = FXCollections.observableArrayList(reserves)
    }

    fun newReserve() {
        NewReserveController.show(stage) {
            if (it) {
                update()
            }
        }
    }

    fun showClients() {
        ClientViewController.show(stage) {}
    }

    companion object {
        fun show(owner: Node, callback: CloseListener<Boolean>) {
            LoadController.show(owner, callback,
                    path = "/view/reserves/ReservesView.fxml",
                    title = "Резервы",
                    minHeight = 600.0,
                    minWidth = 800.0,
                    isResizable = false,
                    modality = Modality.WINDOW_MODAL)
        }
    }
}
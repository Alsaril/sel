package controllers

import controllers.cashbox.PasswordController
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage

import java.io.IOException

/**
 * Created by andrey on 12.07.17.
 */
class Controller {
    fun showProducts(actionEvent: ActionEvent) {
        try {
            val stage = Stage()
            val categoryAddFXML = FXMLLoader.load<Parent>(javaClass.getResource("/view/products/ProductsView.fxml"))
            stage.title = "Товары"
            stage.minHeight = 800.0
            stage.minWidth = 900.0
            stage.isResizable = false
            stage.scene = Scene(categoryAddFXML)
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner((actionEvent.source as Node).scene.window)
            stage.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun showPasswordCheckDialog(actionEvent: ActionEvent) {
        PasswordController.show(actionEvent.source as Node) { result ->
            if (result) {
                showOperations(actionEvent)
            }
        }
    }

    private fun showOperations(actionEvent: ActionEvent) {
        try {
            val stage = Stage()
            val categoryAddFXML = FXMLLoader.load<Parent>(javaClass.getResource("/view/cashbox/CashboxView.fxml"))
            stage.title = "Касса"
            stage.minHeight = 800.0
            stage.minWidth = 900.0
            stage.scene = Scene(categoryAddFXML)
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner((actionEvent.source as Node).scene.window)
            stage.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun showSupply(actionEvent: ActionEvent) {
        try {
            val stage = Stage()
            val categoryAddFXML = FXMLLoader.load<Parent>(javaClass.getResource("/view/supply/SupplyView.fxml"))
            stage.title = "Поставки"
            stage.minHeight = 800.0
            stage.minWidth = 900.0
            stage.isResizable = false
            stage.scene = Scene(categoryAddFXML)
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner((actionEvent.source as Node).scene.window)
            stage.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun showSettings(actionEvent: ActionEvent) {
         SettingsController.show(actionEvent.source as Node){

         }
    }

    fun showClients(actionEvent: ActionEvent) {
        try {
            val stage = Stage()
            val categoryAddFXML = FXMLLoader.load<Parent>(javaClass.getResource("/view/ClientsView.fxml"))
            stage.title = "Клиенты"
            stage.minHeight = 800.0
            stage.minWidth = 900.0
            stage.isResizable = false
            stage.scene = Scene(categoryAddFXML)
            stage.initModality(Modality.WINDOW_MODAL)
            stage.initOwner((actionEvent.source as Node).scene.window)
            stage.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}

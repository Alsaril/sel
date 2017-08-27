package controllers

import controllers.cashbox.OperationController
import controllers.cashbox.PasswordController
import controllers.products.ProductViewController
import controllers.reserves.ReservesViewController
import controllers.supply.SuppliesViewController
import javafx.event.ActionEvent
import javafx.scene.Node

class Controller {
    fun showProducts(actionEvent: ActionEvent) {
        ProductViewController.show(actionEvent.source as Node) {}
    }

    fun showPasswordCheckDialog(actionEvent: ActionEvent) {
        PasswordController.show(actionEvent.source as Node) { result ->
            if (result) {
                showOperations(actionEvent)
            }
        }
    }

    private fun showOperations(actionEvent: ActionEvent) {
        OperationController.show(actionEvent.source as Node) {}
    }

    fun showSupply(actionEvent: ActionEvent) {
        SuppliesViewController.show(actionEvent.source as Node) {}
    }

    fun showSettings(actionEvent: ActionEvent) {
        SettingsController.show(actionEvent.source as Node) {}
    }

    fun showClients(actionEvent: ActionEvent) {
        ClientViewController.show(actionEvent.source as Node) {}
    }

    fun showReserves(actionEvent: ActionEvent) {
        ReservesViewController.show(actionEvent.source as Node) {}
    }

}

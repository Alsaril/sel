package controllers;

import controllers.cashbox.PasswordCheckController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by andrey on 12.07.17.
 */
public class Controller {
    public void showProducts(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("/view/ProductsView.fxml"));
            stage.setTitle("Товары");
            stage.setMinHeight(800);
            stage.setMinWidth(900);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showPasswordCheckDialog(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/cashbox/PasswordCheck.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Введите пароль");
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            PasswordCheckController controller = loader.getController();
            stage.showAndWait();

            if (controller.isOk()) {
                showOperations(actionEvent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showOperations(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("/view/cashbox/CashboxView.fxml"));
            stage.setTitle("Касса");
            stage.setMinHeight(800);
            stage.setMinWidth(900);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showSupply(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("/view/supply/SupplyView.fxml"));
            stage.setTitle("Поставки");
            stage.setMinHeight(800);
            stage.setMinWidth(900);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showSettings(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("/view/SettingsView.fxml"));
            stage.setTitle("Настройки");
            stage.setMinHeight(800);
            stage.setMinWidth(900);
            //stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showClients(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("/view/ClientsView.fxml"));
            stage.setTitle("Клиенты");
            stage.setMinHeight(800);
            stage.setMinWidth(900);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

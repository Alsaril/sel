package controllers.supply;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by andrey on 25.07.17.
 */
public class SupplyViewController {

    public void showSuppliers(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("/view/supply/SuppliersView.fxml"));
            stage.setTitle("Поставщики");
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

    public void showSupplyAddView(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("/view/supply/SupplyAddView.fxml"));
            stage.setTitle("Новая поставка");
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

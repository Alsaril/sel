package controllers.supply;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;

import java.io.IOException;

/**
 * Created by andrey on 25.07.17.
 */
public class SuppliersViewController {
    Api api = RetrofitClient.getApiService();
    private ObservableList<Supplier> suppliersOL = FXCollections.observableArrayList();

    @FXML
    private TableView<Supplier> suppliersTable;
    @FXML
    private TableColumn<Supplier, String> nameColumn;
    @FXML
    private TableColumn<Supplier, String> addressColumn;


    private void loadSuppliersData() {
        Call<Suppliers> call = api.suppliersData();
        call.enqueue(new Callback<Suppliers>() {
            @Override
            public void onResponse(Call<Suppliers> call, Response<Suppliers> response) {
                if (response.code() == 200) {
                    suppliersOL = FXCollections.observableArrayList(response.body().getSuppliers());
                    suppliersTable.setItems(suppliersOL);

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Dialogs.showErrorDialog("Ошибка запроса на сервер!");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Suppliers> call, Throwable t) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Dialogs.showErrorDialog("Ошибка запроса! Проверьте соеденение с интернетом!");
                        //Dialogs.showExeptionDialog(t.getMessage());
                    }
                });
            }
        });
    }

    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        nameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
        loadSuppliersData();


    }

    public void showAddSupplierDialog(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("../../view/supply/SuppliersEditView.fxml"));
            stage.setTitle("Новый поставщик");
            stage.setMinHeight(600);
            stage.setMinWidth(800);
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

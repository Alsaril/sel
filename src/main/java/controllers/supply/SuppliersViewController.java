package controllers.supply;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Supplier;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;

import java.io.IOException;
import java.util.List;

/**
 * Created by andrey on 25.07.17.
 */
public class SuppliersViewController {
    Api api = RetrofitClient.INSTANCE.getApiService();
    private ObservableList<Supplier> suppliersOL = FXCollections.observableArrayList();

    @FXML
    private TableView<Supplier> suppliersTable;
    @FXML
    private TableColumn<Supplier, String> nameColumn;
    @FXML
    private TableColumn<Supplier, String> addressColumn;


    private void loadSuppliersData() {
        Call<List<Supplier>> call = api.suppliers();
        call.enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                if (response.code() == 200) {
                    suppliersOL = FXCollections.observableArrayList(response.body());
                    suppliersTable.setItems(suppliersOL);
                } else {
                    Dialogs.INSTANCE.showErrorDialog("Ошибка запроса на сервер!");
                }
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
                Dialogs.INSTANCE.showExeptionDialog(t.getMessage());
            }
        });
    }

    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        nameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("urAddress"));
        loadSuppliersData();


    }


    public void handleAdd(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/supply/SuppliersEditView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Новый поставщик");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            SupplierEditController controller = loader.getController();

            stage.showAndWait();

            if (controller.isOkClicked()) {
                loadSuppliersData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEdit(Supplier supplier) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/supply/SuppliersEditView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Редактировать поставщика");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);

            SupplierEditController controller = loader.getController();
            controller.setSupplier(supplier);

            stage.showAndWait();

            if (controller.isOkClicked()) {
                loadSuppliersData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDel(Supplier supplier) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/supply/DelSupplier.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Удалить поставщика");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);

            SupplierEditController controller = loader.getController();
            controller.setSupplier(supplier);

            stage.showAndWait();

            if (controller.isOkClicked()) {
                loadSuppliersData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

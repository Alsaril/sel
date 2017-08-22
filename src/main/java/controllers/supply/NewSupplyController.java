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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrey on 25.07.17.
 */
public class NewSupplyController {
    Product firstSupplyProduct;

    @FXML
    private ComboBox<Supplier> supplierCombobox;
    @FXML
    private TextField priceField;
    @FXML
    private TextField countField;


    @FXML
    private void initialize() {
        //loadSuppliersData();
    }

    Api api = RetrofitClient.getApiService();
    private ObservableList<Supplier> suppliersOL = FXCollections.observableArrayList();

    private void loadSuppliersData() {
        Call<List<Supplier>> call = api.suppliersData();
        call.enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                if (response.code() == 200) {
                    suppliersOL = FXCollections.observableArrayList(response.body());
                    supplierCombobox.setItems(suppliersOL);

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
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
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

    public void showSelectProductDialog(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            Parent categoryAddFXML = FXMLLoader.load(getClass().getResource("/view/supply/SelectProductView.fxml"));
            stage.setTitle("Выберете товар");
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

    public void addProductToList(){

    }

    public void deleteProductsToList(){

    }

    public void editProductsToList(){

    }

    public void setProductToFirstSupply(Product product){
        firstSupplyProduct = product;
    }

    public void handleAddFirstSupply(ActionEvent actionEvent){
        SupplyMin supplyMin = new SupplyMin();
        supplyMin.setSupplier(1);
        supplyMin.setActNum("first");
        PositionSupplyMin positionSupplyMin = new PositionSupplyMin();
        positionSupplyMin.setCount(Double.valueOf(countField.getText()));
        positionSupplyMin.setProduct(firstSupplyProduct.getId());
        positionSupplyMin.setPrice(Double.valueOf(priceField.getText()));
        List<PositionSupplyMin> positionSupplyMinList = new ArrayList<PositionSupplyMin>();
        positionSupplyMinList.add(positionSupplyMin);
        supplyMin.setPositions(positionSupplyMinList);

        addSupply(supplyMin);
    }

    public void addSupply(SupplyMin supplyMin) {
        Call<Void> call = api.addSupply(supplyMin);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Dialogs.showDialog("Поставка добавлена успешно!");
                } else {
                    Dialogs.showExeptionDialog("code:" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }
    private void closeAddFirstSupplyDialog(){
        Stage stage = (Stage) priceField.getScene().getWindow();
        stage.close();
    }
}

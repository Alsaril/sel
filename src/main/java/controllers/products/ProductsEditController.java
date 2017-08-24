package controllers.products;

import controllers.supply.NewSupplyController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Barcode;
import models.Product;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by andrey on 24.07.17.
 */
public class ProductsEditController {

    Api api = RetrofitClient.getApiService();
    private boolean ok = false;
    private boolean edit = false;
    private Product product = new Product();
    @FXML
    private TextField productName;
    @FXML
    private TextField productLName;
    @FXML
    private TextField productMeasurement;
    @FXML
    private TextField productBarcode;
    @FXML
    private TextField productVendor;
    @FXML
    private TextField productProducer;
    @FXML
    private CheckBox productIntegerOnly;
    @FXML
    private TextField productPrice;
    @FXML
    private Label categoryLable;
    @FXML
    private Label subcategoryLable;
    @FXML
    private TextField productMinCount;

    @FXML
    private void initialize() {

    }


    public void handleAdd(ActionEvent actionEvent) {
        if ((!Objects.equals(productName.getText(), "")) && (!Objects.equals(productPrice.getText(), ""))) {
            Double doublePriceProduct = Double.parseDouble(productPrice.getText());
            Double doubleCountProduct = Double.parseDouble(productMinCount.getText());

            product.setName(productName.getText());
            product.setPrice(doublePriceProduct);
            product.setShortName(productLName.getText());
            product.setUnit(productMeasurement.getText());
            product.setVendor(productVendor.getText());
            product.setBarcode(productBarcode.getText());
            product.setProducer(productProducer.getText());
            product.setInteger(productIntegerOnly.isSelected());
            product.setMinCount(doubleCountProduct);

            if (edit) {
                editProduct(product);
            } else {
                addProduct(product);
            }
        } else {
            Dialogs.showDialog("Введите название и цену!");
        }
    }

    public void handleBarcode(ActionEvent actionEvent) {
        Call<Barcode> call = api.barcode();
        call.enqueue(new Callback<Barcode>() {
            @Override
            public void onResponse(Call<Barcode> call, Response<Barcode> response) {
                if (response.code() == 200) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            productBarcode.setText(response.body().getBarcode());
                        }
                    });
                } else {
                    Dialogs.showExeptionDialog("code:" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Barcode> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }

    public void handlePrint(ActionEvent actionEvent) {

    }

    public void setProduct(Product product) {
        this.product = product;

        productName.setText(product.getName());
        productPrice.setText(product.getPriceFormat());
        productLName.setText(product.getShortName());
        productMeasurement.setText(product.getUnit());
        productVendor.setText(product.getVendor());
        productProducer.setText(product.getProducer());
        productIntegerOnly.setSelected(product.isInteger());
        productBarcode.setText(product.getBarcode());
        productMinCount.setText(product.getMinCountFormat());
        edit = true;
    }

    public boolean isOk() {
        return ok;
    }

    public void handleDel(){
        delProduct(product);

    }

    public void close(){
        Stage stage = (Stage) productName.getScene().getWindow();
        stage.close();
    }
    public void closeDelDialog(){
        //Stage stage = (Stage) delButton.getScene().getWindow();
        //stage.close();
    }

    private void delProduct(Product product) {
        String id = String.valueOf(product.getId());
        Call<Void> call = api.delProduct(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    Dialogs.showDialog("Товар удален успешно!");
                    ok = true;
                    closeDelDialog();
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

    private void addProduct(Product product) {
        Call<Product> call = api.addProduct(product);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.code() == 201) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Dialogs.showDialog("Товар добавлен успешно!");
                            ok = true;
                            addFirstSupply(response.body());
                            close();
                        }
                    });
                } else {
                    Dialogs.showExeptionDialog("code:" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }

    private void editProduct(Product product) {
        String id = String.valueOf(product.getId());
        Call<Void> call = api.editProduct(id, product);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Dialogs.showDialog("Товар изменен успешно!");
                    ok = true;
                    close();
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

    private void addFirstSupply(Product product){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/supply/firstSupply.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Добавление поставки");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);

            NewSupplyController controller = loader.getController();
            controller.setProductToFirstSupply(product);
            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

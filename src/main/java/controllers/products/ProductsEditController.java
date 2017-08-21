package controllers.products;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Category;
import models.Product;
import models.Subcategories;
import models.Subcategory;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;
import network.NetworkHelper;

import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * Created by andrey on 24.07.17.
 */
public class ProductsEditController {

    private ObservableList<Subcategory> subcategoriesOL = FXCollections.observableArrayList();
    private List<Category> categoryList;
    private List<Subcategory> subcategoryList;
    private boolean ok = false;
    private boolean edit = false;
    private Product product;

    Api api = RetrofitClient.getApiService();


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
    private void initialize() {

    }


    public void handleAdd(ActionEvent actionEvent) {
        if ((!Objects.equals(productName.getText(), "")) && (!Objects.equals(productPrice.getText(), ""))) {
            Double floatPriceProduct = Double.parseDouble(productPrice.getText());

            product.setName(productName.getText());
            product.setPrice(floatPriceProduct);
            product.setShortName(productLName.getText());
            product.setUnit(productMeasurement.getText());
            product.setVendor(productVendor.getText());
            product.setProducer(productProducer.getText());
            product.setInteger(productIntegerOnly.isSelected());

            if (edit){

            }else{
                NetworkHelper.addProduct(product);
            }


        } else {
            Dialogs.showDialog("Введите название и цену!");
        }
    }

    public void handleBarcode(ActionEvent actionEvent) {
        Call<String> call = api.newBarcode();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            productBarcode.setText(response.body());
                        }
                    });
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
            public void onFailure(Call<String> call, Throwable t) {
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

    public void handlePrint(ActionEvent actionEvent) {

    }


    public void handleCategory(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../../view/products/SelectCategory.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Выберите категорию");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            SelectCategoryController controller = loader.getController();
            controller.setCategoryAndSubcategory(categoryList, subcategoryList);

            stage.showAndWait();

            if (controller.isOk()) {
                product.setCategory(controller.getCategoryId());
                product.setSubCategory(controller.getSubcategoryId());
                categoryLable.setText(controller.getCategoryId().toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setCategoriesAndSubcategories(List<Category> categories, List<Subcategory> subcategories){
        categoryList = categories;
        subcategoryList = subcategories;
    }

    public void setProduct(Product product){
        this.product = product;

        productName.setText(product.getName());
        productPrice.setText(product.getPriceFormat());
        productLName.setText(product.getShortName());
        productMeasurement.setText(product.getUnit());
        productVendor.setText(product.getVendor());
        productProducer.setText(product.getProducer());
        productIntegerOnly.setSelected(product.isInteger());
    }

    public void setEdit(){
        edit = true;
    }


    public boolean isOk(){
        return ok;
    }



}

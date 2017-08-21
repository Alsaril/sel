package controllers.products;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.*;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;
import network.NetworkHelper;

import java.util.List;
import java.util.Objects;

/**
 * Created by andrey on 26.07.17.
 */
public class SubcategoryEditController {
    private ObservableList<Category> categoriesOL = FXCollections.observableArrayList();
    private List<Category> categoryList;

    Api api = RetrofitClient.getApiService();
    @FXML
    private TextField subcategoryName;
    @FXML
    private ComboBox<Category> categoryCombobox;
    @FXML
    private boolean ok = false;
    private boolean edit = false;

    private void initialize() {

        //setCellValueFactory(new PropertyValueFactory<Product, String>("reserve"));
    }


    public void addSubcategory(ActionEvent actionEvent){



        if ((!Objects.equals(subcategoryName.getText(), ""))) {
            Subcategory subcategory = new Subcategory(subcategoryName.getText());
            subcategory.setCategory(categoryCombobox.getSelectionModel().getSelectedItem().getId());
            if (edit){

            }else{
                NetworkHelper.addSubcategory(subcategory);
            }

        } else {
            Dialogs.showDialog("Введите название!");
        }


    }

    public void setCategories(List<Category> categories){
        categoriesOL = FXCollections.observableArrayList(categories);
        categoryCombobox.setItems(categoriesOL);
    }

    public void setEdit(){
        edit = true;
    }

    public void close(ActionEvent actionEvent){
        Stage stage = (Stage)actionEvent.getSource();
        stage.close();
    }

}

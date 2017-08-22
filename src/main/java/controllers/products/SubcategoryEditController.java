package controllers.products;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    static Api api = RetrofitClient.getApiService();
    public boolean okClicked = false;
    public boolean edit = false;
    public ObservableList<Category> categoriesOL = FXCollections.observableArrayList();
    Subcategory subcategory = new Subcategory();

    @FXML
    private TextField subcategoryName;
    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private Button delButton;

    public void setCategoryList(ObservableList<Category> categories){
        categoriesOL = categories;
        categoryComboBox.setItems(categoriesOL);
    }


    public void close(){
        Stage stage = (Stage) subcategoryName.getScene().getWindow();
        stage.close();
    }
    public void closeDelDialog(){
        Stage stage = (Stage) delButton.getScene().getWindow();
        stage.close();
    }

    public void add(ActionEvent actionEvent){
            subcategory.setName(subcategoryName.getText());
            subcategory.setCategory(categoryComboBox.getSelectionModel().getSelectedItem().getId());
            if (edit){
                editSubcategory(subcategory);
            }else{
                addSubcategory(subcategory);
            }

    }

    public void handleDel(){
        delSubcategory(subcategory);
    }
    public void handleCancel(){
        closeDelDialog();
    }



    public void addSubcategory(Subcategory subcategory) {
        Call<Void> call = api.addSubcategory(subcategory);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Dialogs.showDialog("Подкатегория добавлена успешно!");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            okClicked = true;
                            close();

                        }
                    });
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

    public void editSubcategory(Subcategory subcategory) {
        String id = String.valueOf(subcategory.getId());
        Call<Void> call = api.editSubcategory(id, subcategory);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Dialogs.showDialog("Подкатегория изменена успешно!");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            okClicked = true;
                            close();

                        }
                    });
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

    public void delSubcategory(Subcategory subcategory) {
        String id = String.valueOf(subcategory.getId());
        Call<Void> call = api.delSubcategory(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    Dialogs.showDialog("Подкатегория удалена!");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            okClicked = true;
                            closeDelDialog();

                        }
                    });
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

    public void setSubcategoryToEdit(Subcategory subcategory){
        this.subcategory = subcategory;
        subcategoryName.setText(subcategory.getName());
        categoryComboBox.getSelectionModel().select(subcategory.getId());
        edit = true;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

}

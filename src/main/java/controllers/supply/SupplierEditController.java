package controllers.supply;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Category;
import models.Supplier;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;

import java.util.Objects;

/**
 * Created by andrey on 25.07.17.
 */
public class SupplierEditController {
    static Api api = RetrofitClient.getApiService();
    public boolean okClicked = false;
    public boolean edit = false;
    Supplier supplier = new Supplier();

    @FXML
    private TextField nameField;
    @FXML
    private TextField innField;
    @FXML
    private TextArea urAddressField;
    @FXML
    private TextArea physAddressField;
    @FXML
    private TextArea requisitesField;

    @FXML
    private Button delButton;


    public void close(){
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
    public void closeDelDialog(){
        Stage stage = (Stage) delButton.getScene().getWindow();
        stage.close();
    }

    public void handleOk(ActionEvent actionEvent){
        if (!Objects.equals(nameField.getText(), "")){
            supplier.setName(nameField.getText());
            supplier.setInn(innField.getText());
            supplier.setUrAddress(urAddressField.getText());
            supplier.setPhysAddress(physAddressField.getText());
            supplier.setRequisites(requisitesField.getText());

            if (edit){
                editSupplier(supplier);
            }else{
                addSupplier(supplier);
            }
        }
    }

    public void handleDel(){
        delSupplier(supplier);

    }
    public void handleCancel(){
        closeDelDialog();
    }



    public void addSupplier(Supplier supplier) {
        Call<Void> call = api.addSupplier(supplier);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Dialogs.showDialog("Поставщик добавлен");
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

    public void editSupplier(Supplier supplier){
        String id = String.valueOf(supplier.getId());
        Call<Void> call = api.editSupplier(id, supplier);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Dialogs.showDialog("Поставщик изменен успешно!");
                            okClicked = true;
                            close();

                        }
                    });
                }else{
                    Dialogs.showExeptionDialog("code:" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }
    private void delSupplier(Supplier supplier){
        String id = String.valueOf(supplier.getId());
        Call<Void> call = api.delCategory(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Dialogs.showDialog("Поставщик удален!");
                            okClicked = true;
                            closeDelDialog();

                        }
                    });
                }else{
                    Dialogs.showExeptionDialog("code:" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }

    public void setSupplier(Supplier supplierToEdit){
        supplier = supplierToEdit;
        nameField.setText(supplier.getName());
        innField.setText(supplier.getInn());
        urAddressField.setText(supplier.getUrAddress());
        physAddressField.setText(supplier.getPhysAddress());
        requisitesField.setText(supplier.getRequisites());
        edit = true;
    }

    public boolean isOkClicked() {
        return okClicked;
    }



}

package controllers.products;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Node;
import network.Api;
import network.RetrofitClient;
import utils.Dialogs;

import java.util.Objects;


/**
 * Created by andrey on 21.07.17.
 */
public class CategoryEditController {

    static Api api = RetrofitClient.getApiService();
    public boolean okClicked = false;
    public boolean edit = false;
    Node editableCategory;

    @FXML
    private TextField categoryName;

    @FXML
    private Button delButton;


    public void close() {
        Stage stage = (Stage) categoryName.getScene().getWindow();
        stage.close();
    }

    public void closeDelDialog() {
        Stage stage = (Stage) delButton.getScene().getWindow();
        stage.close();
    }

    public void add(ActionEvent actionEvent) {
        if (edit) {
            if (!Objects.equals(categoryName.getText(), "")) {
                // editableCategory.setName(categoryName.getText());
                //editCategory(editableCategory);
            } else {
                Dialogs.showDialog("Введите имя категории!");
            }
        } else {
            if (!Objects.equals(categoryName.getText(), "")) {
                //addCategory(categoryName.getText());
            } else {
                Dialogs.showDialog("Введите имя категории!");
            }
        }
    }

    public void handleDel() {
        //delCategory(editableCategory);

    }

    public void handleCancel() {
        closeDelDialog();
    }


    /*public void addCategory(String name) {
        Call<Void> call = api.addCategory(new Category(name));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Dialogs.showDialog("Категория добавлена успешно!");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            okClicked = true;
                            close();
                        }
                    });

                } else {
                    Dialogs.showErrorDialog("Не удалось создать категорию!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialogs.showErrorDialog("Ошибка запроса на сервер!");
            }
        });
    }*/

    /*public void editCategory(Category category) {
        String id = String.valueOf(category.getId());
        Call<Void> call = api.editCategory(id, category);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            okClicked = true;
                            close();

                        }
                    });
                } else {
                    Dialogs.showErrorDialog("Не удалось изменить категорию!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }*/

    /*private void delCategory(Category category) {
        String id = String.valueOf(category.getId());
        Call<Void> call = api.delCategory(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            okClicked = true;
                            closeDelDialog();

                        }
                    });
                } else {
                    Dialogs.showErrorDialog("Не удалось удалить категорию!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }*/

    /*public void setCategoryToEdit(Category category) {
        editableCategory = category;
        categoryName.setText(category.getName());
        edit = true;
    }

    public void setCategory(Category category) {
        editableCategory = category;
    }*/

    public boolean isOkClicked() {
        return okClicked;
    }
}

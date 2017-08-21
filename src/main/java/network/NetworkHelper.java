package network;

import javafx.application.Platform;
import models.Category;
import models.Operation;
import models.Product;
import models.Subcategory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;

/**
 * Created by andrey on 22.07.17.
 */
public class NetworkHelper {

    static Api api = RetrofitClient.getApiService();


    public static void addProduct(Product product) {
        Call<Void> call = api.addProduct(product);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Dialogs.showDialog("Товар добавлен успешно!");
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

    public static void addSubcategory(Subcategory subcategory) {
        Call<Void> call = api.addSubcategory(subcategory);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Dialogs.showDialog("Подкатегория добавлена успешно!");
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

    public static void editSubcategory(Subcategory subcategory) {
        String id = String.valueOf(subcategory.getId());
        Call<Void> call = api.editSubcategory(id, subcategory);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Dialogs.showDialog("Подкатегория изменена успешно!");
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

    public static void delSubcategory(Subcategory subcategory) {
        String id = String.valueOf(subcategory.getId());
        Call<Void> call = api.delSubcategory(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Dialogs.showDialog("Подкатегория удалена!");
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
}

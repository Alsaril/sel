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

}

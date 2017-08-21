package network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andrey on 06.08.17.
 */

public class AuthRetrofitClient {

    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl("http://188.225.87.129")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static AuthApi getApiService() {
        return getRetrofitInstance().create(AuthApi.class);
    }

}


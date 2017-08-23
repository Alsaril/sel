package network;


import com.google.gson.*;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.FileHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.MathContext;

public class RetrofitClient {

    private static Retrofit getRetrofitInstance() {

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization", "Token " + FileHelper.readToken().toString())
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);

                // Customize or return the response
                return response;
            }
        }).build();
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(final Double src, final Type typeOfSrc, final JsonSerializationContext context) {
                BigDecimal bigDecimal = new BigDecimal(src, MathContext.DECIMAL64);
                return new JsonPrimitive(bigDecimal);
            }
        });
        Gson gson = gsonBuilder.create();

        return new Retrofit.Builder()
                .baseUrl("http://188.225.87.129")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    //public static Api getApiService() {
    //   return getRetrofitInstance().create(Api.class);
    //}

}
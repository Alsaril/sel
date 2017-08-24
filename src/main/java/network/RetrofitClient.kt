package network


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utils.FileHelper

object RetrofitClient {
    val apiService: Api by lazy {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Authorization", "Token " + FileHelper.readToken().toString())
                    .method(original.method(), original.body())
                    .build()

            chain.proceed(request)
        }.build()
        val gsonBuilder = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
        /*gsonBuilder.registerTypeAdapter(Double::class.java, JsonSerializer<Double> { src, typeOfSrc, context ->
            val bigDecimal = BigDecimal(src!!, MathContext.DECIMAL64)
            JsonPrimitive(bigDecimal)
        })*/
        val gson = gsonBuilder.create()

        Retrofit.Builder()
                .baseUrl("http://188.225.87.129")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(Api::class.java)
    }
}
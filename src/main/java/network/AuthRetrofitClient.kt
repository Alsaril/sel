package network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRetrofitClient {
    private val apiService by lazy {
        Retrofit.Builder()
                .baseUrl("http://188.225.87.129")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi::class.java)
    }
}


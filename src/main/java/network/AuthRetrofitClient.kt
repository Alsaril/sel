package network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRetrofitClient {
    private val retrofitInstance: Retrofit
        get() = Retrofit.Builder()
                .baseUrl("http://188.225.87.129")
                .addConverterFactory(GsonConverterFactory.create())
                .build()


    val apiService: AuthApi
        get() = retrofitInstance.create(AuthApi::class.java)

}


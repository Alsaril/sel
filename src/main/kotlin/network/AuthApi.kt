package network

import models.auth.AuthRequest
import models.auth.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("token/")
    fun auth(@Body request: AuthRequest): Call<Token>
}

package network;

import models.auth.AuthRequest;
import models.auth.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by andrey on 06.08.17.
 */
public interface AuthApi {
    @POST("token/")
    Call<Token> auth(@Body AuthRequest request);
}

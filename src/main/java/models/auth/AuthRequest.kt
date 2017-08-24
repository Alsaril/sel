package models.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthRequest(@SerializedName("username")
                  @Expose
                  var login: String,

                  @SerializedName("password")
                  @Expose
                  var password: String)

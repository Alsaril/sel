package models.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Token(@SerializedName("token")
            @Expose
            val token: String) {
    override fun toString() = token
}

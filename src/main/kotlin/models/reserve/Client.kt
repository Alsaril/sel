package models.reserve

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


public class Client(@SerializedName("id")
                    @Expose
                    val id: Int,

                    @SerializedName("name")
                    @Expose
                    val name: String,

                    @SerializedName("phone")
                    @Expose
                    val phone: String,

                    @SerializedName("address")
                    @Expose
                    val address: String,

                    @SerializedName("comment")
                    @Expose
                    val comment: String) {
    override fun toString() = name
}


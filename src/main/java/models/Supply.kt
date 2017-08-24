package models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Supply(@SerializedName("id")
             @Expose(serialize = false)
             var id: Int,

             @SerializedName("user")
             @Expose
             var user: String,

             @SerializedName("date")
             @Expose
             var date: String,

             @SerializedName("act_num")
             @Expose
             var actNum: String,

             @SerializedName("supplier")
             @Expose
             var supplier: Supplier,

             @SerializedName("positions")
             @Expose
             var positions: List<Position>)

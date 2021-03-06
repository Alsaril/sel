package models.reserve

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReservePositionMin(@SerializedName("id")
                         @Expose(serialize = false)
                         val id: Int = 0,

                         @SerializedName("count")
                         @Expose
                         var count: Double,

                         @SerializedName("price")
                         @Expose
                         var price: Double,

                         @SerializedName("product")
                         @Expose
                         val product: Int)

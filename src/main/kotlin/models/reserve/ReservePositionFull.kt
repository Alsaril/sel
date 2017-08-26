package models.reserve

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import models.Product

class ReservePositionFull(@SerializedName("id")
                          @Expose(serialize = false)
                          val id: Int = 0,

                          @SerializedName("count")
                          @Expose
                          var count: Double,

                          @SerializedName("product")
                          @Expose
                          val product: Product)
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

                          @SerializedName("price")
                          @Expose
                          var price: Double,

                          @SerializedName("product")
                          @Expose
                          val product: Product) {


    fun sum() = price * count
    private fun str(d: Double) =
            if (product.isInteger) {
                d.toInt().toString()
            } else {
                String.format("%.2f", d)
            }

    fun getSumFormat() = String.format("%.2f", sum())
    fun getStrCount() = str(count)
    fun getName() = product.name
    fun getUnit() = product.unit
    fun getPriceFormat() = String.format("%.2f", price)

    fun toMin() = ReservePositionMin(id, count, price, product.id)
}

package models.supply

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import models.Product


public class PositionSupplyFull(@SerializedName("id")
                                @Expose(serialize = false)
                                val id: Int = 0,

                                @SerializedName("count")
                                @Expose
                                var count: Double,

                                @SerializedName("price")
                                @Expose
                                var price: Double,

                                @SerializedName("sell_price")
                                @Expose
                                var sellPrice: Double,

                                @SerializedName("product")
                                @Expose
                                val product: Product) {
    var fullPrice: Double = 0.0




    fun getProductName() = product.name
    fun getProductVendor() = product.vendor
    fun getPriceFormat() = String.format("%.2f", price)
    fun getFullPriceFormat() = String.format("%.2f", fullPrice)
    fun getStrCount() = str(count)
    fun getSellPriceFormat() = String.format("%.2f", sellPrice)
    fun getProductUnit() = product.unit


    private fun str(d: Double) =
            if (product.isInteger) {
                d.toInt().toString()
            } else {
                String.format("%.2f", d)
            }


    fun toMin() = PositionSupplyMin(id, count, price, sellPrice, product.id)
}

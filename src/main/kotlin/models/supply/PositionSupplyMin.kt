package models.supply

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField

class PositionSupplyMin(@SerializedName("id")
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
                        @DatabaseField
                        var sellPrice: Double,

                        @SerializedName("product")
                        @Expose
                        val product: Int)

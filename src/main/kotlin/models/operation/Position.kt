package models.operation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField

class Position(@SerializedName("id")
               @Expose(serialize = false)
               @DatabaseField(id = true)
               val id: Int = 0,

               @SerializedName("count")
               @Expose
               @DatabaseField
               var count: Double,

               @SerializedName("price")
               @Expose
               @DatabaseField
               var price: Double,

               @SerializedName("discount")
               @Expose
               @DatabaseField
               var discount: Double = 0.0,

               @SerializedName("product")
               @Expose
               @DatabaseField
               val product: Int) {

    constructor() : this(0, 0.0, 0.0, 0.0, 0)

    var productName: String = ""
    var unit: String = ""
    var isInteger = false

    @DatabaseField
    private var operation: Int = 0


    fun sum() = (price - discount) * count


    fun setOperation(operation: Operation) {
        this.operation = operation.id
    }

    private fun str(d: Double) =
            if (isInteger) {
                d.toInt().toString()
            } else {
                String.format("%.2f", d)
            }

    fun getSumFormat() = String.format("%.2f", sum())
    fun getStrCount() = str(count)
    fun getPriceFormat() = String.format("%.2f", price)
    fun getDiscountFormat() = String.format("%.2f", discount)
}

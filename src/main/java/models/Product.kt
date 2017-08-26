package models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField

class Product(@SerializedName("id")
              @Expose(serialize = false)
              @DatabaseField(id = true)
              var id: Int = 0,

              @SerializedName("name")
              @Expose
              @DatabaseField(columnName = "name")
              var name: String,

              @SerializedName("short_name")
              @Expose
              @DatabaseField
              var shortName: String,

              @SerializedName("unit")
              @Expose
              @DatabaseField
              var unit: String,

              @SerializedName("integer")
              @Expose
              @DatabaseField
              var isInteger: Boolean,

              @SerializedName("barcode")
              @Expose
              @DatabaseField(columnName = "barcode")
              var barcode: String,

              @SerializedName("vendor")
              @Expose
              @DatabaseField
              var vendor: String,

              @SerializedName("producer")
              @Expose
              @DatabaseField
              var producer: String,

              @SerializedName("price")
              @Expose
              @DatabaseField
              var price: Double,

              @SerializedName("count")
              @Expose
              @DatabaseField
              var count: Double,

              @SerializedName("reserved")
              @Expose
              @DatabaseField
              var reserved: Double,

              @SerializedName("min_count")
              @Expose
              @DatabaseField
              var minCount: Double,

              @SerializedName("parent")
              @Expose
              @DatabaseField(columnName = "sub_category")
              var parent: Int) {

    constructor() : this(0, "", "", "", false, "", "", "", 0.0, 0.0, 0.0, 0.0, 0)

    private fun str(d: Double) =
            if (isInteger) {
                d.toInt().toString()
            } else {
                String.format("%.2f", d)
            }

    fun getStrCount() = str(count)
    fun getStrReserved() = str(reserved)
    fun getStrMinCount() = str(minCount)
    fun getPriceFormat() = String.format("%.2f", price)
}

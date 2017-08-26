package models.supply

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import utils.Utils

class Supply(@SerializedName("id")
             @Expose(serialize = false)
             var id: Int,

             @SerializedName("user")
             @Expose
             var user: String,

             @SerializedName("date")
             @Expose
             var date: String,

             @SerializedName("document")
             @Expose
             var document: String,

             @SerializedName("document_info")
             @Expose
             var documentInfo: String,

             @SerializedName("document_date")
             @Expose
             var documentDate: String,

             @SerializedName("supplier")
             @Expose
             var supplier: Supplier,

             @SerializedName("positions")
             @Expose
             var positions: List<PositionSupplyFull>){

    fun getSupplierName():String{
        return supplier.name
    }

    fun getDateFormat() = Utils.formatDate(date)

}



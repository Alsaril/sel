package models

/**
 * Created by andrey on 22.08.17.
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import models.Position
import models.Supplier

class SupplyMin(@SerializedName("id")
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
             var supplier: Int,

             @SerializedName("positions")
             @Expose
             var positions: List<PositionSupplyMin>)
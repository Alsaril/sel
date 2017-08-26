package models.reserve

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ReserveMin(@SerializedName("id")
                 @Expose(serialize = false)
                 var id: Int,

                 @SerializedName("user")
                 @Expose
                 var user: String,

                 @SerializedName("date")
                 @Expose
                 var date: String,

                 @SerializedName("comment")
                 @Expose
                 var comment: String,

                 @SerializedName("client")
                 @Expose
                 var client: Int,

                 @SerializedName("positions")
                 @Expose
                 var positions: List<ReservePositionMin>)

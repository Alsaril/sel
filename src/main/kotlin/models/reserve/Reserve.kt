package models.reserve

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import models.Client

class Reserve(@SerializedName("id")
              @Expose(serialize = false)
              var id: Int,

              @SerializedName("user")
              @Expose
              var user: String,

              @SerializedName("date")
              @Expose
              var date: String,

              @SerializedName("client")
              @Expose
              var client: Client,

              @SerializedName("positions")
              @Expose
              var positions: List<ReservePositionFull>)
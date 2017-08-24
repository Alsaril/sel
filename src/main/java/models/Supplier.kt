package models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Supplier(@SerializedName("id")
               @Expose
               val id: Int,

               @SerializedName("name")
               @Expose
               val name: String,

               @SerializedName("inn")
               @Expose
               val inn: String,

               @SerializedName("type")
               @Expose
               val type: Int,

               @SerializedName("ur_address")
               @Expose
               val urAddress: String,

               @SerializedName("phys_address")
               @Expose
               val physAddress: String,

               @SerializedName("requisites")
               @Expose
               val requisites: String)

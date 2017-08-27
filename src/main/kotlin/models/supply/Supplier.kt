package models.supply

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Supplier(@SerializedName("id")
               @Expose
               var id: Int,

               @SerializedName("name")
               @Expose
               var name: String,

               @SerializedName("inn")
               @Expose
               var inn: String,

               @SerializedName("type")
               @Expose
               var type: Int,

               @SerializedName("ur_address")
               @Expose
               var urAddress: String,

               @SerializedName("phys_address")
               @Expose
               var physAddress: String,

               @SerializedName("requisites")
               @Expose
               var requisites: String,

               @SerializedName("name1")
               @Expose
               var name1: String,

               @SerializedName("phone1")
               @Expose
               var phone1: String,

               @SerializedName("name2")
               @Expose
               var name2: String,

               @SerializedName("phone2")
               @Expose
               var phone2: String){
    constructor() : this(0,"","",0,"","","","","","","")

}

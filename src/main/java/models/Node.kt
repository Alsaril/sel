package models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField

class Node(@SerializedName("id")
           @Expose
           @DatabaseField(id = true)
           var id: Int,

           @SerializedName("name")
           @Expose
           @DatabaseField
           var name: String,

           @SerializedName("parent")
           @Expose
           @DatabaseField
           var parent: Int?) {
    constructor() : this(0, "", null)
}



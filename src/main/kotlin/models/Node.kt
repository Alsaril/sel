package models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField

class Node(@SerializedName("id")
           @Expose(serialize = false)
           @DatabaseField(id = true)
           var id: Int,

           @SerializedName("name")
           @Expose
           @DatabaseField
           var name: String,

           @SerializedName("parent")
           @Expose
           @DatabaseField
           var parent: Int?) : Comparable<Node> {

    constructor() : this(0, "", null)
    constructor(name: String) : this(-1, name, null)

    override fun toString() = name

    override fun equals(other: Any?): Boolean {
        if (other !is Node) return false
        return id == other.id
    }

    override fun compareTo(other: Node) = name.compareTo(other.name)
}



package models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import models.position.Position
import utils.Utils

class Operation(@SerializedName("id")
                @Expose(serialize = false)
                @DatabaseField(id = true)
                val id: Int = 0,

                @SerializedName("user")
                @DatabaseField
                val user: String = "",

                @SerializedName("date")
                @Expose
                @DatabaseField
                val date: String,

                @SerializedName("type")
                @Expose
                @DatabaseField
                val type: Int,

                @SerializedName("positions")
                @Expose
                var positions: List<Position>) {

    constructor() : this(0, "", "", 0, listOf())

    fun dateFormat() = Utils.formatDate(date)

    fun typeString() = if (type == 1) "Возврат" else "Продажа"

    fun setPositions(positions: List<Position>): Operation {
        this.positions = positions
        return this
    }
}

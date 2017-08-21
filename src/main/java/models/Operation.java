
package models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import utils.Utils;

public class Operation {

    @SerializedName("id")
    @Expose(serialize = false)
    @DatabaseField(id = true)
    private int id;
    @SerializedName("user")
    @Expose
    @DatabaseField
    private String user;
    @SerializedName("date")
    @Expose
    @DatabaseField
    private String date;
    @SerializedName("type")
    @Expose
    @DatabaseField
    private Integer type;
    @SerializedName("positions")
    @Expose
    private List<Position> positions = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public String getDateFormat() {
        return Utils.formatDate(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public String getTypeString(){
        if (type == 1){
            return "Возврат";
        }else{
            return "Продажа";
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Operation setPositions(List<Position> positions) {
        this.positions = positions;
        return this;
    }

}

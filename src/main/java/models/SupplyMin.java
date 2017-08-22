package models;

/**
 * Created by andrey on 22.08.17.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import models.Position;
import models.Supplier;

public class SupplyMin {

    @SerializedName("id")
    @Expose(serialize = false)
    private int id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("act_num")
    @Expose
    private String actNum;
    @SerializedName("supplier")
    @Expose
    private int supplier;
    @SerializedName("positions")
    @Expose
    private List<PositionSupplyMin> positions = null;

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

    public void setDate(String date) {
        this.date = date;
    }

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public List<PositionSupplyMin> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionSupplyMin> positions) {
        this.positions = positions;
    }

}
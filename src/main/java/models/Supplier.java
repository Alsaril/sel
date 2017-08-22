
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Supplier {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("inn")
    @Expose
    private String inn;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("ur_address")
    @Expose
    private String urAddress;
    @SerializedName("phys_address")
    @Expose
    private String physAddress;
    @SerializedName("requisites")
    @Expose
    private String requisites;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrAddress() {
        return urAddress;
    }

    public void setUrAddress(String urAddress) {
        this.urAddress = urAddress;
    }

    public String getPhysAddress() {
        return physAddress;
    }

    public void setPhysAddress(String physAddress) {
        this.physAddress = physAddress;
    }

    public String getRequisites() {
        return requisites;
    }

    public void setRequisites(String requisites) {
        this.requisites = requisites;
    }

    @Override
    public String toString() {
        return name;
    }
}

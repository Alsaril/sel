
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class Node {

    @SerializedName("id")
    @Expose
    @DatabaseField(id = true)
    private int id;
    @SerializedName("name")
    @Expose
    @DatabaseField
    private String name;
    @SerializedName("parent")
    @Expose
    @DatabaseField
    private int parent;

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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

}

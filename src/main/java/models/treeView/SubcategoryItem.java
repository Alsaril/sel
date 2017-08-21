package models.treeView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import models.Subcategory;

/**
 * Created by andrey on 13.08.17.
 */
public class SubcategoryItem {


    private int category;

    private int id;

    private String name;

    private Boolean true_subcategory;

    public SubcategoryItem(String name) {
        this.name = name;

    }
    public SubcategoryItem(int id, String name, Boolean true_subcategory){
        this.id = id;
        this.name = name;
        this.true_subcategory = true_subcategory;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

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

    public Boolean getTrue_subcategory() {
        return true_subcategory;
    }

    public void setTrue_subcategory(Boolean true_subcategory) {
        this.true_subcategory = true_subcategory;
    }

    @Override
    public String toString() {
        return this.name;
    }

}

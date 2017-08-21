
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.math.BigDecimal;

public class Product {

    @SerializedName("id")
    @Expose
    @DatabaseField(id = true)
    private int id;
    @SerializedName("name")
    @Expose
    @DatabaseField(columnName = "name")
    private String name;
    @SerializedName("short_name")
    @Expose
    @DatabaseField
    private String shortName;
    @SerializedName("unit")
    @Expose
    @DatabaseField
    private String unit;
    @SerializedName("integer")
    @Expose
    @DatabaseField
    private boolean integer;
    @SerializedName("barcode")
    @Expose
    @DatabaseField(columnName = "barcode")
    private String barcode;
    @SerializedName("vendor")
    @Expose
    @DatabaseField
    private String vendor;
    @SerializedName("producer")
    @Expose
    @DatabaseField
    private String producer;
    @SerializedName("price")
    @Expose
    @DatabaseField
    private Double price;
    @SerializedName("count")
    @Expose
    @DatabaseField
    private String count;
    @SerializedName("reserved")
    @Expose
    @DatabaseField
    private String reserved;
    @SerializedName("min_count")
    @Expose
    @DatabaseField
    private String minCount;
    @SerializedName("sub_category")
    @Expose
    @DatabaseField(columnName = "sub_category")
    private Integer subCategory;
    @SerializedName("category")
    @Expose
    @DatabaseField(columnName = "category")
    private Integer category;

    public Product(String name, Double price) {
        name = this.name;
        price = this.price;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isInteger() {
        return integer;
    }

    public void setInteger(boolean integer) {
        this.integer = integer;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getMinCount() {
        return minCount;
    }

    public void setMinCount(String minCount) {
        this.minCount = minCount;
    }

    public Integer getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Integer subCategory) {
        this.subCategory = subCategory;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
    //format-----------------------------------------------------------------------------
    public String getPriceFormat(){
        return String.format("%.2f", price);
    }
    //-----------------------------------------------------------------------------------
}

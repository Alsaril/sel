
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.math.BigDecimal;

public class Position {

    @SerializedName("id")
    @Expose(serialize = false)
    @DatabaseField(id = true)
    private int id;
    @SerializedName("count")
    @Expose
    @DatabaseField
    private Double count;
    @SerializedName("price")
    @Expose
    @DatabaseField
    private Double price;
    @SerializedName("discount")
    @Expose
    @DatabaseField
    private Double discount;
    @SerializedName("product")
    @Expose
    @DatabaseField
    private int product;

    @DatabaseField
    private int operation;

    //-----------------
    @Expose(serialize = false, deserialize = false)
    private String productName;
    @Expose(serialize = false, deserialize = false)
    private Double sum;
    @Expose(serialize = false, deserialize = false)
    private String unit;
    //-----------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getCountString(){
        if (count != null) {
            return count.toString();
        }else {
            return null;
        }
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;

    }

    public Double getDiscount() {
        return discount;
    }

    public String getDiscountString(){
        if (discount != null) {
            return discount.toString();
        }else {
            return null;
        }
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public String getPriceFormat(){
        return String.format("%.2f", price);
    }

    public String getSumFormat(){
        return String.format("%.2f", sum);
    }


    //-----------------
    public String getProductName(){
        return productName;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public Double getSum(){
        return sum;
    }
    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    //-----------------

    public void setOperation(Operation operation) {
        this.operation = operation.getId();
    }

}

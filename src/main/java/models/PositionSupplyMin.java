package models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PositionSupplyMin {

    @SerializedName("id")
    @Expose(serialize = false)
    private int id;
    @SerializedName("count")
    @Expose
    private Double count;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("product")
    @Expose
    private int product;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

}

package models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PositionSupplyFull {

    @SerializedName("id")
    @Expose(serialize = false)
    private int id;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("product")
    @Expose
    private Product product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}

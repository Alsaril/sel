package models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductsData(@SerializedName("nodes")
                   @Expose
                   val nodes: List<Node>,

                   @SerializedName("products")
                   @Expose
                   val products: List<Product>)

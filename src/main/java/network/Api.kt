package network

import models.*
import retrofit2.Call
import retrofit2.http.*


interface Api {
    @GET("query")
    fun productsData(): Call<ProductsData>

    @GET("operations")
    fun operations(): Call<List<Operation>>

    @POST("operations/")
    fun addOperation(@Body operation: Operation): Call<Void>

    @GET("suppliers")
    fun suppliers(): Call<List<Supplier>>

    @POST("suppliers/")
    fun addSupplier(@Body supplier: Supplier): Call<Void>

    @PUT("suppliers/{id}/")
    fun editSupplier(@Path("id") id: String, @Body supplier: Supplier): Call<Void>

    @DELETE("suppliers/{id}/")
    fun delSupplier(@Path("id") id: String): Call<Void>

    @GET("barcode")
    fun barcode(): Call<Barcode>

    @POST("products/")
    fun addProduct(@Body product: Product): Call<Product>

    @PUT("products/{id}/")
    fun editProduct(@Path("id") id: String, @Body product: Product): Call<Void>

    @DELETE("products/{id}/")
    fun delProduct(@Path("id") id: String): Call<Void>

    @GET("supplies")
    fun supplies(): Call<List<Supply>>

    @POST("supplies/")
    fun addSupply(@Body supplyMin: SupplyMin): Call<Void>

    @PUT("supplies/{id}/")
    fun editSupply(@Path("id") id: String, @Body supplyMin: SupplyMin): Call<Void>

    @DELETE("supplies/{id}/")
    fun delSupply(@Path("id") id: String): Call<Void>

    @POST("nodes/")
    fun addNode(@Body node: Node): Call<Void>

    @PUT("nodes/{id}/")
    fun editNode(@Path("id") id: String, @Body node: Node): Call<Void>

    @DELETE("nodes/{id}/")
    fun delNode(@Path("id") id: String): Call<Void>
}
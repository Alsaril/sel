package network;

import models.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;


public interface Api {
    @GET("query")
    Call<ProductsData> productsData();

    @GET("operations")
    Call <List<Operation>> operations();

    @POST("operations/")
    Call<Void> addOperation(@Body Operation operation);

    @GET("suppliers")
    Call <List<Supplier>> suppliersData();

    @POST("suppliers/")
    Call<Void> addSupplier(@Body Supplier supplier);

    @PUT("suppliers/{id}/")
    Call<Void> editSupplier(@Path("id") String id, @Body Supplier supplier);

    @DELETE("suppliers/{id}/")
    Call<Void> delSupplier(@Path("id") String id);

    @GET("barcode")
    Call<Barcode> newBarcode();

    @POST("products/")
    Call<Product> addProduct(@Body Product product);

    @PUT("products/{id}/")
    Call<Void> editProduct(@Path("id") String id, @Body Product product);

    @DELETE("products/{id}/")
    Call<Void> delProduct(@Path("id") String id);

    @GET("supplies")
    Call <List<Supply>> suppliesData();

    @POST("supplies/")
    Call<Void> addSupply(@Body SupplyMin supplyMin);

    @PUT("supplies/{id}/")
    Call<Void> editSupply(@Path("id") String id, @Body SupplyMin supplyMin);

    @DELETE("supplies/{id}/")
    Call<Void> delSupply(@Path("id") String id);

}

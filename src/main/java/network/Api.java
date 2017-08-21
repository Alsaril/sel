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

    @GET("supplier/list/")
    Call<Suppliers> suppliersData();

    @GET("barcode/")
    Call<String> newBarcode();

    @POST("products/")
    Call<Void> addProduct(@Body Product product);

    @PUT("products/{id}/")
    Call<Void> editProduct(@Path("id") String id, @Body Product product);

    @DELETE("products/{id}/")
    Call<Void> delProduct(@Path("id") String id);

    @POST("subcategories/")
    Call<Void> addSubcategory(@Body Subcategory subcategory);

    @PUT("subcategories/{id}/")
    Call<Void> editSubcategory(@Path("id") String id, @Body Subcategory subcategory);

    @DELETE("subcategories/{id}/")
    Call<Void> delSubcategory(@Path("id") String id);

    @POST("categories/")
    Call<Void> addCategory(@Body Category category);

    @PUT("categories/{id}/")
    Call<Void> editCategory(@Path("id") String id, @Body Category category);

    @DELETE("categories/{id}/")
    Call<Void> delCategory(@Path("id") String id);

}

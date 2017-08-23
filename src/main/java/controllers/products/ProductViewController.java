package controllers.products;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import models.Category;
import models.Product;
import models.ProductsData;
import models.Subcategory;
import models.treeView.SubcategoryItem;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javafx.event.ActionEvent;
import utils.Data;
import utils.Dialogs;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class ProductViewController {


    Api api = RetrofitClient.getApiService();

    private ObservableList<Product> productsOL = FXCollections.observableArrayList();
    private ObservableList<Product> productsFiltredOL = FXCollections.observableArrayList();
    private List<Product> productsList;
    private List<Category> categoryList;
    private List<Subcategory> subcategoryList;
    private Product selectProduct;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> barcodeColumn;
    @FXML
    private TableColumn<Product, String> measurementColumn;
    @FXML
    private TableColumn<Product, String> countColumn;
    @FXML
    private TableColumn<Product, String> reserveColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;
    @FXML
    private TreeView<SubcategoryItem> categoryTreeView;


    public ProductViewController() {

    }

    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        measurementColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("unit"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("barcode"));
        countColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("count"));
        reserveColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("reserved"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("priceFormat"));

        ContextMenu productTableContextMenu = new ContextMenu();
        MenuItem editProduct = new MenuItem("Редактировать");
        editProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Product item = productTable.getSelectionModel().getSelectedItem();
                editProduct(item);
            }
        });

        MenuItem delProduct = new MenuItem("Удалить");
        delProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Product item = productTable.getSelectionModel().getSelectedItem();
            }
        });
        MenuItem infoProduct = new MenuItem("Информация");
        infoProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Product item = productTable.getSelectionModel().getSelectedItem();
            }
        });
        productTableContextMenu.getItems().setAll(editProduct,delProduct,infoProduct);
        productTable.setContextMenu(productTableContextMenu);


        loadProductsData();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Редактировать");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SubcategoryItem item = categoryTreeView.getSelectionModel().getSelectedItem().getValue();
                if (item.getTrue_subcategory()){
                    editSubcategory(Data.getSubcategoryById(item.getId(), subcategoryList));
                }else{
                    editCategory(Data.getCategoryById(item.getId(), categoryList));
                }

            }
        });

        MenuItem del = new MenuItem("Удалить");
        del.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SubcategoryItem item = categoryTreeView.getSelectionModel().getSelectedItem().getValue();
                if (item.getTrue_subcategory()){

                }else{
                    delCategory(Data.getCategoryById(item.getId(), categoryList));
                }

            }
        });
        contextMenu.getItems().setAll(edit, del);
        categoryTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductsInCategory(newValue.getValue()));
        categoryTreeView.setContextMenu(contextMenu);

    }

    public void showAddCategoryDialog(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/CategoryAddView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Новая категория");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            CategoryEditController controller = loader.getController();
            stage.showAndWait();

            if (controller.isOkClicked()) {
                loadProductsData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editCategory(Category category){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/CategoryAddView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Редактирование категории");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);

            //stage.initOwner();

            CategoryEditController controller = loader.getController();
            controller.setCategoryToEdit(category);
            stage.showAndWait();

            if (controller.isOkClicked()) {
                loadProductsData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delCategory(Category category){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/products/DelCategory.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Удаление категории");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);

            //stage.initOwner();

            CategoryEditController controller = loader.getController();
            controller.setCategory(category);
            stage.showAndWait();

            if (controller.isOkClicked()) {
                loadProductsData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAddProductDialog(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/products/ProductEditView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Новый товар");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            ProductsEditController controller = loader.getController();
            controller.setCategoriesAndSubcategories(categoryList, subcategoryList);

            stage.showAndWait();

            if (controller.isOk()) {
                loadProductsData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editProduct(Product product){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/products/ProductEditView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Редактирование товара");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);

            ProductsEditController controller = loader.getController();
            controller.setCategoriesAndSubcategories(categoryList, subcategoryList);
            controller.setProduct(product);

            stage.showAndWait();

            if (controller.isOk()) {
                loadProductsData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delProduct(){

    }

    public void showAddSubcategoryDialog(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/SubcategoryEditView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Новая подкатегория");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            SubcategoryEditController controller = loader.getController();
            controller.setCategoryList(FXCollections.observableArrayList(categoryList));
            stage.showAndWait();

            if (controller.isOkClicked()) {
                loadProductsData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void editSubcategory(Subcategory subcategory){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/SubcategoryEditView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Редактирование подкатегории");
            stage.setMinHeight(150);
            stage.setMinWidth(400);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);

            SubcategoryEditController controller = loader.getController();
            controller.setSubcategoryToEdit(subcategory);
            controller.setCategoryList(FXCollections.observableArrayList(categoryList));
            stage.showAndWait();


            if (controller.isOkClicked()) {
                loadProductsData();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delSubcategory(){

    }

    private void loadProductsData() {
        Call<ProductsData> call = api.productsData();
        call.enqueue(new Callback<ProductsData>() {
            @Override
            public void onResponse(Call<ProductsData> call, Response<ProductsData> response) {
                if (response.code() == 200) {
                    productsList = response.body().getProducts();
                    categoryList = response.body().getCategories();
                    subcategoryList = response.body().getSubcategories();
                    productsOL = FXCollections.observableArrayList(response.body().getProducts());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            productTable.setItems(productsOL);
                            categoryTreeView.setRoot(Data.categoryTreeViewRootItem(response.body().getCategories(), response.body().getSubcategories()));
                        }
                    });

                } else {
                    Dialogs.showErrorDialog("Ошибка запроса на сервер!");
                }
            }

            @Override
            public void onFailure(Call<ProductsData> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });
    }

    //Тут костыль;)



    private void showProductsInCategory(SubcategoryItem subcategoryItem) {

        productsFiltredOL.clear();
        if (subcategoryItem.getTrue_subcategory()) {
            int subcategoryId = subcategoryItem.getId();
            for (int i = 0; i < productsList.size(); i++) {
                if (productsList.get(i).getSubCategory()!=null) {
                    if (productsList.get(i).getSubCategory() == subcategoryId) {
                        if (productsList.get(i) != null) {
                            productsFiltredOL.add(productsList.get(i));
                        }
                    }
                }
            }
        } else {
            int categoryId = subcategoryItem.getId();
            for (int i = 0; i < productsList.size(); i++) {
                if (productsList.get(i).getCategory()!=null) {
                    if (productsList.get(i).getCategory() == categoryId) {
                        if (productsList.get(i) != null) {
                            productsFiltredOL.add(productsList.get(i));
                        }
                    }
                }
            }
        }
        if (Objects.equals(subcategoryItem.getName(), "Все")) {
            productTable.setItems(productsOL);
        } else {
            productTable.setItems(productsFiltredOL);
        }
    }


    public void selectHandle(){
        selectProduct = productTable.getSelectionModel().getSelectedItem();
        close();
    }

    public Product getSelectProduct(){
        return selectProduct;
    }

    private void close(){
        Stage stage = (Stage) productTable.getScene().getWindow();
        stage.close();
    }






}

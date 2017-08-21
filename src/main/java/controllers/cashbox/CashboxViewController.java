package controllers.cashbox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Operation;
import models.Position;
import models.Product;
import models.ProductsData;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import start.Main;

import java.io.IOException;
import java.util.List;

/**
 * Created by andrey on 24.07.17.
 */
public class CashboxViewController {
    private Main mainApp;

    Api api = RetrofitClient.getApiService();

    private ObservableList<Operation> operationOL = FXCollections.observableArrayList();
    private List<Operation> operationList;
    private List<Product> productList;


    @FXML
    private Label networkStatus;
    @FXML
    private TableView<Operation> operationTable;
    @FXML
    private TableColumn<Operation, String> typeColumn;
    @FXML
    private TableColumn<Operation, String> dateColumn;
    @FXML
    private TableColumn<Operation, String> userColumn;
    @FXML
    private Label totalLable;





    @FXML
    private Label userLable;
    @FXML
    private Label typeLable;
    @FXML
    private Label dateLable;


    @FXML
    private TableView<Position> positionTable;
    @FXML
    private TableColumn<Position, String> nameColumn;
    @FXML
    private TableColumn<Position, Float> priceColumn;
    @FXML
    private TableColumn<Position, String> unitColumn;
    @FXML
    private TableColumn<Position, String> countColumn;
    @FXML
    private TableColumn<Position, String> discountColumn;
    @FXML
    private TableColumn<Position, String> sumColumn;


    @FXML
    private void initialize() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Position, Float>("priceFormat"));
        countColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("countString"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("discountString"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("sumFormat"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("unit"));
        //---------------------------------------------------------------------------------------
        typeColumn.setCellValueFactory(new PropertyValueFactory<Operation, String>("typeString"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Operation, String>("dateFormat"));
        userColumn.setCellValueFactory(new PropertyValueFactory<Operation, String>("user"));
        operationTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showOperation(newValue));
        loadOperationData();
        loadProducts();
        //---------------------------------------------------------------------------------------
    }

    private void loadOperationData() {
        Call<List<Operation>> call = api.operations();
        call.enqueue(new Callback<List<Operation>>() {
            @Override
            public void onResponse(Call<List<Operation>> call, Response<List<Operation>> response) {
                if (response.code() == 200) {
                    operationList = response.body();
                    operationOL = FXCollections.observableArrayList(response.body());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            operationTable.setItems(operationOL);
                            networkStatus.setText("ONLINE");
                        }
                    });

                } else {
                    networkStatus.setText("OFFLINE");
                }
            }

            @Override
            public void onFailure(Call<List<Operation>> call, Throwable t) {
                networkStatus.setText("OFFLINE");
            }
        });
    }

    public void newOperation(ActionEvent actionEvent){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../../view/cashbox/NewCashboxView.fxml"));
            Parent categoryAddFXML = loader.load();
            stage.setTitle("Кассовая операция");
            stage.setMinHeight(600);
            stage.setMinWidth(800);
            stage.setResizable(false);
            stage.setScene(new Scene(categoryAddFXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            NewCashboxController controller = loader.getController();
            stage.showAndWait();

            if (controller.isOkClicked()) {
                loadOperationData();
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void showOperation(Operation operation){
        userLable.setText(operation.getUser());
        typeLable.setText(operation.getTypeString());
        dateLable.setText(operation.getDateFormat());
        Double sum = 0.0;
        List<Position> positions = operation.getPositions();
        for (int i = 0; i < positions.size(); i++){
            Integer id = positions.get(i).getProduct();
            if (getProductById(id)!=null){
                Product product = getProductById(id);
                positions.get(i).setProductName(product.getName());
                setSum(positions.get(i));
                positions.get(i).setUnit(product.getUnit());
                sum = sum + positions.get(i).getSum();
            }
        }
        ObservableList<Position> positionObservableList = FXCollections.observableArrayList(operation.getPositions());
        positionTable.setItems(positionObservableList);
        totalLable.setText(String.format("%.2f", sum));
    }

    private void loadProducts(){
        Call<ProductsData> call = api.productsData();
        call.enqueue(new Callback<ProductsData>() {
            @Override
            public void onResponse(Call<ProductsData> call, Response<ProductsData> response) {
                if (response.code() == 200) {
                    productList = response.body().getProducts();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            networkStatus.setText("ONLINE");
                        }
                    });

                } else {
                    networkStatus.setText("OFFLINE");
                }
            }

            @Override
            public void onFailure(Call<ProductsData> call, Throwable t) {
                networkStatus.setText("OFFLINE");
            }
        });
    }
    private Product getProductById(int id){
        Product product = null;
        for (int i = 0; i < productList.size(); i++){
            if(productList.get(i).getId() == id){
                product = productList.get(i);
            }
        }
        return product;
    }

    private void setSum(Position position){
        if ((position.getCount()!= null)&&(position.getPrice()!=null)&&(position.getDiscount()!=null)) {
            position.setSum((position.getPrice() - position.getDiscount()) * position.getCount());
        }
    }
}

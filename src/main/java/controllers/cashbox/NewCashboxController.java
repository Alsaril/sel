package controllers.cashbox;

import controllers.products.ProductViewController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Operation;
import models.Position;
import models.Product;
import network.Api;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Dialogs;
import utils.Printer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by andrey on 26.07.17.
 */
public class NewCashboxController {

    Api api = RetrofitClient.getApiService();

    ObservableList<Position> positionsOL = FXCollections.observableArrayList();
    List<Position> positionList;
    public boolean okClicked = false;


    @FXML
    private CheckBox returnCheckBox;
    @FXML
    private Label totalSum;
    @FXML
    private TableView<Position> positionTable;
    @FXML
    private TableColumn<Position, String> nameColumn;
    @FXML
    private TableColumn<Position, String> priceColumn;
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
        positionTable.setEditable(true);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("priceFormat"));

        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Position, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Position, String> t) {
                Position position = (Position) t.getTableView().getItems().get(t.getTablePosition().getRow());
                Double price = Double.parseDouble(t.getNewValue());
                position.setPrice(price);
                setSum(position);
                refresh();
            }
        });

        countColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("countString"));
        countColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Position, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Position, String> t) {
                Position position = (Position) t.getTableView().getItems().get(t.getTablePosition().getRow());
                Double count = Double.parseDouble(t.getNewValue());
                position.setCount(count);
                setSum(position);
                refresh();

            }
        });
        discountColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("discountString"));
        discountColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        discountColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Position, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Position, String> t) {
                Position position = (Position) t.getTableView().getItems().get(t.getTablePosition().getRow());
                Double discount = Double.parseDouble(t.getNewValue());
                position.setDiscount(discount);
                setSum(position);
                refresh();

            }
        });
        sumColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("sumFormat"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<Position, String>("unit"));
        showPositionList();
        refresh();
    }

    public void addProduct(ActionEvent actionEvent){
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/cashbox/SelectProductView.fxml"));
            Parent FXML = loader.load();
            stage.setTitle("Выбор товара");
            stage.setResizable(false);
            stage.setScene(new Scene(FXML));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            ProductViewController controller = loader.getController();
            stage.showAndWait();

            Product product = controller.getSelectProduct();
            if (product != null) {
                newPosition(product);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newPosition(Product product){
        Position position = new Position();
        position.setProduct(product.getId());
        position.setProductName(product.getName());
        position.setUnit(product.getUnit());
        position.setPrice(Double.parseDouble(product.getPrice().toString()));
        position.setCount((double) 1);
        position.setDiscount((double)0);
        position.setSum((position.getPrice()-position.getDiscount())*position.getCount());
        positionsOL.addAll(position);
        refresh();
    }

    public void okHandle(){
        Operation operation = new Operation();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        operation.setDate(simpleDateFormat.format(new Date()));


        if (returnCheckBox.isSelected()){
            operation.setType(1);
        }else {
            operation.setType(0);
        }
        if (positionsOL!=null) {
            operation.setPositions(positionsOL);
            addOperation(operation);
        }
    }

    public void addOperation(Operation operation){
        Call<Void> call = api.addOperation(operation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Dialogs.showDialog("Операция проведена успешно!");
                    okClicked = true;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            close();
                        }
                    });
                } else {
                    Dialogs.showExeptionDialog(response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Dialogs.showExeptionDialog(t.getMessage());
            }
        });

    }
    public void printHandle(){



    }

    private void refresh(){
        positionTable.refresh();
        Double sum = 0.0;

        for (int j = 0; j < positionsOL.size(); j++){
            sum = sum + positionsOL.get(j).getSum();
        }
        totalSum.setText(String.format("%.2f", sum));
    }

    private void showPositionList(){
        positionTable.setItems(positionsOL);
    }

    private void close(){
        Stage stage = (Stage) totalSum.getScene().getWindow();
        stage.close();
    }
    private void setSum(Position position){
        if ((position.getCount()!= null)&&(position.getPrice()!=null)&&(position.getDiscount()!=null)) {
            position.setSum((position.getPrice() - position.getDiscount()) * position.getCount());
        }
    }
    public boolean isOkClicked(){
        return okClicked;
    }
}

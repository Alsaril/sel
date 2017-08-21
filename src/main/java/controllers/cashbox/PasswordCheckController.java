package controllers.cashbox;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Position;
import utils.Dialogs;

import java.io.DataInput;
import java.util.Objects;

/**
 * Created by andrey on 15.08.17.
 */
public class PasswordCheckController {
    @FXML
    private PasswordField passField;

    @FXML
    private void initialize() {

    }
    private boolean ok = false;

    public void okHandle(){
        if (Objects.equals(passField.getText(), "0000")){
            ok = true;
            close();
        }else{
            ok = false;
            Dialogs.showErrorDialog("Неверный пароль");
        }
    }
    public boolean isOk(){
        return ok;
    }

    private void close(){
        Stage stage = (Stage) passField.getScene().getWindow();
        stage.close();
    }
}

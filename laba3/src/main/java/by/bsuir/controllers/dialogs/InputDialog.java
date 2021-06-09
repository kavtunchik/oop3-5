package by.bsuir.controllers.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class InputDialog<T> extends Stage implements Initializable {

    @FXML
    private VBox fieldVBox;

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;

    protected final T value;
    private boolean isOk;

    protected InputDialog(T value) {
        this.value = value;
        // Настройка UI
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/InputDialogView.fxml"));
        fxmlLoader.setController(this);
        try {
            GridPane root = fxmlLoader.load();
            Scene scene = new Scene(root);
            setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancelButton.setOnAction(actionEvent -> InputDialog.this.close());
        okButton.setOnAction(actionEvent -> {
            if (isInputValid()) {
                isOk = true;
                apply();
                close();
            }
        });
    }

    public boolean showAndWait(Window parent) {
        initOwner(parent);
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
        return isOk;
    }

    protected void addField(String fieldName, TextField textField) {
        // Настройка метки
        Label label = new Label(fieldName);
        label.setPrefWidth(200);
        // Настройка строки ввода
        textField.setPrefWidth(200);
        // Настройка контейнера
        HBox hBox = new HBox(label, textField);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        // Добавление
        fieldVBox.getChildren().add(hBox);
    }

    protected abstract boolean isInputValid();

    protected abstract void apply();
}

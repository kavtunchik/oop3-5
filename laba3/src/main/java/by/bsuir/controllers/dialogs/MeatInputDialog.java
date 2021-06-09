package by.bsuir.controllers.dialogs;

import by.bsuir.models.Meat;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MeatInputDialog<T extends Meat> extends FoodInputDialog<T> {
    protected final TextField originTextField = new TextField();

    public static boolean isOriginValid(String s) {
        if (s.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Origin: blank text!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public MeatInputDialog(T meat) {
        super(meat);
        // Добавление записей
        addField("Origin", originTextField);
        // Установка начальных значений
        originTextField.setText(meat.getOrigin());
    }

    @Override
    protected boolean isInputValid() {
        if (super.isInputValid()) {
            return isOriginValid(originTextField.getText());
        }
        return false;
    }

    @Override
    protected void apply() {
        super.apply();
        value.setOrigin(originTextField.getText());
    }
}

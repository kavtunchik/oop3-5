package by.bsuir.controllers.dialogs;

import by.bsuir.models.Milk;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MilkInputDialog<T extends Milk> extends FoodInputDialog<T> {
    protected final TextField originTextField = new TextField();

    public static boolean isOriginValid(String s) {
        if (s.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Origin: blank text!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public MilkInputDialog(T milk) {
        super(milk);
        // Добавление записей
        addField("Origin", originTextField);
        // Установка начальных значений
        originTextField.setText(milk.getOrigin());
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

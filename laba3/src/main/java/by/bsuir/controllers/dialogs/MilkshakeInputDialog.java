package by.bsuir.controllers.dialogs;

import by.bsuir.models.Milk;
import by.bsuir.models.Milkshake;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MilkshakeInputDialog<T extends Milkshake> extends FoodInputDialog<T> {
    protected final TextField milkTextField = new TextField();

    public MilkshakeInputDialog(T milkshake) {
        super(milkshake);
        // Добавление записей
        addField("Milk", milkTextField);
        // Установка начальных значений
        milkTextField.setText(milkshake.getMilk().getCalorieContent() + ";" + milkshake.getMilk().getOrigin());
    }

    @Override
    protected boolean isInputValid() {
        if (super.isInputValid()) {
            String[] fields = milkTextField.getText().split(";");
            if (fields.length == 2) {
                return MilkInputDialog.isCalorieContentValid(fields[0]) && MeatInputDialog.isOriginValid(fields[1]);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Milk: wrong format!");
                alert.showAndWait();
            }
        }
        return false;
    }

    @Override
    protected void apply() {
        super.apply();
        String[] fields = milkTextField.getText().split(";");
        Milk milk = new Milk(MilkInputDialog.getCalorieContent(fields[0]), fields[1]);
        value.setMilk(milk);
    }
}

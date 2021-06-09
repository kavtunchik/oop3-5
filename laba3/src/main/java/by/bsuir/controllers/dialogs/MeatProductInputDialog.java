package by.bsuir.controllers.dialogs;

import by.bsuir.models.Meat;
import by.bsuir.models.MeatProduct;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MeatProductInputDialog<T extends MeatProduct> extends FoodInputDialog<T> {
    protected final TextField meatTextField = new TextField();

    public MeatProductInputDialog(T meatProduct) {
        super(meatProduct);
        // Добавление записей
        addField("Meat", meatTextField);
        // Установка начальных значений
        meatTextField.setText(meatProduct.getMeat().getCalorieContent() + ";" + meatProduct.getMeat().getOrigin());
    }

    @Override
    protected boolean isInputValid() {
        if (super.isInputValid()) {
            String[] fields = meatTextField.getText().split(";");
            if (fields.length == 2) {
                return MeatInputDialog.isCalorieContentValid(fields[0]) && MeatInputDialog.isOriginValid(fields[1]);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Meat: wrong format!");
                alert.showAndWait();
            }
        }
        return false;
    }

    @Override
    protected void apply() {
        super.apply();
        String[] fields = meatTextField.getText().split(";");
        Meat meat = new Meat(MeatInputDialog.getCalorieContent(fields[0]), fields[1]);
        value.setMeat(meat);
    }
}

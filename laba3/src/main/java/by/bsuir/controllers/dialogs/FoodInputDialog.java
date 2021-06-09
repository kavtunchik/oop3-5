package by.bsuir.controllers.dialogs;

import by.bsuir.models.Food;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class FoodInputDialog<T extends Food> extends InputDialog<T> {
    protected final TextField calorieContentTextField = new TextField();

    public static boolean isCalorieContentValid(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Calorie content: wrong number format!");
            alert.showAndWait();
        }
        return false;
    }

    public static double getCalorieContent(String s) {
        return Double.parseDouble(s);
    }

    public FoodInputDialog(T food) {
        super(food);
        // Добавление записей
        addField("Calorie content", calorieContentTextField);
        // Установка начальных значений
        calorieContentTextField.setText(String.valueOf(food.getCalorieContent()));
    }

    @Override
    protected boolean isInputValid() {
        return isCalorieContentValid(calorieContentTextField.getText());
    }

    @Override
    protected void apply() {
        value.setCalorieContent(getCalorieContent(calorieContentTextField.getText()));
    }
}

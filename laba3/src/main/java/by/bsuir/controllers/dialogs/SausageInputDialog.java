package by.bsuir.controllers.dialogs;

import by.bsuir.models.Sausage;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class SausageInputDialog<T extends Sausage> extends MeatProductInputDialog<T> {
    protected final TextField typeTextField = new TextField();
    protected final TextField gradeTextField = new TextField();

    public static boolean isTypeValid(String s) {
        if (s.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Type: blank text!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public static boolean isGradeValid(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Grade: wrong number format!");
            alert.showAndWait();
        }
        return false;
    }

    public static int getGrade(String s) {
        return Integer.parseInt(s);
    }

    public SausageInputDialog(T sausage) {
        super(sausage);
        // Добавление записей
        addField("Type", typeTextField);
        addField("Grade", gradeTextField);
        // Установка начальных значений
        typeTextField.setText(sausage.getType());
        gradeTextField.setText(String.valueOf(sausage.getGrade()));
    }

    @Override
    protected boolean isInputValid() {
        if (super.isInputValid()) {
            return isTypeValid(typeTextField.getText()) && isGradeValid(gradeTextField.getText());
        }
        return false;
    }

    @Override
    protected void apply() {
        super.apply();
        value.setType(typeTextField.getText());
        value.setGrade(getGrade(gradeTextField.getText()));
    }
}

package by.bsuir.controllers.actions;

import by.bsuir.models.Food;
import javafx.collections.ObservableList;

public class AddCommand extends ItemCommand {

    public AddCommand(ObservableList<Food> list, Food item) {
        super(list, item);
    }

    @Override
    public void undo() {
        list.remove(item);
    }
}

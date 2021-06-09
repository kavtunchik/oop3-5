package by.bsuir.controllers.actions;

import by.bsuir.models.Food;
import javafx.collections.ObservableList;

public class DeleteCommand extends ItemCommand {
    private final int index;

    public DeleteCommand(ObservableList<Food> list, Food item, int index) {
        super(list, item);
        this.index = index;
    }

    @Override
    public void undo() {
        list.add(index, item);
    }
}

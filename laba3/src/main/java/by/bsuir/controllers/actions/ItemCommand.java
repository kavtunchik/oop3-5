package by.bsuir.controllers.actions;

import by.bsuir.models.Food;
import javafx.collections.ObservableList;

public abstract class ItemCommand implements Command {
    protected final ObservableList<Food> list;
    protected final Food item;

    public ItemCommand(ObservableList<Food> list, Food item) {
        this.list = list;
        this.item = item;
    }
}

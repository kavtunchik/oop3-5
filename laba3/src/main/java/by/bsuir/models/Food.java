package by.bsuir.models;

import by.bsuir.serialization.text.TextSerializable;

import java.io.Serializable;

public class Food implements Serializable, TextSerializable {
    protected double calorieContent;

    public Food() {

    }

    public Food(double calorieContent) {
        this.calorieContent = calorieContent;
    }

    public double getCalorieContent() {
        return calorieContent;
    }

    public void setCalorieContent(double calorieContent) {
        this.calorieContent = calorieContent;
    }

    public final String getId() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return getId() + ": calorie content = " + calorieContent;
    }

    @Override
    public String textSerialize() {
        return getId() + ";" + calorieContent;
    }
}

package by.bsuir.models;

public class Milk extends Drink {
    protected String origin;

    public Milk() {
    }

    public Milk(double calorieContent, String origin) {
        super(calorieContent);
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return super.toString() + "; origin = " + origin;
    }

    @Override
    public String textSerialize() {
        return super.textSerialize() + ";" + origin;
    }
}

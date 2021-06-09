package by.bsuir.models;

public class Meat extends Food {
    protected String origin;

    public Meat() {
    }

    public Meat(double calorieContent, String origin) {
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

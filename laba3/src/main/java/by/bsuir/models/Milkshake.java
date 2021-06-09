package by.bsuir.models;

public class Milkshake extends Drink {
    private Milk milk;

    public Milkshake() {
    }

    public Milkshake(double calorieContent, Milk milk) {
        super(calorieContent);
        this.milk = milk;
    }

    public Milk getMilk() {
        return milk;
    }

    public void setMilk(Milk milk) {
        this.milk = milk;
    }

    @Override
    public String toString() {
        return super.toString() + "; milk = (" + milk + ")";
    }

    @Override
    public String textSerialize() {
        return super.textSerialize() + ";" + milk.textSerialize();
    }
}

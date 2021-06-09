package by.bsuir.models;

public class MeatProduct extends Food {
    private Meat meat;

    public MeatProduct() {
    }

    public MeatProduct(double calorieContent, Meat meat) {
        super(calorieContent);
        this.meat = meat;
    }

    public Meat getMeat() {
        return meat;
    }

    public void setMeat(Meat meat) {
        this.meat = meat;
    }

    @Override
    public String toString() {
        return super.toString() + "; meat = (" + meat + ")";
    }

    @Override
    public String textSerialize() {
        return super.textSerialize() + ";" + meat.textSerialize();
    }
}

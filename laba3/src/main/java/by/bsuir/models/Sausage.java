package by.bsuir.models;

public class Sausage extends MeatProduct {
    private String type;
    private int grade;

    public Sausage() {
    }

    public Sausage(double calorieContent, Meat meat, String type, int grade) {
        super(calorieContent, meat);
        this.type = type;
        this.grade = grade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return super.toString() + "; type = " + type + "; grade = " + grade;
    }

    @Override
    public String textSerialize() {
        return super.textSerialize() + ";" + type + ";" + grade;
    }
}

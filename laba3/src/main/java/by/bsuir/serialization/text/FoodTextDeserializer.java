package by.bsuir.serialization.text;

import by.bsuir.models.*;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class FoodTextDeserializer implements Closeable {
    private final BufferedReader reader;

    public FoodTextDeserializer(BufferedReader reader) {
        this.reader = reader;
    }

    public Food[] read() throws IOException {
        LinkedList<Food> items = new LinkedList<>();
        String line;
        Food food;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            switch (values[0]) {
                case "Food" -> food = readFood(values);
                case "MeatProduct" -> food = readMeatProduct(values);
                case "Sausage" -> food = readSausage(values);
                case "Meat" -> food = readMeat(values);
                case "Milkshake" -> food = readMilkshake(values);
                case "Milk" -> food = readMilk(values);
                default -> throw new RuntimeException("Unknown food ID!");
            }
            items.add(food);
        }
        return items.toArray(new Food[0]);
    }

    private Food readFood(String[] values) {
        return new Food(Double.parseDouble(values[1]));
    }

    private MeatProduct readMeatProduct(String[] values) {
        double calorieContent = Double.parseDouble(values[1]);
        Meat meat = readMeat(Arrays.copyOfRange(values, 2, 5));
        return new MeatProduct(calorieContent, meat);
    }

    private Sausage readSausage(String[] values) {
        double calorieContent = Double.parseDouble(values[1]);
        Meat meat = readMeat(Arrays.copyOfRange(values, 2, 5));
        int grade = Integer.parseInt(values[6]);
        return new Sausage(calorieContent, meat, values[5], grade);
    }

    private Meat readMeat(String[] values) {
        return new Meat(Double.parseDouble(values[1]), values[2]);
    }

    private Milkshake readMilkshake(String[] values) {
        double calorieContent = Double.parseDouble(values[1]);
        Milk milk = readMilk(Arrays.copyOfRange(values, 2, 5));
        return new Milkshake(calorieContent, milk);
    }

    private Milk readMilk(String[] values) {
        return new Milk(Double.parseDouble(values[1]), values[2]);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}

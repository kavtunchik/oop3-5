package by.bsuir.controllers;

import by.bsuir.App;
import by.bsuir.controllers.actions.AddCommand;
import by.bsuir.controllers.actions.Command;
import by.bsuir.controllers.actions.DeleteCommand;
import by.bsuir.controllers.dialogs.*;
import by.bsuir.models.*;
import by.bsuir.plugins.Plugin;
import by.bsuir.serialization.text.FoodTextDeserializer;
import by.bsuir.serialization.text.TextSerializer;
import by.bsuir.serialization.xml.CompressingXmlSerializer;
import by.bsuir.serialization.xml.ObjectXmlSerializer;
import by.bsuir.serialization.xml.XmlSerializer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final Deque<Command> undoDeque = new LinkedList<>();
    private Plugin plugin;

    @FXML
    private MenuItem loadBinaryMenuItem;
    @FXML
    private MenuItem loadXmlMenuItem;
    @FXML
    private MenuItem loadTextMenuItem;

    @FXML
    private MenuItem saveBinaryMenuItem;
    @FXML
    private MenuItem saveXmlMenuItem;
    @FXML
    private MenuItem saveTextMenuItem;

    @FXML
    private MenuItem loadPluginMenuItem;
    @FXML
    private MenuItem removePluginMenuItem;

    @FXML
    private MenuItem addFoodMenuItem;
    @FXML
    private MenuItem addMeatProductMenuItem;
    @FXML
    private MenuItem addSausageMenuItem;
    @FXML
    private MenuItem addMeatMenuItem;
    @FXML
    private MenuItem addMilkshakeMenuItem;
    @FXML
    private MenuItem addMilkMenuItem;

    @FXML
    private MenuItem deleteMenuItem;

    @FXML
    private ListView<Food> foodListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Просто какие-то начальные данные
        foodListView.getItems().add(new Sausage(12, new Meat(32, "pork"), "default", 1));
        // Определение обработчиков
        saveBinaryMenuItem.setOnAction(actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(App.getMainWindow());
                if (file != null) {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    Food[] items = foodListView.getItems().toArray(new Food[0]);
                    // Запись
                    objectOutputStream.writeObject(items);
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        saveXmlMenuItem.setOnAction(actionEvent -> {
            try {
                if (plugin != null) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    XMLEncoder xmlEncoder = new XMLEncoder(outputStream);
                    Food[] items = foodListView.getItems().toArray(new Food[0]);
                    // Запись
                    xmlEncoder.writeObject(items);
                    xmlEncoder.close();
                    // Дополнительное преобразование
                    String output = plugin.saveTransform(outputStream.toString());
                    // Запись в файл
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(App.getMainWindow());
                    if (file != null) {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                        writer.write(output);
                        writer.close();
                    }
                } else {
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(App.getMainWindow());
                    if (file != null) {
                        Food[] items = foodListView.getItems().toArray(new Food[0]);
                        // Сериализация
                        XmlSerializer xmlSerializer = new ObjectXmlSerializer();
                        byte[] bytes = xmlSerializer.xmlSerialize(items);
                        // Запись
                        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                        outputStream.write(bytes);
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        saveTextMenuItem.setOnAction(actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(App.getMainWindow());
                if (file != null) {
                    TextSerializer textSerializer = new TextSerializer(new FileWriter(file));
                    Food[] items = foodListView.getItems().toArray(new Food[0]);
                    // Запись
                    textSerializer.write(items);
                    textSerializer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loadBinaryMenuItem.setOnAction(actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(App.getMainWindow());
                if (file != null) {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    // Чтение
                    Food[] items = (Food[]) objectInputStream.readObject();
                    objectInputStream.close();
                    // Обновлени UI
                    foodListView.setItems(FXCollections.observableArrayList(items));
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        loadXmlMenuItem.setOnAction(actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(App.getMainWindow());
                if (file != null) {
                    if (plugin != null) {
                        // Чтение всех байт
                        byte[] bytes = Files.readAllBytes(file.toPath());
                        // Преобразоваие исходного содержимого в XML
                        String input = plugin.loadTransform(new String(bytes, StandardCharsets.UTF_8));
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
                        XMLDecoder xmlDecoder = new XMLDecoder(inputStream);
                        // Чтение
                        Food[] items = (Food[]) xmlDecoder.readObject();
                        xmlDecoder.close();
                        // Обновлени UI
                        foodListView.setItems(FXCollections.observableArrayList(items));
                    } else {
                        XMLDecoder xmlDecoder = new XMLDecoder(new FileInputStream(file));
                        // Чтение
                        Food[] items = (Food[]) xmlDecoder.readObject();
                        xmlDecoder.close();
                        // Обновлени UI
                        foodListView.setItems(FXCollections.observableArrayList(items));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loadTextMenuItem.setOnAction(actionEvent -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(App.getMainWindow());
                if (file != null) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    FoodTextDeserializer textDeserialization = new FoodTextDeserializer(bufferedReader);
                    // Чтение
                    Food[] items = textDeserialization.read();
                    textDeserialization.close();
                    // Обновлени UI
                    foodListView.setItems(FXCollections.observableArrayList(items));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loadPluginMenuItem.setOnAction(this::loadPluginMenuItemActionHandler);
        removePluginMenuItem.setOnAction(this::removePluginMenuItemActionHandler);
        addFoodMenuItem.setOnAction(actionEvent -> {
            Food item = new Food(0);
            FoodInputDialog<Food> inputDialog = new FoodInputDialog<>(item);
            addItem(inputDialog, item);
        });
        addMeatProductMenuItem.setOnAction(actionEvent -> {
            MeatProduct item = new MeatProduct(0, new Meat(0, ""));
            MeatProductInputDialog<MeatProduct> inputDialog = new MeatProductInputDialog<>(item);
            addItem(inputDialog, item);
        });
        addSausageMenuItem.setOnAction(actionEvent -> {
            Sausage item = new Sausage(0, new Meat(0, ""), "", 0);
            SausageInputDialog<Sausage> inputDialog = new SausageInputDialog<>(item);
            addItem(inputDialog, item);
        });
        addMeatMenuItem.setOnAction(actionEvent -> {
            Meat item = new Meat(0, "");
            MeatInputDialog<Meat> inputDialog = new MeatInputDialog<>(item);
            addItem(inputDialog, item);
        });
        addMilkshakeMenuItem.setOnAction(actionEvent -> {
            Milkshake item = new Milkshake(0, new Milk(0, ""));
            MilkshakeInputDialog<Milkshake> inputDialog = new MilkshakeInputDialog<>(item);
            addItem(inputDialog, item);
        });
        addMilkMenuItem.setOnAction(actionEvent -> {
            Milk item = new Milk(0, "");
            MilkInputDialog<Milk> inputDialog = new MilkInputDialog<>(item);
            addItem(inputDialog, item);
        });
        deleteMenuItem.setOnAction(actionEvent -> {
            int index = foodListView.getSelectionModel().getSelectedIndex();
            if (index != -1) {
                Food item = foodListView.getItems().remove(index);
                undoDeque.add(new DeleteCommand(foodListView.getItems(), item, index));
            }
        });
        foodListView.setOnMouseClicked(this::foodListViewClickHandler);
    }

    public void setupShortcuts(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if ((event.getCode() == KeyCode.Z) && event.isControlDown()) {
                Command command = undoDeque.pollLast();
                if (command != null) {
                    command.undo();
                }
            }
        });
    }

    private void loadPluginMenuItemActionHandler(ActionEvent event) {
        try {
            // Загрузка
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PluginManagerView.fxml"));
            Parent parent = fxmlLoader.load();
            PluginManagerController pluginManagerController = fxmlLoader.getController();
            // Создание модального окна
            Scene scene = new Scene(parent);
            Stage modalStage = new Stage();
            // Настройка модального окна
            modalStage.setScene(scene);
            modalStage.initOwner(App.getMainWindow());
            modalStage.initModality(Modality.APPLICATION_MODAL);
            // Показ окна
            modalStage.showAndWait();
            // Получение плагина
            plugin = pluginManagerController.getPlugin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removePluginMenuItemActionHandler(ActionEvent event) {
        plugin = null;
    }

    private void foodListViewClickHandler(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && (event.getClickCount() == 2)) {
            int index = foodListView.getSelectionModel().getSelectedIndex();
            Food item = foodListView.getSelectionModel().getSelectedItem();
            listItemDoubleClickHandler(index, item);
        }
    }

    private void addItem(InputDialog<?> inputDialog, Food item) {
        if (inputDialog.showAndWait(App.getMainWindow())) {
            foodListView.getItems().add(item);
            undoDeque.add(new AddCommand(foodListView.getItems(), item));
        }
    }

    private void listItemDoubleClickHandler(int index, Food item) {
        // Получение диалога
        InputDialog<?> inputDialog;
        switch (item.getId()) {
            case "Food" -> inputDialog = new FoodInputDialog<>(item);
            case "MeatProduct" -> inputDialog = new MeatProductInputDialog<>((MeatProduct) item);
            case "Sausage" -> inputDialog = new SausageInputDialog<>((Sausage) item);
            case "Meat" -> inputDialog = new MeatInputDialog<>((Meat) item);
            case "Milkshake" -> inputDialog = new MilkshakeInputDialog<>((Milkshake) item);
            case "Milk" -> inputDialog = new MilkInputDialog<>((Milk) item);
            default -> throw new RuntimeException("Unknown item ID!");
        }
        // Показ диалога
        if (inputDialog.showAndWait(App.getMainWindow())) {
            // Обновление UI
            foodListView.getItems().set(index, item);
        }
    }
}

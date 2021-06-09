package by.bsuir.controllers;

import by.bsuir.plugins.Plugin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class PluginManagerController implements Initializable {
    private File dir;
    private Plugin plugin;

    @FXML
    private Button selectDirectoryButton;

    @FXML
    private ListView<Path> pluginListView;

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        okButton.disableProperty().bind(pluginListView.getSelectionModel().selectedItemProperty().isNull());
        // Установка обработчиков
        selectDirectoryButton.setOnAction(this::selectDirectoryButtonActionHandler);
        okButton.setOnAction(this::okButtonActionHandler);
        cancelButton.setOnAction(this::cancelButtonActionEvent);
    }

    private void selectDirectoryButtonActionHandler(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        dir = directoryChooser.showDialog(getOwnerWindow(event));
        if (dir != null) {
            // Удаление старых и занесение новых элементов списка
            pluginListView.getItems().clear();
            scanDirectory(dir);
        }
    }

    private void scanDirectory(File dir) {
        try {
            Stream<Path> stream = Files.list(dir.toPath());
            stream.forEach(path -> {
                File file = path.toFile();
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else if (path.toString().endsWith(".class")) {
                    pluginListView.getItems().add(path);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cancelButtonActionEvent(ActionEvent event) {
        closeWindow(event);
    }

    private void okButtonActionHandler(ActionEvent event) {
        try {
            Path path = pluginListView.getSelectionModel().getSelectedItem();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{dir.toURI().toURL()});
            // Получение classpath
            String temp = path.toString().substring(dir.toPath().toString().length() + 1);
            String name = temp.replace(File.separatorChar, '.');
            name = name.substring(0, name.length() - 6);
            // Загрузка класса
            Class<?> pluginClass = classLoader.loadClass(name);
            // Создание экземпляра плагина
            Object o = pluginClass.getConstructor().newInstance();
            // Проверка имплементации интерфейса Plugin
            if (o instanceof Plugin) {
                plugin = (Plugin) o;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Selected class doesn't implement Plugin interface!");
                alert.showAndWait();
                return;
            }
            // Закрытие окна
            closeWindow(event);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't create instance of selected class!");
            alert.showAndWait();
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow(ActionEvent event) {
        getOwnerWindow(event).close();
    }

    private Stage getOwnerWindow(ActionEvent event) {
        return ((Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    public Plugin getPlugin() {
        return plugin;
    }
}

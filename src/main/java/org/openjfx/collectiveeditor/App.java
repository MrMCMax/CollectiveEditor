package org.openjfx.collectiveeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.BiConsumer;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import org.openjfx.collectiveeditor.gui.PrimaryController;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("FXML1"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Abre una nueva ventana con el URL indicado, con tamaño de ventana y
     * título
     *
     * @param <C> Tipo de controlador
     * @param path ruta
     * @param width anchura de la ventana
     * @param height altura de la ventana
     * @param title título de la ventana
     * @return el controlador creado
     */
    public static <C extends Initializable> C openModalController(URL path, String title) {
        return openModalController(path, -1, -1, title, false);
    }

    /**
     * Abre una nueva ventana con el URL indicado, con tamaño de ventana y
     * título
     *
     * @param <C> Tipo de controlador
     * @param path ruta
     * @param width anchura de la ventana
     * @param height altura de la ventana
     * @param title título de la ventana
     * @return el controlador creado
     */
    public static <C extends Initializable> C openModalController(URL path,
            double width, double height, String title) {
        return openModalController(path, width, height, title, false);
    }

    /**
     * Abre una nueva ventana con el URL indicado, con tamaño de ventana, título
     * y posibilidad de fijar el tamaño
     *
     * @param <C> Tipo de controlador
     * @param path ruta
     * @param width anchura de la ventana
     * @param height altura de la ventana
     * @param title título de la ventana
     * @param fixedSize tamaño fijo o no
     * @return el controlador creado
     */
    public static <C extends Initializable> C openModalController(URL path,
            double width, double height, String title, boolean fixedSize) {
        return openModalController(path, width, height, title, fixedSize, null, null);
    }

    /**
     * Abre una nueva ventana con el URL indicado, con tamaño de ventana, título
     * y operación de inicialización
     *
     * @param <C> Tipo de controlador
     * @param <O> Tipo de datos para el controlador
     * @param path ruta
     * @param width anchura de la ventana
     * @param height altura de la ventana
     * @param title título de la ventana
     * @param fixedSize tamaño fijo o no
     * @param function operación de inicialización
     * @param data datos de inicialización
     * @return el controlador creado
     */
    public static <C extends Initializable, O> C openModalController(URL path,
            double width, double height, String title,
            boolean fixedSize, BiConsumer<C, O> function, O data) {
        try {
            FXMLLoader cargador = new FXMLLoader(path);
            Parent root = (Parent) cargador.load();
            C documentController = cargador.<C>getController();
            if (function != null) {
                function.accept(documentController, data);
            }
            Stage stage = new Stage();
            Scene scene = null;
            if (width < 0) {
                scene = new Scene(root);
            } else {
                scene = new Scene(root, width, height);
            }
            if (fixedSize) {
                stage.setMaxHeight(height);
                stage.setMaxWidth(width);
            }
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            return documentController;
        } catch (IOException ex) {
            System.err.println("Error abriendo el FXML");
            return null;
        }
    }

}

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;


import java.util.logging.Level;
import java.util.logging.Logger;



public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/GUI/mainwindow.fxml"));
            JMetro jMetro = new JMetro(Style.DARK);
            primaryStage.setTitle("Wyszukiwarka SciMagor Resurchify");
            root.getStyleClass().add(JMetroStyleClass.BACKGROUND);
            Scene scene = new Scene(root, 1200, 800);
            jMetro.setScene(scene);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING,e.getMessage());
        }
    }



    public static void main(String[] args) {
        launch(args);
    }

}

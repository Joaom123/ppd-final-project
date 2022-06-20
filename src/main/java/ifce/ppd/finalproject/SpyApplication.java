package ifce.ppd.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SpyApplication extends Application {
    public static void main(String[] args) {
        System.setProperty("java.security.policy","/home/joaomarcus/Projetos/final-project/src/main/java/ifce/ppd/finalproject/spaces/all.policy");
        System.setSecurityManager(new SecurityManager());
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SpyApplication.class.getResource("spy-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 900);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
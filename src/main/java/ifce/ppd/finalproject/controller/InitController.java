package ifce.ppd.finalproject.controller;

import ifce.ppd.finalproject.HelloApplication;
import ifce.ppd.finalproject.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class InitController {
    @FXML
    public TextField userName;

    @FXML
    public Text usernameFieldErrorMessage;

    @FXML
    public void enter(ActionEvent actionEvent) throws IOException {
        if(userName.getText() == null || userName.getText().trim().isEmpty()) {
            usernameFieldErrorMessage.setText("O nome de usuário é obrigatório");
            return;
        }

        usernameFieldErrorMessage.setText("");

        // Create User
        User user = new User(UUID.randomUUID(), userName.getText());

        // Go to choose room view
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-view.fxml"));
        fxmlLoader.setController(new ChatController(user));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        actualStage.setScene(scene);
        actualStage.show();
    }
}

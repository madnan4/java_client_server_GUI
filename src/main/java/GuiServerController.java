import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiServerController {

    @FXML
    private Button serverButton;

    @FXML
    private Button clientButton;






    @FXML
    public void initialize() {
        serverButton.setStyle("-fx-pref-width: 300px");
        serverButton.setStyle("-fx-pref-height: 300px");
        serverButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("server.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("Server.css").toExternalForm());
                Stage stage = (Stage) serverButton.getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        clientButton.setStyle("-fx-pref-width: 300px");
        clientButton.setStyle("-fx-pref-height: 300px");
        clientButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Client.fxml"));
                Parent root = loader.load();
                ClientController clientController = loader.getController();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("Client.css").toExternalForm());
                Stage stage = (Stage) clientButton.getScene().getWindow();
                stage.setScene(scene);
                stage.setOnCloseRequest(event -> {
                    clientController.closeConnection();
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}

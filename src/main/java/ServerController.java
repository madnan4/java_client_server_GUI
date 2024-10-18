import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ServerController
{
    @FXML
    private ListView<String> listItems;
    private Server serverConnection;
    public void initialize()
    {
        serverConnection = new Server(data -> {
            Platform.runLater(()->{
                listItems.getItems().add(data.toString());
            });

        });
    }
}

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Objects;

public class ClientController
{
    public Label Instructions;
    Client clientConnection;
    ArrayList<Integer> receiveMessage = new ArrayList<>();
    @FXML
    ListView<String> listItems2;
    @FXML
    ListView<String> textArea = new ListView<>();
    @FXML
    TextField c1,c2;
    @FXML
    Button Send;
    public void initialize()
    {
//        textArea.setDisable(true);
        clientConnection = new Client(data->{
            Platform.runLater(()->{listItems2.getItems().add(data.toString());
                transfer(clientConnection.getNums());
            });
        });
        clientConnection.start();
    }
    @FXML
    public void receivers()
    {
        receiveMessage.clear();
        String text = c2.getText();
        System.out.println(text);
        String message = c1.getText();
        if(Objects.equals(text.toLowerCase(), "all"))
        {
            for(int num : GuiServer.clientNumber)
            {
                System.out.println(num);
                receiveMessage.add(num);
            }
            send(receiveMessage,message);
        }
        else
        {
            String[] inputValues = text.split(",");
            for (String value : inputValues) {
                receiveMessage.add(Integer.parseInt(value));
            }
            send(receiveMessage,message);
        }
    }
    public void send(ArrayList<Integer> nums, String message)
    {
        for(int num:nums)
        {
            clientConnection.sendMultiple(message,num);
        }
        System.out.println();
    }
    public void transfer(ArrayList<Integer> clientNumber)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Clients connected:\n");
        for (int num : clientNumber) {
            sb.append("Client: ").append(num). append('\n');
        }
        String x = sb.toString();
        textArea.getItems().clear();
        textArea.getItems().add(x);
    }

    public void closeConnection()
    {
        Platform.exit();
        System.exit(0);
    }
}

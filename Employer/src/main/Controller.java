package main;

import JMS.ClientAppGateway;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button btSend;

    @FXML
    private TextField tfMessage;

    private ClientAppGateway clientAppGateway;


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        clientAppGateway = new ClientAppGateway() {
            public void onMediatorReply(TextMessage message) {
                if (message instanceof TextMessage) {
//                    try {
//                        //bericht ontvangen
//                    } catch (JMSException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        };

        btSend.setOnMouseClicked((event) -> sendMessage(tfMessage.getText()));
    }

    private void sendMessage(String message) {
        clientAppGateway.sendMessage(message);
    }
}
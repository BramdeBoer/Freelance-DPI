package main;

import JMS.ClientAppGateway;
import JMS.FreelancerAppGateway;
import com.google.gson.Gson;
import domain.Project;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML //  fx:id="lbMessage"
    private Label lbMessage; // Value injected by FXMLLoader

    private ClientAppGateway clientAppGateway;


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert lbMessage != null : "fx:id=\"lbMessage\" was not injected: check your FXML file 'main.fxml'.";
        /*clientAppGateway = new ClientAppGateway() {
            @Override
            public void onEmployerRequest(TextMessage message) {
                if (message instanceof TextMessage) {
                    setLastMessage(message);
                }
            }
        };*/
    }

    public void setLastMessage(final Message message){
        Gson gson = new Gson();
        try {
            System.out.println(message.getJMSCorrelationID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    lbMessage.setText(((TextMessage)message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


package main;

import JMS.ClientAppGateway;
import JMS.FreelancerAppGateway;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class Main {

    public static void main(String[] args) {
        Communication communication = new Communication();
        communication.startListeners();
    }
}

package main;

import JMS.ClientAppGateway;
import com.google.gson.Gson;
import domain.FreelanceReply;
import domain.Project;
import domain.Sector;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    ClientAppGateway clientAppGateway;

    @FXML
    private ComboBox cbSector;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfRate;
    @FXML
    private TextArea taSkillset;
    @FXML
    private Label lbFeedback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSector.getItems().setAll(Sector.values());

        cbSector.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                createGateway((Sector)newValue);
            }
        });
    }

    private void createGateway(Sector sector) {
        clientAppGateway = new ClientAppGateway(sector) {
            @Override
            public void onMediatorRequest(TextMessage message) {
                try {
                    Gson gson = new Gson();
                    System.out.println(message.getText());
                    createProjectDialog(gson.fromJson(message.getText(), Project.class), message.getJMSCorrelationID(), message.getLongProperty("replyDeadline"));
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void createProjectDialog(Project project, String correlationID, long projectReplyDeadline) {
        System.out.println(projectReplyDeadline);

        if (JOptionPane.showConfirmDialog(null, project.toString(), "Accept this project?",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (!hasProjectReplyDeadlineExpired(projectReplyDeadline)) {
                Gson gson = new Gson();
                FreelanceReply freelanceReply = new FreelanceReply(tfName.getText(),
                        taSkillset.getText().split(","),
                        Double.valueOf(tfRate.getText()),
                        true,
                        project);

                clientAppGateway.sendMessage(gson.toJson(freelanceReply), correlationID);
                setFeedbackMessage("Project accepted");
            } else {
                setFeedbackMessage("Project deadline reply time expired");
            }
        } else {
            setFeedbackMessage("Project declined");
        }
    }

    public boolean hasProjectReplyDeadlineExpired(long deadlineMillis) {
        return System.currentTimeMillis() > deadlineMillis;
    }

    public void setFeedbackMessage(final String errorMessage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lbFeedback.setText(errorMessage);
            }
        });
    }

}

package main;

import JMS.ClientAppGateway;
import com.google.gson.Gson;
import domain.Project;
import domain.ProjectCandidate;
import domain.Sector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.net.URL;
import java.time.ZoneId;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private Button btSend;

    @FXML
    private TextField tfDescription;

    @FXML
    private ComboBox cbSector;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private DatePicker dpEndDate;

    @FXML
    private TextArea taSkillsets;

    @FXML
    private ListView<String> lvReplies;

    private ClientAppGateway clientAppGateway;
    private Gson gson;
    ObservableList<String> items = FXCollections.observableArrayList();

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        gson = new Gson();
        cbSector.getItems().setAll(Sector.values());

        clientAppGateway = new ClientAppGateway() {
            public void onMediatorReply(TextMessage message) {
                if (message instanceof TextMessage) {
                    try {
                        items.add("Project: " + tfDescription.getText());
                        ProjectCandidate[] candidates = gson.fromJson(message.getText(), ProjectCandidate[].class);
                        for (ProjectCandidate candidate : candidates) {
                            items.add(candidate.toString());
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    lvReplies.setItems(items);
                }
            }
        };

        btSend.setOnMouseClicked((event) -> {
            ZoneId zoneId = ZoneId.systemDefault();
            Calendar calendar = Calendar.getInstance();
            calendar.set(dpStartDate.getValue().getYear(), dpStartDate.getValue().getMonth().getValue() - 1, dpStartDate.getValue().getDayOfMonth());
            long startDate = calendar.getTimeInMillis();
            calendar.set(dpEndDate.getValue().getYear(), dpEndDate.getValue().getMonth().getValue() - 1, dpEndDate.getValue().getDayOfMonth());
            long endDate = calendar.getTimeInMillis();

            Project project = new Project(Sector.valueOf(cbSector.getSelectionModel().getSelectedItem().toString()),
                    tfDescription.getText(),
                    startDate,
                    endDate,
                    taSkillsets.getText().split(","));

            sendMessage(project);
        });
    }

    private void sendMessage(Project newProject) {
        clientAppGateway.sendMessage(gson.toJson(newProject));
    }
}
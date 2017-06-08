package main;

import JMS.ClientAppGateway;
import JMS.FreelancerAppGateway;
import com.google.gson.Gson;
import domain.FreelanceReply;
import domain.Project;
import domain.ProjectCandidate;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.*;

/**
 * Created by bramd on 31-5-2017.
 */
public class Communication {

    private ClientAppGateway clientAppGateway;
    private FreelancerAppGateway freelancerAppGateway;

    private HashMap<String, ArrayList> projectReplies;

    private Gson gson;

    private final long TIME_TO_WAIT_FOR_REPLY = 1000 * 10;

    public Communication() {
        projectReplies = new HashMap<String, ArrayList>();
        gson = new Gson();
    }

    public void startListeners(){
        clientAppGateway = new ClientAppGateway() {
            @Override
            public void onEmployerRequest(TextMessage message) {
                try {
                    System.out.println(message.getText());
                    final String messageId = message.getJMSMessageID();
                    freelancerAppGateway.sendMessage(message.getText(), message.getJMSMessageID(), System.currentTimeMillis() + TIME_TO_WAIT_FOR_REPLY);

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            sendFreelanceRepliesToEmployer(messageId);
                        }
                    }, TIME_TO_WAIT_FOR_REPLY);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };

        freelancerAppGateway = new FreelancerAppGateway() {
            @Override
            public void onFreelancerReply(TextMessage message) {
                try {
                    System.out.println(message.getText());

                    String correlationId = message.getJMSCorrelationID();

                    ArrayList<FreelanceReply> replies = new ArrayList<>();
                    if(projectReplies.get(correlationId) != null){
                        replies =  projectReplies.get(correlationId);
                    }
                    replies.add(gson.fromJson(message.getText(), FreelanceReply.class));
                    projectReplies.put(correlationId, replies);
                    System.out.println("Amount of replies for: " + correlationId + " : " + projectReplies.get(correlationId).size());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void sendFreelanceRepliesToEmployer(String messageId) {
        List<FreelanceReply> replies = this.projectReplies.get(messageId);
        System.out.println("SEND REPLIES TO EMPLLOYER " + messageId);

        List<ProjectCandidate> bestCandidates = calculateBestCandidates(replies.get(0).getProject(), replies);

        clientAppGateway.sendMessage(gson.toJson(bestCandidates));
    }

    public List<ProjectCandidate> calculateBestCandidates(Project project, List<FreelanceReply> freelanceReplies) {
        List<ProjectCandidate> candidates = new ArrayList<>();
        if (freelanceReplies == null) {
            return candidates;
        } else {
            List<String> neededSkills = Arrays.asList(project.getNeededSkillsets());

            for (FreelanceReply reply : freelanceReplies) {
                int requiredSkillAmount = calcAmountOfRequiredSkills(neededSkills, Arrays.asList(reply.getSkillset()));
                if (requiredSkillAmount != 0) {
                    candidates.add(new ProjectCandidate(reply, requiredSkillAmount));
                }
            }
            candidates.forEach(candidate -> System.out.println("Before list sorting: " + candidate.getRequiredSkillAmount() + " rate: " + candidate.getFreelanceReply().getRate()));
            Collections.sort(candidates);
            candidates.forEach(candidate -> System.out.println("After list sorting: " + candidate.getRequiredSkillAmount() + " rate: " + candidate.getFreelanceReply().getRate()));

            return candidates;
        }
    }

    public int calcAmountOfRequiredSkills(List<String> requiredSkills, List<String> freelancerSkills) {
        int amount = 0;
        for (String requiredSkill : requiredSkills) {
            if (freelancerSkills.contains(requiredSkill)) {
                amount += 1;
            }
        }
        return amount;
    }
}

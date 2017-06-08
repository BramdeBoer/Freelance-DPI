package JMS;

import com.google.gson.Gson;
import domain.Project;
import domain.Sector;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public abstract class FreelancerAppGateway {
	private MessageSenderGateway sender;
	private MessageReceiverGateway receiver;

	public FreelancerAppGateway() {
		sender = new MessageSenderGateway("mediator.client.send");
		receiver = new MessageReceiverGateway("freelancer.client.send");

		receiver.setListener(new MessageListener() {
			public void onMessage(Message message) {
				System.out.println("Mediator from freelancer @ClientAppGateway Received message");
				onFreelancerReply((TextMessage)message);
			}
		});
	}

	public abstract void onFreelancerReply(TextMessage message);

	public void sendMessage(String text, String correlationID, long replyDeadline) {
		Gson gson = new Gson();
		Project project = gson.fromJson(text, Project.class);
		System.out.println(project.toString());
		MessageSenderGateway sectorSender = new MessageSenderGateway();
		sectorSender.createTopic("mediator.client.sendtosector." + project.getSector().toString());
		Message msg = sectorSender.createMessage(text);
		try {
			msg.setJMSCorrelationID(correlationID);
			msg.setLongProperty("replyDeadline", replyDeadline);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		sectorSender.send(msg);
	}

}

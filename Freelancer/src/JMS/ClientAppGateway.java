package JMS;

import domain.Sector;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class ClientAppGateway {
	private Set<String> receivedMessageIDs;

	private MessageSenderGateway sender;
	private MessageReceiverGateway receiver;

	public ClientAppGateway(Sector sector) {
		receivedMessageIDs = new HashSet<String>();

		sender = new MessageSenderGateway("freelancer.client.send");
		receiver = new MessageReceiverGateway();
		receiver.subscribeToTopic("mediator.client.sendtosector." + sector.toString());

		receiver.setListener(new MessageListener() {
			public void onMessage(Message message) {
				System.out.println("Freelancer @ClientAppGateway Received message");
				try {
					if (receivedMessageIDs.add(message.getJMSMessageID())) {
						System.out.println("New MESSAGE");
						System.out.println(message.getJMSCorrelationID());
						System.out.println(message.getJMSMessageID());

						onMediatorRequest((TextMessage)message);
					}
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public abstract void onMediatorRequest(TextMessage message);

	public void sendMessage(String text, String correlationID) {
		Message msg = sender.createMessage(text);
		try {
			msg.setJMSCorrelationID(correlationID);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		sender.send(msg);
	}

}

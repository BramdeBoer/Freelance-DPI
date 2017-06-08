package JMS;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.UUID;

public abstract class ClientAppGateway {
	private MessageSenderGateway sender;
	private MessageReceiverGateway receiver;

	public ClientAppGateway() {
		sender = new MessageSenderGateway("employer.client.send");
		receiver = new MessageReceiverGateway("mediator.client.send");

		receiver.setListener(new MessageListener() {
			public void onMessage(Message message) {
				System.out.println("@ClientAppGateway Received message");
				onMediatorReply((TextMessage)message);
			}
		});
	}

	public abstract void onMediatorReply(TextMessage message);

	public void sendMessage(String text) {
		Message msg = sender.createMessage(text);
		try {
			msg.setJMSCorrelationID(UUID.randomUUID().toString());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		sender.send(msg);
	}

}

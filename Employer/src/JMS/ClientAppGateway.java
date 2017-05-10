package JMS;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public abstract class ClientAppGateway {
	private MessageSenderGateway sender;
	private MessageReceiverGateway receiver;

	public ClientAppGateway() {
		sender = new MessageSenderGateway("employer.client.send");
		receiver = new MessageReceiverGateway("employer.client.receive");

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
		sender.send(msg);
	}

}

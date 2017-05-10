package JMS;

import main.Controller;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public abstract class ClientAppGateway {
	private MessageSenderGateway sender;
	private MessageReceiverGateway receiver;

	public ClientAppGateway() {
		sender = new MessageSenderGateway("mediator.client.send");
		receiver = new MessageReceiverGateway("employer.client.send");

		receiver.setListener(new MessageListener() {
			public void onMessage(Message message) {
				System.out.println("@ClientAppGateway Received message");
				onEmployerRequest((TextMessage)message);
			}
		});
	}

	public abstract void onEmployerRequest(TextMessage message);

	public void sendMessage(String text) {
		Message msg = sender.createMessage(text);
		sender.send(msg);
	}

}

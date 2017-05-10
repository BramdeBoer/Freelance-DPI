package JMS;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageReceiverGateway {
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageConsumer consumer;

	public MessageReceiverGateway(String queue) {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		try {
			connection = connectionFactory.createConnection();
			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queue);

			consumer = this.session.createConsumer(destination);
		}
		catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void setListener(MessageListener listener) {
		try {
			consumer.setMessageListener(listener);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

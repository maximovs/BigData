package ar.edu.itba.bigdata.flume;

import java.util.Map;
import java.util.Map.Entry;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.flume.Channel;
import org.apache.flume.ChannelException;
import org.apache.flume.Context;
import org.apache.flume.CounterGroup;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQSink extends AbstractSink implements Configurable {

	private static final Logger logger = LoggerFactory
			.getLogger(ActiveMQSink.class);
	private static final String JMS_TOPIC = "topic";
	private static final String JMS_QUEUE = "queue";

	private ConnectionFactory jmsConnectionFactory = null;
	private Connection jmsConnection = null;
	private Session jmsSession = null;
	private Destination jmsDestination = null;
	private MessageProducer jmsProducer = null;

	private CounterGroup counterGroup;
	private String destinationName = "Flume-Storm-Queue";
	private String destinationType = JMS_QUEUE;

	public ActiveMQSink() {
		counterGroup = new CounterGroup();
	}
	
	@Override
	public void configure(Context context) {

	}

	@Override
	public void start() {
		// Initialize the connection to the external repository (e.g. HDFS) that
		// this Sink will forward Events to ..
		logger.info("JMS sink starting", this.getName());

		if (jmsConnectionFactory == null) {
			jmsConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			
		}

		try {
			jmsConnection = jmsConnectionFactory.createConnection();
		} catch (JMSException e) {
			logger.error("Could not create connection to broker: " + e);
		}

		try {
			jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			logger.error("Could not create session: " + e);
		}

		try {	
			if (destinationType.equalsIgnoreCase(JMS_QUEUE)) {
				jmsDestination = jmsSession.createQueue(destinationName);
			} else if (destinationType.equalsIgnoreCase(JMS_TOPIC)) {
				jmsDestination = jmsSession.createTopic(destinationName);
			} else {
				logger.error("Invalid destination type. Please selecte between topic or queue.");
			}
		} catch (JMSException e) {
			logger.error("Could not create destination" + destinationName
					+ ": " + e);
			return;
		}

		try {	
			jmsProducer = jmsSession.createProducer(jmsDestination);
		} catch (JMSException e) {	
			logger.error("Could not create producer: " + e);
		}
		super.start();
		logger.debug("JMS sink started", this.getName());
	}

	@Override
	public void stop() {
		// Disconnect from the external respository and do any
		// additional cleanup (e.g. releasing resources or nulling-out
		// field values)

		logger.info("JMS sink stopping", this.getName());
		try {
			if (jmsSession != null) {
				jmsSession.close();
			}
			if (jmsConnection != null) {
				jmsConnection.close();
			}
		} catch (JMSException e) {
			logger.error("Could not destroy connection: " + e);
			return;
		}

		super.stop();
		logger.debug("JMS sink stopped. Metrics:", this.getName(), counterGroup);
	}

	@Override
	public Status process() throws EventDeliveryException {
		Status status = null;

		// Start transaction
		Channel ch = getChannel();
		Transaction transaction = ch.getTransaction();
		transaction.begin();
		try {
			// This try clause includes whatever Channel operations you want to
			// do
			Event event = ch.take();
			
			if (event == null) {
				counterGroup.incrementAndGet("event.empty");
				status = Status.BACKOFF;
			} else {
				sendEvent(event);
				counterGroup.incrementAndGet("event.jms");
			}

			transaction.commit();
			status = Status.READY;
		} catch (ChannelException e) {
			handleTransactionException(transaction, e, "Unable to get event from channel.");
			status = Status.BACKOFF;
		} catch (Exception e) {
			handleTransactionException(transaction, e, "Could not complete transaction. Exception follows.");
			status = Status.BACKOFF;

			//			destroyConnection();

		} finally {
			transaction.close();
		}
		return status;
	}

	private void handleTransactionException(Transaction transaction,
			Exception e, String msg) {
		try {
			if (jmsSession != null) {
				jmsSession.rollback();
			}
		} catch (JMSException e1) {	
			logger.error("Unable to rollback JMS transaction. Exception follows.", e);
		}

		transaction.rollback();	
		logger.error(msg + " Exception follows.", e);
	}

	private void sendEvent(Event event) throws JMSException {
		BytesMessage message = jmsSession.createBytesMessage();

		Map<String, String> headers = event.getHeaders();
		for(Entry<String, String> entry: headers.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			message.setStringProperty(key, value);
		}
		message.writeBytes(event.getBody());
		jmsProducer.send(message);
	}
}

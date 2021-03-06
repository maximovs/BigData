package com.itba.g2.storm.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;


public class ActiveMQJmsProvider implements JmsProvider {
    private static final long serialVersionUID = 1L;

    private ConnectionFactory connectionFactory = null;
    private Destination destination = null;
    private String dest = "Flume-Storm-Queue";
    
    public ActiveMQJmsProvider(String mqAdress) throws NamingException{
        this.connectionFactory = new ActiveMQConnectionFactory(mqAdress); 
//        Context jndiContext = new InitialContext();
//        this.destination = (Destination) jndiContext.lookup("Flume-Storm-Queue");        

    }
    
    /**
     * Provides the JMS <code>ConnectionFactory</code>
     * @return the connection factory
     * @throws Exception
     */
    public ConnectionFactory connectionFactory() throws Exception{
        return this.connectionFactory;
    }

    /**
     * Provides the <code>Destination</code> (topic or queue) from which the
     * <code>JmsSpout</code> will receive messages.
     * @return
     * @throws Exception
     */
    public Destination destination() throws Exception{
        return this.destination;
    }

	@Override
	public String getDestination() {
		// TODO Auto-generated method stub
		return dest;
	}

}
package de.zeeman.jmstester;

import java.util.Date;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JmsJboss {
	Context context; 
	QueueConnectionFactory cf;
	Queue jmsQueuePEAPIQueue;
	
	@After
	public void tearDown() throws Exception {
        context.close();
	}
	
	private void list(String sb) throws Exception {
		NamingEnumeration<NameClassPair> list = context.list(sb);

        while(list.hasMore()) {
        	NameClassPair e = list.next();
        	if (e.getClassName().equals("javax.naming.Context")) {
        		list(sb + "/" + e.getName());
        	} else {
        		System.out.println(sb + "/" + e.getName() + ": " + e.getClassName());        		
        	}
        }
	}
	
	@Before
	public void setUp() throws Exception {
        final Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        env.put(Context.PROVIDER_URL, "remote://localhost:4447");
        env.put(Context.SECURITY_PRINCIPAL, "guest");
        env.put(Context.SECURITY_CREDENTIALS, "pass");
        context = new InitialContext(env);
        
        list("");
        
        cf = (QueueConnectionFactory) context.lookup("/jms/RemoteConnectionFactory");
        jmsQueuePEAPIQueue = (Queue)context.lookup("jms/queue/PEAPIQueue4");
	}
	
	@Test
	public void test() throws Exception{
		QueueConnection con = cf.createQueueConnection();

		QueueSession session = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		
		QueueSender sender = session.createSender(jmsQueuePEAPIQueue);
		con.start();
		TextMessage msg = session.createTextMessage("hello world!" + new Date());
		sender.send(msg);

		System.out.println("Sent " + msg);
		
		session.close();
		con.stop();
		con.close();
		
	}
	
}

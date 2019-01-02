package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	private SubscriptionService subsserv;
	
	@Before
	public void setUp(){
		subsserv=new SubscriptionService();
	}
	
	
	@Test (expected=NullClientException.class)
	public void testNullClientSubscriptionReturnsException() throws NullClientException, ExistingClientException {
		subsserv.addSubscriber(null);
	}
	
	@Test 
	public void testAddSubscriberAddsClientToSubscribers() throws NullClientException, ExistingClientException {
		Client c = mock (Client.class);
		subsserv.addSubscriber(c);
		assertTrue(subsserv.subscribers.contains(c));
	}
	
	@Test (expected=ExistingClientException.class)
	public void testExeptionWhenAddingExistingSubscriber() throws NullClientException, ExistingClientException {
		Client c = mock (Client.class);
		subsserv.addSubscriber(c);
		subsserv.addSubscriber(c);
	}
	
	@Test 
	public void testAddSubscriberAddsManyClientsToSubscribers() throws NullClientException, ExistingClientException {
		Client c1 = mock (Client.class);
		Client c2 = mock (Client.class);
		subsserv.addSubscriber(c1);
		subsserv.addSubscriber(c2);
		assertTrue(subsserv.subscribers.contains(c1) && subsserv.subscribers.contains(c2) );
	}
	
	@Test (expected=NullClientException.class)
	public void testNullClientDeletionReturnsException() throws NullClientException, NonExistingClientException {
		subsserv.removeSubscriber(null);
	}
	
	@Test (expected=NonExistingClientException.class)
	public void testExeptionWhenRemovingNonExistingSubscriber() throws NullClientException, NonExistingClientException {
		Client c = mock (Client.class);
		subsserv.removeSubscriber(c);
	}
	
	@Test 
	public void testRemoveSubscriberRemovesClientFromSubscribers() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client c1 = mock (Client.class);
		Client c2 = mock (Client.class);
		subsserv.addSubscriber(c1);
		subsserv.addSubscriber(c2);
		subsserv.removeSubscriber(c1);
		assertTrue(!subsserv.subscribers.contains(c1));
	}
	
	@Test (expected=NonExistingClientException.class)
	public void testRemoveSubscriberTwoTimesFails() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client c = mock (Client.class);
		subsserv.addSubscriber(c);
		subsserv.removeSubscriber(c);
		subsserv.removeSubscriber(c);
	}
	
	@Test 
	public void testRemoveSubscriberRemovesManyClientsFromSubscribers() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client c1 = mock (Client.class);
		Client c2 = mock (Client.class);
		subsserv.addSubscriber(c1);
		subsserv.addSubscriber(c2);
		subsserv.removeSubscriber(c1);
		subsserv.removeSubscriber(c2);
		assertTrue(!subsserv.subscribers.contains(c1) && !subsserv.subscribers.contains(c2) );
	}
	@Test 
	public void testRemoveSubscriberRemovesAllClientsFromSubscribers() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client c1 = mock (Client.class);
		Client c2 = mock (Client.class);
		Client c3 = mock (Client.class);
		subsserv.addSubscriber(c1);
		subsserv.addSubscriber(c2);
		subsserv.addSubscriber(c3);
		subsserv.removeSubscriber(c1);
		subsserv.removeSubscriber(c2);
		subsserv.removeSubscriber(c3);
		assertTrue(subsserv.subscribers.isEmpty() );
	}
	
	@Test 
	public void testNotReceivingMessagesWithoutMail() throws NullClientException, ExistingClientException {
		Client c = mock (Client.class);
		Message m = mock (Message.class);
		
		when(c.hasEmail()).thenReturn(false);
		subsserv.addSubscriber(c);
		subsserv.sendMessage(m);
		
		verify(c, never()).receiveMessage(m);
		
	}
	
	@Test 
	public void testReceivingMessagesWithMail() throws NullClientException, ExistingClientException {
		Client c = mock (Client.class);
		Message m = mock (Message.class);
		
		when(c.hasEmail()).thenReturn(true);
		subsserv.addSubscriber(c);
		subsserv.sendMessage(m);
		
		verify(c).receiveMessage(m);
		
	}
	
	@Test 
	public void testSeveralSubscribedClientsReceivingMessagesWithMail() throws NullClientException, ExistingClientException {
		Client c1 = mock (Client.class);
		Client c2 = mock (Client.class);
		Message m = mock (Message.class);
		
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		subsserv.addSubscriber(c1);
		subsserv.addSubscriber(c2);
		subsserv.sendMessage(m);
		
		verify(c1).receiveMessage(m);
		verify(c2).receiveMessage(m);
		
	}
	
	@Test 
	public void testUnsubscribedClientsNotReceivingMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c1 = mock (Client.class);
		Client c2 = mock (Client.class);
		Message m = mock (Message.class);
		
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		subsserv.addSubscriber(c1);
		subsserv.addSubscriber(c2);
		subsserv.removeSubscriber(c1);
		
		subsserv.sendMessage(m);
		
		verify(c1, never()).receiveMessage(m);
		verify(c2).receiveMessage(m);
		
	}
}

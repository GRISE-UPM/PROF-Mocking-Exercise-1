package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SubscriptionServiceTest {
	private SubscriptionService subscription;
 	private Client c1;
 	private Client c2;
 	private Client c3;
 	private Message m1;
 	private Message m2;

 	@Before
 	public void setUp(){
 		subscription = new SubscriptionService();
 		c1 = Mockito.mock(Client.class);
		c2 = Mockito.mock(Client.class);
		c3 = Mockito.mock(Client.class);
		m1 = Mockito.mock(Message.class);
		m2 = Mockito.mock(Message.class);
 	}

 	@Test (expected=NullClientException.class)
 	public void nullClient() throws NullClientException, ExistingClientException {
 		subscription.addSubscriber(null);
 	}

 	@Test
 	public void addClient() throws NullClientException, ExistingClientException {
 		subscription.addSubscriber(c1);		
 	}

 	@Test(expected=ExistingClientException.class)
 	public void addTwoEqualClients() throws NullClientException, ExistingClientException {
 		subscription.addSubscriber(c1);
 		subscription.addSubscriber(c1);
 	}

 	@Test
 	public void addClientsToSubscribers() throws NullClientException, ExistingClientException {
 		subscription.addSubscriber(c1);
 		subscription.addSubscriber(c2);
 		assertTrue(subscription.subscribers.contains(c1));
 		assertTrue(subscription.subscribers.contains(c2));
 	}

 	@Test (expected=NullClientException.class)
 	public void removeNullClient() throws NullClientException, NonExistingClientException {
 		subscription.removeSubscriber(null);
 	}

 	@Test (expected=NonExistingClientException.class)
 	public void removeNotExistingClient() throws NullClientException, NonExistingClientException {
 		subscription.removeSubscriber(c1);
 	}

 	@Test
 	public void removeExistingClient() throws NullClientException, NonExistingClientException, ExistingClientException {
 		subscription.addSubscriber(c1);
 		subscription.removeSubscriber(c1);
 		assertTrue(subscription.subscribers.isEmpty());
 	}

 	@Test (expected=NonExistingClientException.class)
 	public void removeTwoTimesSameClient() throws NullClientException, NonExistingClientException, ExistingClientException {
 		subscription.addSubscriber(c1);
 		subscription.removeSubscriber(c1);
 		subscription.removeSubscriber(c1);
 	}

 	@Test
 	public void addRemoveClients() throws NullClientException, NonExistingClientException, ExistingClientException {
 		subscription.addSubscriber(c1);
 		subscription.addSubscriber(c2);
 		subscription.addSubscriber(c3);
 		subscription.removeSubscriber(c1);
 		subscription.removeSubscriber(c2);
 		assertTrue(subscription.subscribers.size() == 1);
 	}

 	@Test
 	public void removeAllClients() throws NullClientException, NonExistingClientException, ExistingClientException {
 		subscription.addSubscriber(c1);
 		subscription.addSubscriber(c2);
 		subscription.removeSubscriber(c1);
 		subscription.removeSubscriber(c2);
 		assertTrue(subscription.subscribers.isEmpty());
 	}
 	
 	// ************* INTERACTION TESTS *************

 	@Test
 	public void SendMessageWithEmail() throws NullClientException, ExistingClientException {
 		Mockito.when(c1.hasEmail()).thenReturn(true);
 		subscription.addSubscriber(c1);
 		subscription.sendMessage(m1);
 		Mockito.verify(c1).receiveMessage(m1); 
 	}

 	@Test
 	public void noMessageWithInvalidEmail() throws NullClientException, ExistingClientException {
 		Mockito.when(c1.hasEmail()).thenReturn(false);
 		subscription.addSubscriber(c1);
 		subscription.sendMessage(m1);
 		Mockito.verify(c1, Mockito.never()).receiveMessage(m1); 
 	}

 	@Test
 	public void SendMessageToSubscritionWithEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
 		Mockito.when(c1.hasEmail()).thenReturn(true);
 		Mockito.when(c2.hasEmail()).thenReturn(true);
 		subscription.addSubscriber(c1);
 		subscription.addSubscriber(c2);
 		subscription.sendMessage(m1);
 		Mockito.verify(c1).receiveMessage(m1); 
 		Mockito.verify(c2).receiveMessage(m1);

 		subscription.removeSubscriber(c2);
 		subscription.sendMessage(m2);
 		Mockito.verify(c2, Mockito.never()).receiveMessage(m2); 
 	}
}

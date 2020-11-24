package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.*;


public class SubscriptionServiceTest {
	
	private SubscriptionService subscriptionService;
	
	private Client sub;
	private Message msg;
	
	@Before
	public void setup() {
		subscriptionService = new SubscriptionService();
		sub = mock(Client.class);
		msg = mock(Message.class);
	}
	
	/*
	 * State Tests 
	 */
	
	@Test(expected = NullClientException.class)
	public void addNullClient() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(null);
	}
	
	@Test
	public void addClientToSubscribersList() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(sub);
		assertTrue(subscriptionService.subscribers.contains(sub));
	}
	
	@Test(expected = ExistingClientException.class)
	public void addAClientTwice() throws NullClientException, ExistingClientException  {
		subscriptionService.addSubscriber(sub);
		subscriptionService.addSubscriber(sub);
	}
	
	@Test
	public void addSeveralClients() throws NullClientException, ExistingClientException {
		Client sub2 = mock(Client.class);
		Client sub3 = mock(Client.class);
		subscriptionService.addSubscriber(sub);
		subscriptionService.addSubscriber(sub2);
		subscriptionService.addSubscriber(sub3);
		assertTrue(subscriptionService.subscribers.contains(sub));
		assertTrue(subscriptionService.subscribers.contains(sub2));
		assertTrue(subscriptionService.subscribers.contains(sub3));
	}

	@Test(expected = NullClientException.class)
	public void removeNullClient() throws NullClientException, NonExistingClientException {
		subscriptionService.removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void removeNonExistingClient() throws NullClientException, NonExistingClientException {
		subscriptionService.removeSubscriber(sub);
	}
	
	@Test
	public void removeAnExistingClient() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(sub);
		subscriptionService.removeSubscriber(sub);
		assertFalse(subscriptionService.subscribers.contains(sub));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void removeAClientTwice() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(sub);
		subscriptionService.removeSubscriber(sub);
		subscriptionService.removeSubscriber(sub);
	}
	
	@Test
	public void removeSeveralClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client sub2 = mock(Client.class);
		Client sub3 = mock(Client.class);
		subscriptionService.addSubscriber(sub);
		subscriptionService.addSubscriber(sub2);
		subscriptionService.addSubscriber(sub3);
		subscriptionService.removeSubscriber(sub);
		subscriptionService.removeSubscriber(sub2);
		assertFalse(subscriptionService.subscribers.contains(sub));
		assertFalse(subscriptionService.subscribers.contains(sub2));
	}
	
	@Test
	public void removeAllClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client sub2 = mock(Client.class);
		Client sub3 = mock(Client.class);
		subscriptionService.addSubscriber(sub);
		subscriptionService.addSubscriber(sub2);
		subscriptionService.addSubscriber(sub3);
		subscriptionService.removeSubscriber(sub);
		subscriptionService.removeSubscriber(sub2);
		subscriptionService.removeSubscriber(sub3);
		assertTrue(subscriptionService.subscribers.isEmpty());
	}
	
	/*
	 * Interaction Tests 
	 */
	
	@Test
	public void aSubscriberReceiveMsg() throws NullClientException, ExistingClientException {
		when(sub.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(sub);
		subscriptionService.sendMessage(msg);
		verify(sub).receiveMessage(msg);
	}
	
	@Test
	public void aSubscriberDoesntReceiveMsg() throws NullClientException, ExistingClientException {
		when(sub.hasEmail()).thenReturn(false);
		subscriptionService.addSubscriber(sub);
		subscriptionService.sendMessage(msg);
		verify(sub, never()).receiveMessage(msg);
	}
	
	@Test
	public void multipleSubscribersDontReceiveMsgs() throws NullClientException, ExistingClientException {
		when(sub.hasEmail()).thenReturn(true);
		Client sub2 = mock(Client.class);
		when(sub2.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(sub);
		subscriptionService.addSubscriber(sub2);
		subscriptionService.sendMessage(msg);
		verify(sub).receiveMessage(msg);
		verify(sub2).receiveMessage(msg);
	}
	
	@Test
	public void aNonSubscriberDoesntReceiveMsg() throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(sub);
		subscriptionService.removeSubscriber(sub);
		subscriptionService.sendMessage(msg);
		verify(sub, never()).receiveMessage(msg);
	}
}

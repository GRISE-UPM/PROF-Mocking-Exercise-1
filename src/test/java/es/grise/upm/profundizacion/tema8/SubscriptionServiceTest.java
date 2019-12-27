package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	private SubscriptionService subscriptionService;
	
	@Before
	public void setUp() {
		
		subscriptionService = new SubscriptionService();
	}
	
	@Test(expected = NullClientException.class)
	public void shouldNotAddNullClient() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(null);
	}
	
	@Test
	public void shouldAddClientCorrectly() throws NullClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		
		assertEquals(1, subscriptionService.subscribers.size());
		assertTrue(subscriptionService.subscribers.contains(client));
	}
	
	@Test(expected = ExistingClientException.class)
	public void shouldNotAddClientTwice() throws NullClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);
		
		subscriptionService.addSubscriber(client);
	}
	
	@Test
	public void shouldAddSeveralClientCorrectly() throws NullClientException, ExistingClientException {
		
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		assertEquals(3, subscriptionService.subscribers.size());
		assertTrue(subscriptionService.subscribers.contains(client1));
		assertTrue(subscriptionService.subscribers.contains(client2));
		assertTrue(subscriptionService.subscribers.contains(client3));
	}
	
	@Test(expected = NullClientException.class)
	public void shouldNotRemoveNullClient() throws NullClientException, NonExistingClientException {
		subscriptionService.removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void shouldNotRemoveUnexistingClient() throws NullClientException, NonExistingClientException {
		
		Client client = mock(Client.class);

		subscriptionService.removeSubscriber(client);
	}
	
	@Test
	public void shouldRemoveExistingClient() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);
		
		subscriptionService.removeSubscriber(client);
		
		assertEquals(0, subscriptionService.subscribers.size());
		assertFalse(subscriptionService.subscribers.contains(client));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void shouldNotRemoveExistingClientTwice() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);
		
		subscriptionService.removeSubscriber(client);
		subscriptionService.removeSubscriber(client);
	}
	
	@Test
	public void shouldRemoveSeveralClientsCorrectly() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
		
		assertEquals(1, subscriptionService.subscribers.size());
		assertFalse(subscriptionService.subscribers.contains(client1));
		assertFalse(subscriptionService.subscribers.contains(client2));
		assertTrue(subscriptionService.subscribers.contains(client3));
	}
	
	@Test
	public void shouldRemoveAllClientsCorrectly() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
		subscriptionService.removeSubscriber(client3);
		
		assertEquals(0, subscriptionService.subscribers.size());
		assertFalse(subscriptionService.subscribers.contains(client1));
		assertFalse(subscriptionService.subscribers.contains(client2));
		assertFalse(subscriptionService.subscribers.contains(client3));
	}
	
	@Test
	public void subscribedClientWithEmailShouldReceiveMessages() throws NullClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		when(client.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(client);

		Message message = mock(Message.class);
				
		subscriptionService.sendMessage(message);
		
		verify(client).hasEmail();
		verify(client).receiveMessage(message);
	}
	
	@Test
	public void subscribedClientWithoutEmailShouldNotReceiveMessages() throws NullClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);

		Message message = mock(Message.class);
				
		subscriptionService.sendMessage(message);
		
		verify(client).hasEmail();
		verify(client, never()).receiveMessage(message);
	}
	
	@Test
	public void allSubscribedClientsWithEmailShouldReceiveMessages() throws NullClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		when(client.hasEmail()).thenReturn(true);		
		subscriptionService.addSubscriber(client);

		Client client2 = mock(Client.class);
		when(client2.hasEmail()).thenReturn(true);		
		subscriptionService.addSubscriber(client2);
		
		Client client3 = mock(Client.class);	
		subscriptionService.addSubscriber(client3);
		
		Message message = mock(Message.class);
				
		subscriptionService.sendMessage(message);
		
		verify(client).hasEmail();
		verify(client2).hasEmail();
		verify(client3).hasEmail();
		
		verify(client).receiveMessage(message);
		verify(client2).receiveMessage(message);
		verify(client3, never()).receiveMessage(message);
	}
	
	@Test
	public void unsubscribedClientWithEmailShouldNotReceiveMessages() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		when(client.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);

		Message message = mock(Message.class);
		subscriptionService.sendMessage(message);
		
		verify(client, never()).hasEmail();
		verify(client, never()).receiveMessage(message);		
	}
}

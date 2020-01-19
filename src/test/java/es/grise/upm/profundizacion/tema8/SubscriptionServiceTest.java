package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.lang.reflect.Array;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	private SubscriptionService subscriptionService;
	
	private static Client client1;
	private static Client client2;
	private static Client client3;
	
	private static Message message1;
	
	@BeforeClass
	public static void setUCpClass() {
		client1 = mock(Client.class);
		client2 = mock(Client.class);
		client3 = mock(Client.class);
		
		message1 = mock(Message.class);
	}
	
	@Before
	public void setUp() {
		subscriptionService = new SubscriptionService();
	}
	
	@Test(expected = NullClientException.class)
	public void testAddNullClient() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(null);
	}
	
	@Test
	public void testAddClient() throws NullClientException, ExistingClientException {
		
		subscriptionService.addSubscriber(client1);
		assertTrue(subscriptionService.subscribers.contains(client1));
	}
	
	@Test(expected = ExistingClientException.class)
	public void testAddSameClientTwice() throws NullClientException, ExistingClientException {
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client1);
	}
	
	@Test
	public void testAddVariousClientsAndAreInSubscribersList() throws NullClientException, ExistingClientException {
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		//assertArrayEquals(new Client[] {client1, client2, client3}, subscriptionService.subscribers.toArray(new Client[] {}) );
		assertTrue(subscriptionService.subscribers.contains(client1));
		assertTrue(subscriptionService.subscribers.contains(client2));
		assertTrue(subscriptionService.subscribers.contains(client3));
	}
	
	@Test(expected = NullClientException.class)
	public void testDeleteNullClient() throws NullClientException, NonExistingClientException {
		subscriptionService.removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void testDeleteNonExistingClient() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
	}
	
	@Test
	public void testDeleteClient() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(client1);
		subscriptionService.removeSubscriber(client1);
		assertFalse(subscriptionService.subscribers.contains(client1));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void testDeleteExistingClientTwice() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(client1);
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client1);
	}
	
	@Test
	public void testDeleteVariousClients() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		// Add
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		// delete
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
		
		assertFalse(subscriptionService.subscribers.contains(client1));
		assertFalse(subscriptionService.subscribers.contains(client2));
		
	}
	
	@Test
	public void testDeleteAllsClients() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		// Add
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		// Delete
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
		subscriptionService.removeSubscriber(client3);
		
		assertFalse(subscriptionService.subscribers.contains(client1));
		assertFalse(subscriptionService.subscribers.contains(client2));
		assertFalse(subscriptionService.subscribers.contains(client3));
		assertTrue(subscriptionService.subscribers.isEmpty());
		
	}
	
	@Test
	public void testSubscribedClientEmailOkReceiveMessages() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client c1 = mock(Client.class);
		
		when(c1.hasEmail()).thenReturn(true);
		
		subscriptionService.addSubscriber(c1);
		
		subscriptionService.sendMessage(message1);
		
		verify(c1).hasEmail();
		verify(c1).receiveMessage(message1);
	}
	
	@Test
	public void testSubscribedClientEmailNotOkDoesntReceiveMessages() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client c1 = mock(Client.class);
		
		when(c1.hasEmail()).thenReturn(false);
		
		subscriptionService.addSubscriber(c1);
		
		subscriptionService.sendMessage(message1);
		
		verify(c1).hasEmail();
		verify(c1, never()).receiveMessage(message1);
	}
	
	@Test
	public void testVariousSubscribedClientEmailOkReceiveMessages() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		
		subscriptionService.addSubscriber(c1);
		subscriptionService.addSubscriber(c2);
		
		subscriptionService.sendMessage(message1);
		
		verify(c1).hasEmail();
		verify(c2).hasEmail();
		verify(c1).receiveMessage(message1);
		verify(c2).receiveMessage(message1);
	}
	
	@Test
	public void testUnSubscribedClientEmailDontReceiveMessages() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client c1 = mock(Client.class);
		
		when(c1.hasEmail()).thenReturn(true);
		
		subscriptionService.addSubscriber(c1);
		
		subscriptionService.sendMessage(message1);
		
		subscriptionService.removeSubscriber(c1);
		
		subscriptionService.sendMessage(message1);
		
		// Only receives the first msg; when the second message was sent, the client was UnSubscribed
		verify(c1, times(1)).hasEmail();
		verify(c1, times(1)).receiveMessage(message1);
	}
	
	
}

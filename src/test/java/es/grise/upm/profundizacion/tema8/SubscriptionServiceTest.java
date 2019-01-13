package es.grise.upm.profundizacion.tema8;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

public class SubscriptionServiceTest {
	
	private SubscriptionService subscription_service;
	private Client client;
	private Message message;
	private final int AMOUNT_CLIENTS = 5;
	private Client[] clients;
	
	@Before
	public void before() {
		client = mock(Client.class);
		message = mock(Message.class);
		clients = new Client[AMOUNT_CLIENTS];
		for(int i = 0; i < clients.length; i++) {
			clients[i] = mock(Client.class); 
		}
		subscription_service = new SubscriptionService();
	}
	
	@Test(expected = NullClientException.class)
	public void add_null_subscriber() throws Exception {
		subscription_service.addSubscriber(null);
	}
	
	@Test
	public void successfully_add_one_subscriber() throws Exception {
		subscription_service.addSubscriber(client);
		assertTrue(subscription_service.subscribers.contains(client));
	}
	
	@Test(expected = ExistingClientException.class)
	public void add_subscriber_twice() throws Exception {
		subscription_service.addSubscriber(client);
		subscription_service.addSubscriber(client);
	}
	
	@Test
	public void successfully_add_many_subscriber() throws Exception {
		
		for(Client client : clients) {
			subscription_service.addSubscriber(client);
		}
		
		for(Client client :clients) {
			assertTrue(subscription_service.subscribers.contains(client));
		}
		
	}
	
	@Test(expected = NullClientException.class)
	public void remove_null_subscriber() throws Exception {
		subscription_service.removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void remove_non_existing_client() throws Exception {
		subscription_service.removeSubscriber(client);
	}
	
	@Test
	public void successfully_remove_one_client() throws Exception {
		subscription_service.addSubscriber(client);
		subscription_service.removeSubscriber(client);
		
		assertFalse(subscription_service.subscribers.contains(client));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void remove_twice_existing_client() throws Exception {
		subscription_service.removeSubscriber(client);
		subscription_service.removeSubscriber(client);
		subscription_service.removeSubscriber(client);
	}
	
	@Test
	public void successfully_remove_many_client() throws Exception {
		subscription_service.addSubscriber(client);
		
		for(Client client : clients) {
			subscription_service.addSubscriber(client);
		}
		
		assertEquals(AMOUNT_CLIENTS + 1, subscription_service.subscribers.size());
		
		for(Client client : clients) {
			subscription_service.removeSubscriber(client);
		}
		
		for(Client client : clients) {
			assertFalse(subscription_service.subscribers.contains(client));
		}
		assertTrue(subscription_service.subscribers.contains(client));
		assertEquals(1, subscription_service.subscribers.size());
	}
	
	@Test
	public void successfully_remove_all_client() throws Exception {
		for(Client client : clients) {
			subscription_service.addSubscriber(client);
		}
		for(Client client : clients) {
			subscription_service.removeSubscriber(client);
		}
		assertTrue(subscription_service.subscribers.isEmpty());
	}
	
	@Test
	public void one_client_receives_messages_when_having_mail() throws Exception {
		when(client.hasEmail()).thenReturn(true);
		
		subscription_service.addSubscriber(client);
		subscription_service.sendMessage(message);
		
		verify(client).receiveMessage(message);
	}
	
	@Test
	public void one_client_does_not_receives_messages_when_not_having_mail() throws Exception {
		when(client.hasEmail()).thenReturn(false);
		
		subscription_service.addSubscriber(client);
		subscription_service.sendMessage(message);
		
		verify(client, never()).receiveMessage(message);
	}
	
	@Test
	public void many_client_receive_messages_when_having_mail() throws Exception {
		for(Client client : clients) {
			when(client.hasEmail()).thenReturn(true);
			subscription_service.addSubscriber(client);
		}
		
		subscription_service.sendMessage(message);
		
		for(Client client : clients) {
			verify(client).receiveMessage(message);
		}
	}
	
	@Test
	public void unsubscribed_client_does_not_receive_messages() throws Exception {
		when(client.hasEmail()).thenReturn(true);
		subscription_service.addSubscriber(client);
		
		for(Client client : clients) {
			when(client.hasEmail()).thenReturn(true);
			subscription_service.addSubscriber(client);
		}
		
		subscription_service.removeSubscriber(client);
		subscription_service.sendMessage(message);
		
		for(Client client : clients) {
			verify(client).receiveMessage(message);
		}
		verify(client, never()).receiveMessage(message);
	}
	
	
}

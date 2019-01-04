package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	@Test(expected = NullClientException.class)
	public void AddClientNull() throws NullClientException, ExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = null;
		service.addSubscriber(client);
	}
	
	@Test
	public void AddClient() throws NullClientException, ExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = mock(Client.class);
		service.addSubscriber(client);
		assertEquals(service.subscribers.size(),1);
		assertTrue(service.subscribers.contains(client));
	}
	
	@Test(expected = ExistingClientException.class)
	public void AddDuplicatedClient() throws NullClientException, ExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = mock(Client.class);
		service.addSubscriber(client);
		service.addSubscriber(client);
	}
	
	@Test
	public void AddSeveralClients() throws NullClientException, ExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.addSubscriber(client3);
		assertEquals(service.subscribers.size(),3);
		assertTrue(service.subscribers.contains(client1));
		assertTrue(service.subscribers.contains(client2));
		assertTrue(service.subscribers.contains(client3));
	}
	
	@Test(expected = NullClientException.class)
	public void RemoveClientNull() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = null;
		service.removeSubscriber(client);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void RemoveNonExistingClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = mock(Client.class);
		service.removeSubscriber(client);
	}
	
	@Test
	public void RemoveClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = mock(Client.class);
		service.addSubscriber(client);
		service.removeSubscriber(client);
		assertFalse(service.subscribers.contains(client));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void RemoveSameClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = mock(Client.class);
		service.addSubscriber(client);
		service.removeSubscriber(client);
		service.removeSubscriber(client);
	}
	
	@Test
	public void RemoveSeveralClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.addSubscriber(client3);
		service.removeSubscriber(client1);
		service.removeSubscriber(client3);
		assertFalse(service.subscribers.contains(client1));
		assertTrue(service.subscribers.contains(client2));
		assertFalse(service.subscribers.contains(client3));
	}
	
	@Test
	public void RemoveAllClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.addSubscriber(client3);
		service.removeSubscriber(client1);
		service.removeSubscriber(client2);
		service.removeSubscriber(client3);
		assertEquals(service.subscribers.size(),0);
	}
	
	@Test
	public void CallReceiveMessageInClient() throws NullClientException, ExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = mock(Client.class);
		when(client.hasEmail()).thenReturn(true);
		Message message = mock(Message.class);
		service.addSubscriber(client);
		service.sendMessage(message);
		verify(client).receiveMessage(message);
	}
	
	@Test
	public void NotCallReceiveMessageInClient() throws NullClientException, ExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client = mock(Client.class);
		when(client.hasEmail()).thenReturn(false);
		Message message = mock(Message.class);
		service.addSubscriber(client);
		service.sendMessage(message);
		verify(client, never()).receiveMessage(message);
	}
	
	@Test
	public void CallReceiveMessageInClients() throws NullClientException, ExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		Message message = mock(Message.class);
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.sendMessage(message);
		verify(client1).receiveMessage(message);
		verify(client2).receiveMessage(message);
	}
	
	@Test
	public void NotCallReceiveMessageInRemovedClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		Message message = mock(Message.class);
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.removeSubscriber(client1);
		service.sendMessage(message);
		verify(client1, never()).receiveMessage(message);
		verify(client2).receiveMessage(message);
	}
}

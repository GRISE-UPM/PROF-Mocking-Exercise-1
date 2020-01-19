package es.grise.upm.profundizacion.tema8;

import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

public class SubscriptionServiceTest {

	SubscriptionService subscribers;
	Collection <Client> subClientList;

	@Before
	public void setUp() {
		subscribers = new SubscriptionService();
	}

	@Test(expected = NullClientException.class)
	public void addNullClient() throws NullClientException, ExistingClientException {
		subscribers.addSubscriber(null);
	}

	@Test
	public void clientIsAdded() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		subscribers.addSubscriber(client);
		assertTrue(subscribers.subscribers.contains(client));
	}

	@Test(expected = ExistingClientException.class)
	public void clientTwiceAdded() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		subscribers.addSubscriber(client);
		subscribers.addSubscriber(client);
	}

	@Test
	public void multipleClientsStored() throws NullClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		subscribers.addSubscriber(client1);
		subscribers.addSubscriber(client2);
		subscribers.addSubscriber(client3);
		assertEquals(3, subscribers.subscribers.size());
		assertTrue(subscribers.subscribers.contains(client1));
		assertTrue(subscribers.subscribers.contains(client2));
		assertTrue(subscribers.subscribers.contains(client3));
	}

	@Test(expected = NullClientException.class)
	public void noRemoveNullClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = null;
		subscribers.addSubscriber(client);
		subscribers.removeSubscriber(client);
	}

	@Test(expected = NonExistingClientException.class)
	public void removeNonExistingClient() throws NullClientException, NonExistingClientException {
		Client client = mock(Client.class);
		subscribers.removeSubscriber(client);
	}

	@Test
	public void removeClientTest() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		subscribers.addSubscriber(client);
		subscribers.removeSubscriber(client);
		assertTrue(subscribers.subscribers.size() == 0);
		assertFalse(subscribers.subscribers.contains(client));
	}

	@Test(expected = NonExistingClientException.class)
	public void removeClientTwice() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client client = mock(Client.class);
		subscribers.addSubscriber(client);
		subscribers.removeSubscriber(client);
		subscribers.removeSubscriber(client);
	}

	@Test
	public void removeMultipleUsers() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		subscribers.addSubscriber(client1);
		subscribers.addSubscriber(client2);
		subscribers.addSubscriber(client3);
		subscribers.removeSubscriber(client2);
		subscribers.removeSubscriber(client3);
		assertEquals(1, subscribers.subscribers.size());
		assertFalse(subscribers.subscribers.contains(client2));
		assertFalse(subscribers.subscribers.contains(client3));
	}

	@Test
	public void removeAllClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		subscribers.addSubscriber(client1);
		subscribers.addSubscriber(client2);
		subscribers.addSubscriber(client3);
		subscribers.removeSubscriber(client1);
		subscribers.removeSubscriber(client2);
		subscribers.removeSubscriber(client3);
		assertTrue(subscribers.subscribers.isEmpty());
	}

	@Test
	public void testReceivingMessages() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(true);
		subscribers.addSubscriber(client);
		subscribers.sendMessage(message);
		verify(client, times(1)).receiveMessage(message);
	}

	@Test 
	public void testNoReceivingMessageNoEmail() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(false);
		subscribers.addSubscriber(client);
		subscribers.sendMessage(message);
		verify(client, never()).receiveMessage(message);
	}

	@Test
	public void multipleClientsReceiveMessages() throws NullClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Message message = mock(Message.class);
		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		subscribers.addSubscriber(client1);
		subscribers.addSubscriber(client2);
		subscribers.sendMessage(message);
		verify(client1).hasEmail();
		verify(client2).hasEmail();
		verify(client1, times(1)).receiveMessage(message);
		verify(client2, times(1)).receiveMessage(message);
	}

	@Test
	public void unsubscribeClientNotReceiveMessages() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Message message = mock(Message.class);
		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		subscribers.addSubscriber(client1);
		subscribers.addSubscriber(client2);
		subscribers.removeSubscriber(client1);
		subscribers.sendMessage(message);
		verify(client1, never()).hasEmail();
		verify(client2).hasEmail();
		verify(client1, never()).receiveMessage(message);
		verify(client2, times(1)).receiveMessage(message);
	}

}

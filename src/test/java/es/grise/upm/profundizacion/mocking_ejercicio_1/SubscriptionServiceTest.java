package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class SubscriptionServiceTest {

	SubscriptionService sub;

	// STATE TESTS

	@BeforeEach
	public void setUpEach() {
		sub = new SubscriptionService();
	}

	@Test(expected = NullClientException.class)
	public void shouldThrowNullClientException_addNullClient()
			throws NullClientException, ExistingClientException {

		sub.addSubscriber(null);
	}

	@Test
	public void storesClient()
			throws NullClientException, ExistingClientException {

		Client cliMock = mock(Client.class);
		sub.addSubscriber(cliMock);
		assertTrue(sub.subscribers.contains(cliMock));
	}

	@Test(expected = ExistingClientException.class)
	public void shouldThrowExistingClientExc_duplicatedClient()
			throws NullClientException, ExistingClientException {

		Client cliMock = mock(Client.class);
		sub.addSubscriber(cliMock);
		sub.addSubscriber(cliMock);
	}

	@Test
	public void storesMultipleClients()
			throws NullClientException, ExistingClientException {

		Client cliMock1 = mock(Client.class);
		Client cliMock2 = mock(Client.class);
		sub.addSubscriber(cliMock1);
		sub.addSubscriber(cliMock2);
		assertTrue(sub.subscribers.contains(cliMock1) &&
				sub.subscribers.contains(cliMock2));
	}

	@Test(expected = NullClientException.class)
	public void shouldThrowNullCLientException_removeNullCLient()
			throws NullClientException, NonExistingClientException {

		sub.removeSubscriber(null);
	}

	@Test(expected = NonExistingClientException.class)
	public void shouldThrowNonExistingClientException_removeNonExistingClient()
			throws NullClientException, NonExistingClientException {

		sub.removeSubscriber(mock(Client.class));
	}

	@Test
	public void removeClient()
			throws NullClientException, ExistingClientException, NonExistingClientException {

		Client cliMock = mock(Client.class);
		sub.addSubscriber(cliMock);
		sub.removeSubscriber(cliMock);
		assertTrue(!sub.subscribers.contains(cliMock));
	}

	@Test(expected = NonExistingClientException.class)
	public void shouldThrowNonExistingClientException_removeTwiceSameClient()
			throws NullClientException, ExistingClientException, NonExistingClientException {

		Client cliMock = mock(Client.class);
		sub.addSubscriber(cliMock);
		sub.removeSubscriber(cliMock);
		sub.removeSubscriber(cliMock);
	}

	@Test
	public void removeMultipleClient()
			throws NullClientException, ExistingClientException, NonExistingClientException {

		Client cliMock1 = mock(Client.class);
		Client cliMock2 = mock(Client.class);
		sub.addSubscriber(cliMock1);
		sub.addSubscriber(cliMock2);
		sub.removeSubscriber(cliMock1);
		sub.removeSubscriber(cliMock2);
		assertTrue(!sub.subscribers.contains(cliMock1) &&
				!sub.subscribers.contains(cliMock2));
	}

	@Test
	public void removeAllClient()
			throws NullClientException, ExistingClientException {

		// Add 10 clients
		for (int i = 0; i < 10; i++)
			sub.addSubscriber(mock(Client.class));

		// Remove all clients through lambda expression
		sub.subscribers.forEach(client -> {
			try {
				sub.removeSubscriber(client);
			} catch (NullClientException | NonExistingClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	// INTERACTION TESTS

	@Test
	public void receiveMessagesIfHasEmail()
			throws NullClientException, ExistingClientException {

		// Check user email
		Client cliMock = mock(Client.class);
		when(cliMock.hasEmail()).thenReturn(true);

		// Check if user receives messages
		Message msg = mock(Message.class);
		sub.addSubscriber(cliMock);
		sub.sendMessage(msg);
		verify(cliMock, times(1)).receiveMessage(msg);
	}

	@Test
	public void dontReceiveMessageNoEmail()
			throws NullClientException, ExistingClientException {

		// Check user email
		Client cliMock = mock(Client.class);
		when(cliMock.hasEmail()).thenReturn(false);

		// Check if user receives messages
		Message msg = mock(Message.class);
		sub.addSubscriber(cliMock);
		sub.sendMessage(msg);
		verify(cliMock, times(0)).receiveMessage(msg);
	}

	@Test
	public void multipleUsersReceiveMessages()
			throws NullClientException, ExistingClientException {

		// Check user email
		Client cliMock1 = mock(Client.class);
		Client cliMock2 = mock(Client.class);
		when(cliMock1.hasEmail()).thenReturn(true);
		when(cliMock2.hasEmail()).thenReturn(true);

		// Check if user receives messages
		Message msg = mock(Message.class);
		sub.addSubscriber(cliMock1);
		sub.addSubscriber(cliMock2);
		sub.sendMessage(msg);
		verify(cliMock1, times(1)).receiveMessage(msg);
		verify(cliMock2, times(1)).receiveMessage(msg);
	}

	@Test
	public void unsubscribedClientDoesNotReceiveMessaged()
			throws NullClientException, ExistingClientException, NonExistingClientException {

		// Check user email
		Client cliMock1 = mock(Client.class);
		Client cliMock2 = mock(Client.class);
		when(cliMock1.hasEmail()).thenReturn(true);
		when(cliMock2.hasEmail()).thenReturn(true);

		// Check if user receives messages
		Message msg = mock(Message.class);
		sub.addSubscriber(cliMock1);
		sub.addSubscriber(cliMock2);
		sub.sendMessage(msg);
		sub.removeSubscriber(cliMock1);
		verify(cliMock1, times(0)).receiveMessage(msg);
		verify(cliMock2, times(1)).receiveMessage(msg);
	}
}

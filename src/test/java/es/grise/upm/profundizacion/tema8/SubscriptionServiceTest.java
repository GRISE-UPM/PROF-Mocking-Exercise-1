package es.grise.upm.profundizacion.tema8;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

	private SubscriptionService subscriptionService = new SubscriptionService();

	/**
	 * Error al añadir un Client null a la lista subscribers
	 * 
	 */
	@Test(expected = NullClientException.class)
	public void addNullClient() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(null);
	}

	/**
	 * Añadir un Client a subscribers.
	 * 
	 */
	@Test
	public void addClient() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);
		assertTrue(subscriptionService.subscribers.contains(client));
	}

	/**
	 * Anadir dos veces el mismo Client.
	 * 
	 * @throws ExistingClientException
	 */
	@Test(expected = ExistingClientException.class)
	public void addClientTwice() throws NullClientException, ExistingClientException {
		Client cliente;
		cliente = mock(Client.class);

		subscriptionService.addSubscriber(cliente);
		subscriptionService.addSubscriber(cliente);
	}

	/**
	 * Anadir varios Client a subscribers.
	 * 
	 */
	@Test
	public void addDifferentsClients() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		assertTrue(subscriptionService.subscribers.contains(client));
		assertTrue(subscriptionService.subscribers.contains(client2));
		assertTrue(subscriptionService.subscribers.contains(client3));
		assertTrue(subscriptionService.subscribers.size() == 3);
	}

	/**
	 * Eliminar Client null.
	 * 
	 * @throws NullClientException
	 */
	@Test(expected = NullClientException.class)
	public void deleteClientNull() throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.removeSubscriber(null);
	}

	/**
	 * Eliminar un Client inexistente.
	 * 
	 * @throws NonExistingClientException
	 */
	@Test(expected = NonExistingClientException.class)
	public void deleteClientNonExistent() throws NullClientException, NonExistingClientException {
		Client client = mock(Client.class);
		subscriptionService.removeSubscriber(client);
	}

	/**
	 * Eliminar Client de subscriber.
	 * 
	 */
	@Test
	public void deleteClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);
		assertTrue(subscriptionService.subscribers.size() == 0);
		assertFalse(subscriptionService.subscribers.contains(client));
	}

	/**
	 * Eliminar dos veces a Client.
	 * 
	 * @throws NonExistingClientException
	 */
	@Test(expected = NonExistingClientException.class)
	public void deleteClientTwice() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);
		subscriptionService.removeSubscriber(client);
	}

	/**
	 * Eliminar varios Client distintos.
	 * 
	 */
	@Test
	public void deleteDifferentsClients()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		Client client4 = mock(Client.class);
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		subscriptionService.addSubscriber(client4);
		assertTrue(subscriptionService.subscribers.size() == 4);

		subscriptionService.removeSubscriber(client);
		subscriptionService.removeSubscriber(client2);
		subscriptionService.removeSubscriber(client3);
		assertFalse(subscriptionService.subscribers.contains(client));
		assertFalse(subscriptionService.subscribers.contains(client2));
		assertFalse(subscriptionService.subscribers.contains(client3));
		assertTrue(subscriptionService.subscribers.size() == 1);
	}

	/**
	 * Elimitar todos los Client de subscriber.
	 * 
	 */
	@Test
	public void deleteAllClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		assertTrue(subscriptionService.subscribers.size() == 3);

		subscriptionService.removeSubscriber(client);
		subscriptionService.removeSubscriber(client2);
		subscriptionService.removeSubscriber(client3);
		assertFalse(subscriptionService.subscribers.contains(client));
		assertFalse(subscriptionService.subscribers.contains(client2));
		assertFalse(subscriptionService.subscribers.contains(client3));
		assertTrue(subscriptionService.subscribers.size() == 0);
	}

	/**
	 * Client recibe mensajes.
	 * 
	 */
	@Test
	public void receiveMessage() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(client);
		subscriptionService.sendMessage(message);

		verify(client).hasEmail();
		verify(client, times(1)).receiveMessage(message);
	}

	/**
	 * Client suscrito sin email no recibe mensaje.
	 * 
	 */
	@Test
	public void notHasEmail() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(false);
		subscriptionService.addSubscriber(client);
		subscriptionService.sendMessage(message);

		verify(client, never()).receiveMessage(message);
	}

	/**
	 * Varios Client reciben mensajes
	 * 
	 */
	@Test
	public void receiveMessageClients() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		Message message = mock(Message.class);

		when(client.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		when(client3.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		subscriptionService.sendMessage(message);

		verify(client).hasEmail();
		verify(client, times(1)).receiveMessage(message);
		verify(client2).hasEmail();
		verify(client2, times(1)).receiveMessage(message);
		verify(client3).hasEmail();
		verify(client3, times(1)).receiveMessage(message);
	}

	/**
	 * Cliente desuscrito no recibe mensaje.
	 */
	@Test
	public void notReceiveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);
		subscriptionService.sendMessage(message);

		verify(client, never()).hasEmail();
		verify(client, never()).receiveMessage(message);
	}

}

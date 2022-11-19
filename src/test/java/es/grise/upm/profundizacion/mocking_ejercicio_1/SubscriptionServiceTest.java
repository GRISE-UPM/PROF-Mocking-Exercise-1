package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class SubscriptionServiceTest {

	SubscriptionService subscriptionService;
	Client c1;
	Client c2;
	Message m;

	@BeforeEach
	public void init() {
		subscriptionService = new SubscriptionService();
		c1 = mock(Client.class);
		c2 = mock(Client.class);
		m = mock(Message.class);
	}

	// State tests
	@DisplayName("Test 1 -> No se puede añadir un Client null a la lista subscribers.")
	@Test
	public void addNullClient() throws NullClientException, ExistingClientException {
		assertThrows(NullClientException.class, () -> subscriptionService.addSubscriber(null));
	}

	@DisplayName("Test 2 -> Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.")
	@Test
	public void addClient() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(c1);

		assertTrue(subscriptionService.subscribers.contains(c1));
	}

	@DisplayName("Test 3 -> No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers."
			+ "Al hacerlo, se lanza la excepción ExistingClientException.")
	@Test
	public void addClientSameClient() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(c1);

		assertThrows(ExistingClientException.class, () -> subscriptionService.addSubscriber(c1));
	}

	@DisplayName("Test 4 -> Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.")
	@Test
	public void addClientSeveralClients() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(c1);
		subscriptionService.addSubscriber(c2);

		assertTrue(subscriptionService.subscribers.contains(c1));
		assertTrue(subscriptionService.subscribers.contains(c2));
	}

	@DisplayName("Test 5 -> No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. "
			+ "Al hacerlo, se lanza la excepción NullClientException.")
	@Test
	public void removeClientNull() throws NullClientException, ExistingClientException {
		assertThrows(NullClientException.class, () -> subscriptionService.removeSubscriber(null));
	}

	@DisplayName("Test 6 -> No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. "
			+ "Al hacerlo, se lanza la excepción NonExistingClientException.")
	@Test
	public void removeClientNotInListSubscribers() throws NullClientException, ExistingClientException {
		assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(c1));
	}

	@DisplayName("Test 7 -> Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.")
	@Test
	public void removeClientInListSubscribers()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(c1);
		subscriptionService.removeSubscriber(c1);

		assertFalse(subscriptionService.subscribers.contains(c1));
	}

	@DisplayName("Test 8 -> No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
	@Test
	public void removeClientSameClient()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(c1);
		subscriptionService.removeSubscriber(c1);

		assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(c1));
	}

	@DisplayName("Test 9 -> Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.)")
	@Test
	public void removeClientSeveralClients()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(c1);
		subscriptionService.addSubscriber(c2);

		subscriptionService.removeSubscriber(c1);

		assertFalse(subscriptionService.subscribers.contains(c1));
		assertTrue(subscriptionService.subscribers.contains(c2));
	}

	@DisplayName("Test 10 -> Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.")
	@Test
	public void removeClientAllClients()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(c1);
		subscriptionService.addSubscriber(c2);

		subscriptionService.removeSubscriber(c1);
		subscriptionService.removeSubscriber(c2);

		assertFalse(subscriptionService.subscribers.contains(c1));
		assertFalse(subscriptionService.subscribers.contains(c2));
	}

	// Interaction tests
	@DisplayName("Test 11 -> Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).")
	@Test
	public void receiveMessageSubscribeClient()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(c1);
		when(c1.hasEmail()).thenReturn(true);

		subscriptionService.sendMessage(m);
		verify(c1, times(1)).receiveMessage(m);
	}

	@DisplayName("Test 12 -> Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).")
	@Test
	public void receiveMessageNotSubscribeClient()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		when(c1.hasEmail()).thenReturn(false);

		subscriptionService.sendMessage(m);
		verify(c1, times(0)).receiveMessage(m);
	}

	@DisplayName("Test 13 -> Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).")
	@Test
	public void receiveMessageSeveralClients()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(c1);
		subscriptionService.addSubscriber(c2);
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);

		subscriptionService.sendMessage(m);
		verify(c1, times(1)).receiveMessage(m);
		verify(c2, times(1)).receiveMessage(m);
	}

	@DisplayName("Test 14 -> Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).")
	@Test
	public void receiveMessageNotSubscribe()
			throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(c1);
		when(c1.hasEmail()).thenReturn(false);

		subscriptionService.removeSubscriber(c1);
		subscriptionService.sendMessage(m);
		verify(c1, times(0)).receiveMessage(m);
	}
}

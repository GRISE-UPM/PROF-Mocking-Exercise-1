package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

	@InjectMocks
	SubscriptionService subscriptionService;

	@Mock
	Client client;
	
	@Mock
	Message message;

	// No se puede añadir un Client null a la lista subscribers.
	@Test
	public void addSubscriber1Test() throws NullClientException, ExistingClientException {
		assertThrows(NullClientException.class, () -> subscriptionService.addSubscriber(null));
	}

	// Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	@Test
	public void addSubscriber2Test() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(client);
		assertEquals(1, subscriptionService.subscribers.size());
	}

	// No se puede añadir dos veces el mismo Client mediante addSubscriber() a la
	// lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.
	@Test
	public void addSubscriber3Test() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(client);
		assertThrows(ExistingClientException.class, () -> subscriptionService.addSubscriber(client));
	}

	// Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	@Test
	public void addSubscriber4Test() throws NullClientException, ExistingClientException {
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		assertEquals(3, subscriptionService.subscribers.size());
	}

	// No se puede eliminar (usando removeSubscriber() un Client null de la lista
	// subscribers. Al hacerlo, se lanza la excepción NullClientException.
	@Test
	public void removeSubscriber1Test() throws NullClientException, NonExistingClientException {
		assertThrows(NullClientException.class, () -> subscriptionService.removeSubscriber(null));
	}

	// No se puede eliminar (usando removeSubscriber() un Client que no está
	// almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test
	public void removeSubscriber2Test() throws NullClientException, NonExistingClientException {
		assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client));
	}
	
	// Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
	@Test
	public void removeSubscriber3Test() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);
		
		assertEquals(0, subscriptionService.subscribers.size());
	}
	
	// No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de
	// la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test
	public void removeSubscriber4Test() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);
		assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client));
	}

	// Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.
	@Test
	public void removeSubscriber5Test() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		assertEquals(3, subscriptionService.subscribers.size());
		
		subscriptionService.removeSubscriber(client);
		subscriptionService.removeSubscriber(client2);
		
		assertEquals(1, subscriptionService.subscribers.size());
	}
	
	// Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.
	@Test
	public void removeSubscriber6Test() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client2 = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client2);
		
		assertEquals(2, subscriptionService.subscribers.size());
		
		subscriptionService.removeSubscriber(client);
		subscriptionService.removeSubscriber(client2);
		
		assertEquals(0, subscriptionService.subscribers.size());
	}
	
	// Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).
	@Test
	public void sendMessage1Test() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(client);
		when(client.hasEmail()).thenReturn(true);
		
		subscriptionService.sendMessage(message);
		
		verify(client, times(1)).receiveMessage(message);
	}
	
	// Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).
	@Test
	public void sendMessage2Test() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(client);
		when(client.hasEmail()).thenReturn(false);
		
		subscriptionService.sendMessage(message);
		
		verify(client, times(0)).receiveMessage(message);
	}
	
	// Varios Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).
	@Test
	public void sendMessage3Test() throws NullClientException, ExistingClientException {
		Client client2 = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client2);
		when(client.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		
		subscriptionService.sendMessage(message);
		
		verify(client, times(1)).receiveMessage(message);
		verify(client2, times(1)).receiveMessage(message);
	}
	
	// Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
	@Test
	public void sendMessage4Test() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);
		subscriptionService.sendMessage(message);
		
		verify(client, times(0)).receiveMessage(message);
	}
}

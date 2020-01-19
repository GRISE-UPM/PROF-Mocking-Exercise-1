package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	private SubscriptionService subscriptionService;
	
	@Before
	public void before() {
		subscriptionService = new SubscriptionService();
	}
	
	/* State tests */
	
	// No se puede añadir un Client null a la lista subscribers.
	@Test(expected = NullClientException.class)
	public void anaidirClienteNull() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(null);
	}
	
	
	// Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	@Test
	public void anaidirClienteBueno() throws NullClientException, ExistingClientException {
		Client cliente;
		cliente = mock(Client.class);
		
		subscriptionService.addSubscriber(cliente);
		
		assertTrue(subscriptionService.subscribers.contains(cliente));
	}
	
	
	// No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.
	@Test(expected = ExistingClientException.class)
	public void anaidirClienteRepetido() throws NullClientException, ExistingClientException {
		Client cliente;
		cliente = mock(Client.class);
		
		subscriptionService.addSubscriber(cliente);
		subscriptionService.addSubscriber(cliente);
	}
	
	
	// Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	@Test
	public void anaidirVariosClientes() throws NullClientException, ExistingClientException {
		Client cliente;
		Client cliente2;
		Client cliente3;
		
		cliente = mock(Client.class);
		cliente2 = mock(Client.class);
		cliente3 = mock(Client.class);
		
		subscriptionService.addSubscriber(cliente);
		subscriptionService.addSubscriber(cliente2);
		subscriptionService.addSubscriber(cliente3);
		
		assertTrue(subscriptionService.subscribers.contains(cliente));
		assertTrue(subscriptionService.subscribers.contains(cliente2));
		assertTrue(subscriptionService.subscribers.contains(cliente3));
		assertTrue(subscriptionService.subscribers.size() == 3);
	}

	
	// No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	@Test(expected = NullClientException.class)
	public void eliminarClienteNull() throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.removeSubscriber(null);
	}
	
	
	// No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
	public void eliminarClienteMalo() throws NullClientException, NonExistingClientException {
		Client cliente;
		cliente = mock(Client.class);

		subscriptionService.removeSubscriber(cliente);
	}
	
	
	// Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	@Test
	public void eliminarClienteBueno() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client cliente;
		Client cliente2;
		
		cliente = mock(Client.class);
		cliente2 = mock(Client.class);
		
		subscriptionService.addSubscriber(cliente);
		subscriptionService.addSubscriber(cliente2);
		subscriptionService.removeSubscriber(cliente2);
		
		assertTrue(subscriptionService.subscribers.size() == 1);
		assertFalse(subscriptionService.subscribers.contains(cliente2));
	}
	
	
	// No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
	public void eliminarClienteDosVeces() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client cliente;
		
		cliente = mock(Client.class);
		
		subscriptionService.addSubscriber(cliente);
		
		subscriptionService.removeSubscriber(cliente);
		subscriptionService.removeSubscriber(cliente);
	}
	
	
	// Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
	@Test
	public void eliminarVariosClientes() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client cliente;
		Client cliente2;
		Client cliente3;
		Client cliente4;
		
		cliente = mock(Client.class);
		cliente2 = mock(Client.class);
		cliente3 = mock(Client.class);
		cliente4 = mock(Client.class);
		
		subscriptionService.addSubscriber(cliente);
		subscriptionService.addSubscriber(cliente2);
		subscriptionService.addSubscriber(cliente3);
		subscriptionService.addSubscriber(cliente4);
		
		subscriptionService.removeSubscriber(cliente);
		subscriptionService.removeSubscriber(cliente2);
		subscriptionService.removeSubscriber(cliente3);
		
		assertFalse(subscriptionService.subscribers.contains(cliente));
		assertFalse(subscriptionService.subscribers.contains(cliente2));
		assertFalse(subscriptionService.subscribers.contains(cliente3));
		assertTrue(subscriptionService.subscribers.size() == 1);
	}
	
	
	// Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
	@Test
	public void eliminarTodosClientes() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client cliente;
		Client cliente2;
		Client cliente3;
		Client cliente4;
		
		cliente = mock(Client.class);
		cliente2 = mock(Client.class);
		cliente3 = mock(Client.class);
		cliente4 = mock(Client.class);
		
		subscriptionService.addSubscriber(cliente);
		subscriptionService.addSubscriber(cliente2);
		subscriptionService.addSubscriber(cliente3);
		subscriptionService.addSubscriber(cliente4);
		
		subscriptionService.removeSubscriber(cliente);
		subscriptionService.removeSubscriber(cliente2);
		subscriptionService.removeSubscriber(cliente3);
		subscriptionService.removeSubscriber(cliente4);
		
		assertFalse(subscriptionService.subscribers.contains(cliente));
		assertFalse(subscriptionService.subscribers.contains(cliente2));
		assertFalse(subscriptionService.subscribers.contains(cliente3));
		assertFalse(subscriptionService.subscribers.contains(cliente4));
		assertTrue(subscriptionService.subscribers.size() == 0);
	}
	

	/* Interaction tests */
	
	// Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
	@Test
	public void recibirMensajes() throws NullClientException, ExistingClientException {
		Client cliente;
		Message mensaje;
		
		cliente = mock(Client.class);
		mensaje = mock(Message.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(cliente);
		subscriptionService.sendMessage(mensaje);
		
		verify(cliente).hasEmail();
		verify(cliente,times(1)).receiveMessage(mensaje);
	}
	
	
	// Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
	@Test
	public void noRecibirMensajes() throws NullClientException, ExistingClientException {
		Client cliente;
		Message mensaje;
		
		cliente = mock(Client.class);
		mensaje = mock(Message.class);
		
		when(cliente.hasEmail()).thenReturn(false);
		subscriptionService.addSubscriber(cliente);
		subscriptionService.sendMessage(mensaje);
		
		verify(cliente,never()).receiveMessage(mensaje);
	}
	
	
	// Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
	@Test
	public void recibirVariosMensajes() throws NullClientException, ExistingClientException {
		Client cliente;
		Client cliente2;
		Message mensaje;
		
		cliente = mock(Client.class);
		cliente2 = mock(Client.class);
		mensaje = mock(Message.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(cliente);
		subscriptionService.addSubscriber(cliente2);
		
		subscriptionService.sendMessage(mensaje);
		
		verify(cliente).hasEmail();
		verify(cliente,times(1)).receiveMessage(mensaje);
		verify(cliente2).hasEmail();
		verify(cliente2,times(1)).receiveMessage(mensaje);
	}
	
	
	// Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
	@Test
	public void desuscribirNoMensajes() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client cliente;
		Client cliente2;
		Message mensaje;
		
		cliente = mock(Client.class);
		cliente2 = mock(Client.class);
		mensaje = mock(Message.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(cliente);
		subscriptionService.addSubscriber(cliente2);
		
		subscriptionService.removeSubscriber(cliente);
		subscriptionService.sendMessage(mensaje);
		
		verify(cliente, never()).hasEmail();
		verify(cliente, never()).receiveMessage(mensaje);
		verify(cliente2).hasEmail();
		verify(cliente2,times(1)).receiveMessage(mensaje);
	}
}

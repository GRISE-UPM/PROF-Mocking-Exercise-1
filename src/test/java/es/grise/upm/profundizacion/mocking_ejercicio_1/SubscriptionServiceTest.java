package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class SubscriptionServiceTest {
		
	// STATE TESTS
	
	/*
	 *  No se puede añadir un Client null a la lista subscribers.
	 */
	@Test
	public void testNullClient() {
		SubscriptionService subsService = new SubscriptionService();
		assertThrows(NullClientException.class, () -> subsService.addSubscriber(null));
	}
	
	
	/*
	 * Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	 */
	@Test
	public void testAddOneSuscriber() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client = mock(Client.class);
			
		subsService.addSubscriber(client);
		
		assertEquals(true, subsService.subscribers.contains(client));
	}
	
	/*
	 * No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers
	 * Al hacerlo, se lanza la excepción ExistingClientException.
	 */
	@Test
	public void testExistingClientException() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client = mock(Client.class);
		
		subsService.addSubscriber(client);
		
		assertThrows(ExistingClientException.class, () -> subsService.addSubscriber(client));
	}
	
	/*
	 * Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.
	 */
	@Test
	public void testAddSuscribers() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subsService.addSubscriber(client1);
		subsService.addSubscriber(client2);
		subsService.addSubscriber(client3);
		
		assertEquals(true, subsService.subscribers.contains(client1));
		assertEquals(true, subsService.subscribers.contains(client2));
		assertEquals(true, subsService.subscribers.contains(client3));
	}
	
	/*
	 * No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers.
	 * Al hacerlo, se lanza la excepción NullClientException.
	 */
	@Test
	public void testNonExistingClientException() {
		SubscriptionService subsService = new SubscriptionService();
		assertThrows(NullClientException.class, () -> subsService.removeSubscriber(null));
	}
	
	/*
	 * No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	@Test
	public void testRemoveNoSuscriber() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		
		subsService.addSubscriber(client1);
		
		assertThrows(NonExistingClientException.class, () -> subsService.removeSubscriber(client2));
	}
	
	/*
	 * Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
	 */
	@Test
	public void testRemoveOneSuscriber() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client = mock(Client.class);
		
		subsService.addSubscriber(client);
		subsService.removeSubscriber(client);
		
		assertEquals(false, subsService.subscribers.contains(client));
	}
	
	/*
	 * No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	@Test
	public void testRemoveSuscriberTwice() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client1 = mock(Client.class);
		
		subsService.addSubscriber(client1);
		subsService.removeSubscriber(client1);
		
		assertThrows(NonExistingClientException.class, () -> subsService.removeSubscriber(client1));
	}	
	
	/*
	 * Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.
	 */
	@Test
	public void testRemoveSuscribers() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subsService.addSubscriber(client1);
		subsService.addSubscriber(client2);
		subsService.addSubscriber(client3);
		
		subsService.removeSubscriber(client1);
		subsService.removeSubscriber(client2);
		
		assertEquals(false, subsService.subscribers.contains(client1));
		assertEquals(false, subsService.subscribers.contains(client2));
		assertEquals(true, subsService.subscribers.contains(client3));
	}
	
	/*
	 * Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.
	 */
	@Test
	public void testRemoveAllSuscribers() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subsService.addSubscriber(client1);
		subsService.addSubscriber(client2);
		subsService.addSubscriber(client3);
				
		subsService.removeSubscriber(client1);
		subsService.removeSubscriber(client2);
		subsService.removeSubscriber(client3);

		assertEquals(true, subsService.subscribers.isEmpty());
	
	}
	
	
	// INTERACTION TESTS
	
	/*
	 * Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).
	 */
	@Test
	public void testClientReceiveMessages() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client = mock(Client.class);
		Message msg = mock(Message.class);
		
		when(client.hasEmail()).thenReturn(true);

		subsService.addSubscriber(client);
		
		subsService.sendMessage(msg);
		subsService.sendMessage(msg);

		verify(client, times(2)).receiveMessage(msg);
	}

	/*
	 * Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).
	 */
	@Test
	public void testClientNotReceiveMessages() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client = mock(Client.class);
		Message msg = mock(Message.class);
		
		when(client.hasEmail()).thenReturn(false);

		subsService.addSubscriber(client);
		
		subsService.sendMessage(msg);
		verify(client, times(0)).receiveMessage(msg);
	}
	
	/*
	 * Varios Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).
	 */
	@Test
	public void testSeveralClientsReceiveMessages() throws Exception {
		SubscriptionService subsService = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Message msg = mock(Message.class);
		
		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);

		subsService.addSubscriber(client1);
		subsService.addSubscriber(client2);

		subsService.sendMessage(msg);

		verify(client1, times(1)).receiveMessage(msg);	
		verify(client2, times(1)).receiveMessage(msg);	
	}
	
	/*
	 * Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
	 */
	@Test
	public void testNoSuscriberMessageReception() throws Exception{
		SubscriptionService subsService = new SubscriptionService();
		Client client = mock(Client.class);
		Message msg = mock(Message.class);
		
		when(client.hasEmail()).thenReturn(true);

		subsService.addSubscriber(client);
		subsService.sendMessage(msg);
		subsService.removeSubscriber(client);
		subsService.sendMessage(msg);

		verify(client, times(1)).receiveMessage(msg);	
	}
	
}

package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class SubscriptionServiceTest {
	
	SubscriptionService sub;
	Client cli1;
	Client cli2;
	Client cli3;
	Message me;
	
	
	/*
	 * No se puede añadir un Client null a la lista subscribers.
	 */
	
	@Test
	public void testClienteNull(){
		sub = new SubscriptionService();
		assertThrows(NullClientException.class, () -> {sub.addSubscriber(null);});
	}
	
	/*
	 * Al añadir un Clientmediante addSubscriber(), éste Client se almacena en la lista subscribers.
	 */
	
	@Test
	public void testAniadirCliente() throws NullClientException, ExistingClientException {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		
		sub.addSubscriber(cli1);
		assertTrue(sub.subscribers.contains(cli1));
	}
	
	/*
	 * No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.
	 */
	
	@Test
	public void testAniadirDuplicado() throws NullClientException, ExistingClientException {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		
		sub.addSubscriber(cli1);
		assertThrows(ExistingClientException.class, () -> sub.addSubscriber(cli1));
	}

	/*
	 * Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.
	 */
	
	@Test
	public void testAniadirVariosClientes() throws NullClientException, ExistingClientException {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
	    cli2 = mock(Client.class);
	    cli3 = mock(Client.class);
	    
		Client[] clients = {cli1, cli2, cli3};
		for (Client cli: clients)
			sub.addSubscriber(cli);

		for (Client c: clients)
			assertTrue(sub.subscribers.contains(c));
	}
	
	/*
	 * No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	 */

	@Test
	public void testEliminarClientesNulos() throws NullClientException {
		sub = new SubscriptionService();
		
		assertThrows(NullClientException.class, () -> sub.removeSubscriber(null));
	}

	/*
	 * No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	
	@Test
	public void testEliminarClienteNoExiste() throws Exception {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		cli2 = mock(Client.class);
		
		sub.addSubscriber(cli1);
		assertThrows(NonExistingClientException.class, () -> sub.removeSubscriber(cli2));
	}
	
	/*
	 * Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
	 */

	@Test
	public void testEliminar() throws Exception {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		
		sub.addSubscriber(cli1);
		sub.removeSubscriber(cli1);
		assertTrue(sub.subscribers.isEmpty());
	}
	
	/*
	 * No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	 */

	@Test
	public void testEliminarClienteMismo() throws NonExistingClientException, NullClientException, ExistingClientException {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		
		sub.addSubscriber(cli1);
		sub.removeSubscriber(cli1);
		assertThrows(NonExistingClientException.class, () -> sub.removeSubscriber(cli1));
	}
	
	/*
	 * Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.
	 */

	@Test
	public void testEliminarVario() throws NullClientException, ExistingClientException, NonExistingClientException  {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		cli2= mock(Client.class);
		cli3 = mock(Client.class);
		
		Client[] clients = {cli1, cli2, cli3};
		for (Client c: clients)
			sub.addSubscriber(c);

		sub.removeSubscriber(cli1);
		sub.removeSubscriber(cli3);
		assertTrue(sub.subscribers.contains(cli2));
		assertTrue(sub.subscribers.size() == 1);
	}

	/*
	 * Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.
	 */
	
	@Test
	public void testEliminarTodo() throws NullClientException, ExistingClientException, NonExistingClientException {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		cli2= mock(Client.class);
		cli3 = mock(Client.class);
		
		Client[] clients = {cli1, cli2, cli3};
		for (Client c: clients)
			sub.addSubscriber(c);

		for (Client c: clients)
			sub.removeSubscriber(c);
		assertTrue(sub.subscribers.isEmpty());
	}
	
	/*
	 * Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).
	 */

	@Test 
	public void testRecibeMensaje() throws NullClientException, ExistingClientException{
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		me = mock(Message.class);
				
		sub.addSubscriber(cli1);
		when(cli1.hasEmail()).thenReturn(true);
		sub.sendMessage(me);
		verify(cli1, times(1)).receiveMessage(me);
	}
	
	/*
	 * Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).
	 */

	@Test
	public void testNoRecibeMensaje() throws NullClientException, ExistingClientException {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		me = mock(Message.class);
		
		sub.addSubscriber(cli1);
		when(cli1.hasEmail()).thenReturn(false);
		sub.sendMessage(me);
		verify(cli1, times(0)).receiveMessage(me);
	}
	
	/*
	 * Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).
	 */

	@Test
	public void testRecibeVarios() throws NullClientException, ExistingClientException {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		cli2 = mock(Client.class);
		cli3 = mock(Client.class);
		me = mock(Message.class);
		
		Client[] clients = {cli1, cli2, cli3};
		for (Client c: clients)
			sub.addSubscriber(c);

		when(cli1.hasEmail()).thenReturn(true);
		when(cli2.hasEmail()).thenReturn(false);
		when(cli3.hasEmail()).thenReturn(true);

		sub.sendMessage(me);
		verify(cli1, times(1)).receiveMessage(me);
		verify(cli2, times(0)).receiveMessage(me);
		verify(cli3, times(1)).receiveMessage(me);
	}
	
	/*
	 * Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
	 */

	@Test
	public void testEliminarSuscripcion() throws NullClientException, ExistingClientException, NonExistingClientException {
		sub = new SubscriptionService();
		cli1 = mock(Client.class);
		
		sub.addSubscriber(cli1);
		when(cli1.hasEmail()).thenReturn(true);
		
		sub.removeSubscriber(cli1);
		sub.sendMessage(me);
		verify(cli1, times(0)).receiveMessage(me);
	}
}

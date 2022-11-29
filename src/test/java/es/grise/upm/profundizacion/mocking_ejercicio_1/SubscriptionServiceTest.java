package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import es.grise.upm.profundizacion.mocking_ejercicio_1.Client;
import es.grise.upm.profundizacion.mocking_ejercicio_1.ExistingClientException;
import es.grise.upm.profundizacion.mocking_ejercicio_1.NonExistingClientException;
import es.grise.upm.profundizacion.mocking_ejercicio_1.NullClientException;
import es.grise.upm.profundizacion.mocking_ejercicio_1.SubscriptionService;


public class SubscriptionServiceTest {
	

	
	//STATE TEST
	
	//Comprobar que no se puede añadir un cliente nulo
	@Test
	public void cliente_null() {
		SubscriptionService servicio = new SubscriptionService();;
		
		assertThrows(NullClientException.class, ()-> servicio.addSubscriber(null));
		
	}
	
	
	/*
	 * Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	 */
	@Test
	public void cliente_add() throws NullClientException, ExistingClientException{
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);

		servicio.addSubscriber(cliente1);
		assertEquals(true, servicio.subscribers.contains(cliente1));
	}
	
	/*
	 * No se puede añadir dos veces el mismo Client mediante addSubscriber() a
	 * la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.
	 */
	@Test
	public void cliente_repetido() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);

		servicio.addSubscriber(cliente1);
		assertThrows(ExistingClientException.class, ()-> servicio.addSubscriber(cliente1));
	}
	
	/*
	 * Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers
	 */
	@Test
	public void cliente_add_varios() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		Client cliente2 = Mockito.mock(Client.class);
		Client cliente3 = Mockito.mock(Client.class);
		Client cliente4 = Mockito.mock(Client.class);
		
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.addSubscriber(cliente3);
		servicio.addSubscriber(cliente4);
		
		assertEquals(true, servicio.subscribers.contains(cliente1));
		assertEquals(true, servicio.subscribers.contains(cliente2));
		assertEquals(true, servicio.subscribers.contains(cliente3));
		assertEquals(true, servicio.subscribers.contains(cliente4));
	}
	
	/*
	 * No se puede eliminar (usando removeSubscriber() un Client null de la
	 * lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	 */
	@Test
	public void eliminar_cliente_null() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		assertThrows(NullClientException.class, ()-> servicio.removeSubscriber(null));
	}
	
	
	/*
	 * No se puede eliminar (usando removeSubscriber() un Client que no está almacenado
	 * en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
		@Test
	public void eliminar_cliente_no_almacenado() {
			SubscriptionService servicio = new SubscriptionService();;
			Client cliente2 = Mockito.mock(Client.class);
		
			assertThrows(NonExistingClientException.class, ()-> servicio.removeSubscriber(cliente2));
	}
		
	/*
	 * Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
	 */
	@Test
	public void eliminar_cliente_ok() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		
		servicio.addSubscriber(cliente1);
		servicio.removeSubscriber(cliente1);
		assertEquals(true, !servicio.subscribers.contains(cliente1)); 
	}
	/*
	 * No se puede eliminar (usando removeSubscriber() dos veces el mismo Client
	 * de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	@Test
	public void eliminar_cliente_2_veces() throws NullClientException, NonExistingClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		
		servicio.addSubscriber(cliente1);
		servicio.removeSubscriber(cliente1);
		assertThrows(NonExistingClientException.class, ()-> servicio.removeSubscriber(cliente1));
	}
	
	/*
	 * Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.
	 */
	@Test
	public void eliminar_varios_clientes() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		Client cliente2 = Mockito.mock(Client.class);
		Client cliente3 = Mockito.mock(Client.class);
		
		
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.addSubscriber(cliente3);
		
		servicio.removeSubscriber(cliente1);
		servicio.removeSubscriber(cliente2);
		servicio.removeSubscriber(cliente3);
		
		assertEquals(true, !servicio.subscribers.contains(cliente1));
		assertEquals(true, !servicio.subscribers.contains(cliente2));
		assertEquals(true, !servicio.subscribers.contains(cliente3));
		
	}
	
	/*
	 * Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.
	 */
	@Test
	public void eliminar_todos_clientes() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		Client cliente2 = Mockito.mock(Client.class);
		Client cliente3 = Mockito.mock(Client.class);
		
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.addSubscriber(cliente3);
		
		servicio.removeSubscriber(cliente1);
		servicio.removeSubscriber(cliente2);
		servicio.removeSubscriber(cliente3);
		
		assertEquals(true, !servicio.subscribers.contains(cliente1));
		assertEquals(true, !servicio.subscribers.contains(cliente2));
		assertEquals(true, !servicio.subscribers.contains(cliente3));
		assertEquals(true, servicio.subscribers.size()==0);
		
	}
	
	//INTERACTION TEST
	/*
	 * Un Client suscrito recibe mensajes (método receiveMessage()
	 * si tiene email (método hasEmail() == true).
	 */
	
	@Test
	public void recibir_mensaje() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		Message message = Mockito.mock(Message.class);
		
		servicio.addSubscriber(cliente1);
	
		when(cliente1.hasEmail()).thenReturn(true);
		
		servicio.sendMessage(message);
		verify(cliente1).receiveMessage(message);
	}
	
	/*
	 * Un Client suscrito no recibe mensajes (método receiveMessage()
	 * si no tiene email (método hasEmail() == false).
	 */
	@Test
	public void no_recibir_mensaje() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		Message message = Mockito.mock(Message.class);
		
		servicio.addSubscriber(cliente1);

		when(!cliente1.hasEmail()).thenReturn(false);
		
		servicio.sendMessage(message);
		//comprobar que no se ha llamado 
		verify(cliente1, times(0)).receiveMessage(message);
	}
	
	/*
	 * Varios  Client suscritos reciben mensajes (método receiveMessage()
	 * si tienen email (método hasEmail() == true).
	 */
	
	@Test
	public void recibir_mensaje_varios_clientes() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		Client cliente2 = Mockito.mock(Client.class);
		Client cliente3 = Mockito.mock(Client.class);
		Message message = Mockito.mock(Message.class);
		
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.addSubscriber(cliente3);
		
		when(cliente1.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		when(cliente3.hasEmail()).thenReturn(true);
		
		servicio.sendMessage(message);
		verify(cliente1).receiveMessage(message);
		verify(cliente2).receiveMessage(message);
		verify(cliente3).receiveMessage(message);
	}
	
	/*
	 * Al des-suscribir un Client éste no recibe mensajes (método receiveMessage())
	 */
	@Test
	public void no_recibir_msg_client_no_suscrito() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();;
		Client cliente1 = Mockito.mock(Client.class);
		Message message = Mockito.mock(Message.class);
		
		servicio.addSubscriber(cliente1);
		servicio.removeSubscriber(cliente1);
		
		servicio.sendMessage(message);
		verify(cliente1, times(0)).receiveMessage(message);
		
	}
	
}

package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	@Test(expected = NullClientException.class)
	public void clienteNulo() throws NullClientException, ExistingClientException {
		SubscriptionService service = new SubscriptionService();
		Client cliente = null;
		
		service.addSubscriber(cliente);
	}

 	@Test
	public void nuevoCliente() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente = mock(Client.class);
		
		servicio.addSubscriber(cliente);
		assertEquals(servicio.subscribers.size(),1);
		assertTrue(servicio.subscribers.contains(cliente));
	}

 	@Test(expected = ExistingClientException.class)
	public void clienteDuplicado() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente = mock(Client.class);
		
		servicio.addSubscriber(cliente);
		servicio.addSubscriber(cliente);
	}

 	@Test
	public void nuevosClientes() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		Client cliente3 = mock(Client.class);
		
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.addSubscriber(cliente3);
		
		assertEquals(servicio.subscribers.size(),3);
		assertTrue(servicio.subscribers.contains(cliente1));
		assertTrue(servicio.subscribers.contains(cliente2));
		assertTrue(servicio.subscribers.contains(cliente3));
	}

 	@Test(expected = NullClientException.class)
	public void eliminarClienteNulo() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente = null;
		
		servicio.removeSubscriber(cliente);
	}

 	@Test(expected = NonExistingClientException.class)
	public void eliminarClienteNoExiste() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente = mock(Client.class);
		
		servicio.removeSubscriber(cliente);
	}

 	@Test
	public void eliminarCliente() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente = mock(Client.class);
		
		servicio.addSubscriber(cliente);
		servicio.removeSubscriber(cliente);
		assertFalse(servicio.subscribers.contains(cliente));
	}

 	@Test(expected = NonExistingClientException.class)
	public void eliminarMismoCliente() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente = mock(Client.class);
		
		servicio.addSubscriber(cliente);
		servicio.removeSubscriber(cliente);
		servicio.removeSubscriber(cliente);
	}

 	@Test
	public void eliminarClientes() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		Client cliente3 = mock(Client.class);
		
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.addSubscriber(cliente3);
		servicio.removeSubscriber(cliente1);
		servicio.removeSubscriber(cliente3);
		assertFalse(servicio.subscribers.contains(cliente1));
		assertTrue(servicio.subscribers.contains(cliente2));
		assertFalse(servicio.subscribers.contains(cliente3));
	}

 	@Test
	public void elimnarTodosClientes() throws NullClientException, ExistingClientException, NonExistingClientException {
 		SubscriptionService servicio = new SubscriptionService();
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		Client cliente3 = mock(Client.class);
		
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.addSubscriber(cliente3);
		servicio.removeSubscriber(cliente1);
		servicio.removeSubscriber(cliente2);
		servicio.removeSubscriber(cliente3);
		assertEquals(servicio.subscribers.size(),0);
	}

 	@Test
	public void recibirMensaje() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente = mock(Client.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		Message message = mock(Message.class);
		servicio.addSubscriber(cliente);
		servicio.sendMessage(message);
		verify(cliente).receiveMessage(message);
	}

 	@Test
	public void noRecibirMensaje() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente = mock(Client.class);
		
		when(cliente.hasEmail()).thenReturn(false);
		Message message = mock(Message.class);
		servicio.addSubscriber(cliente);
		servicio.sendMessage(message);
		verify(cliente, never()).receiveMessage(message);
	}

 	@Test
	public void recibirMensajes() throws NullClientException, ExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		
		when(cliente1.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		Message message = mock(Message.class);
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.sendMessage(message);
		verify(cliente1).receiveMessage(message);
		verify(cliente2).receiveMessage(message);
	}

 	@Test
	public void noRecibirMensajeClienteDesuscrito() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService servicio = new SubscriptionService();
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		
		when(cliente1.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		Message message = mock(Message.class);
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.removeSubscriber(cliente1);
		servicio.sendMessage(message);
		verify(cliente1, never()).receiveMessage(message);
		verify(cliente2).receiveMessage(message);
	}
	
}

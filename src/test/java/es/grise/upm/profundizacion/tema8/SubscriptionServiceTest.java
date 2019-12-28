package es.grise.upm.profundizacion.tema8;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

public class SubscriptionServiceTest {
	
	SubscriptionService subscripcion = new SubscriptionService();
	
	@Test(expected = NullClientException.class)
	public void noClientesNull() throws NullClientException, ExistingClientException {
		
		Client cliente = null;
		
		subscripcion.addSubscriber(cliente);
	}
	
	
	@Test
	public void introducirSubscriptorCorrectamente() throws NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		subscripcion.addSubscriber(cliente);
		
		assertTrue(subscripcion.subscribers.contains(cliente));
		
	}
		
	
	@Test(expected = ExistingClientException.class)
	public void noRepetir2VecesElSubscriptor() throws NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		
		subscripcion.addSubscriber(cliente);
		subscripcion.addSubscriber(cliente);
	}
	
	@Test
	public void introducirSubscriptoresCorrectamente() throws NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		Client cliente2 = mock(Client.class);
		subscripcion.addSubscriber(cliente);
		subscripcion.addSubscriber(cliente2);
		
		//compruebo solo el cliente2 porque el cliente1 ya lo comprobé hace 2 tests (introducirSubscriptorCorrectamente)
		assertTrue(subscripcion.subscribers.contains(cliente2));
		
	}
	
	@Test(expected = NullClientException.class)
	public void noBorrarUnSubscriptorNull() throws NullClientException, NonExistingClientException {
		
		Client cliente = null;
		
		subscripcion.subscribers.add(cliente);
		subscripcion.removeSubscriber(cliente);

	}
	
	
	@Test(expected = NonExistingClientException.class)
	public void noBorrarUnSubscriptorInexistente() throws NullClientException, NonExistingClientException {
		
		Client cliente = mock(Client.class);
		
		subscripcion.removeSubscriber(cliente);

	}
	
	@Test
	public void eliminarSubscriptorCorrectamente() throws NonExistingClientException, NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		
		subscripcion.addSubscriber(cliente);
		
		subscripcion.removeSubscriber(cliente);
		
		assertTrue(subscripcion.subscribers.isEmpty());
		
	}
	
	
	@Test(expected = NonExistingClientException.class)
	public void noBorrarUnSubscriptor2Veces() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Client cliente = mock(Client.class);
		subscripcion.addSubscriber(cliente);
		
		subscripcion.removeSubscriber(cliente);
		subscripcion.removeSubscriber(cliente);

	}
	
	@Test
	public void eliminarSubscriptoresCorrectamente() throws NonExistingClientException, NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		Client cliente2 = mock(Client.class);
		Client cliente3 = mock(Client.class);
		
		subscripcion.addSubscriber(cliente);
		subscripcion.addSubscriber(cliente2);
		subscripcion.addSubscriber(cliente3);
		
		subscripcion.removeSubscriber(cliente);
		subscripcion.removeSubscriber(cliente2);
		
		//solo queda el cliente3
		assertTrue(subscripcion.subscribers.size()==1 && subscripcion.subscribers.contains(cliente3));
		
	}
	
	@Test
	public void eliminarTodosLosSubscriptoresCorrectamente() throws NonExistingClientException, NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		Client cliente2 = mock(Client.class);
		
		subscripcion.addSubscriber(cliente);
		subscripcion.addSubscriber(cliente2);
		
		subscripcion.removeSubscriber(cliente);
		subscripcion.removeSubscriber(cliente2);
		
		assertTrue(subscripcion.subscribers.isEmpty());
		
	}
	
	@Test
	public void recibirMensajeSiTieneEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Message message = mock(Message.class);
		Client cliente = mock(Client.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		
		subscripcion.addSubscriber(cliente);
		subscripcion.sendMessage(message);
		
		verify(cliente, times(1)).receiveMessage(message);
	}
	
	@Test
	public void noRecibirMensajeSiNoHayEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Message message = mock(Message.class);
		Client cliente = mock(Client.class);
		
		//Por defecto, cuando se llame al metodo "hasEmail()" mockito devolvera false
		
		subscripcion.addSubscriber(cliente);
		subscripcion.sendMessage(message);
		
		verify(cliente, times(0)).receiveMessage(message);
	}
	
	
	@Test
	public void recibirMensajeSiTienenEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Message message = mock(Message.class);
		Client cliente = mock(Client.class);
		Client cliente2 = mock(Client.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		
		subscripcion.addSubscriber(cliente);
		subscripcion.addSubscriber(cliente2);
		subscripcion.sendMessage(message);
		
		verify(cliente, times(1)).receiveMessage(message);
		verify(cliente2, times(1)).receiveMessage(message);
	}
	
	@Test
	public void noRecibirMensajePorDesuscripcion() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Message message = mock(Message.class);
		Client cliente = mock(Client.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		
		subscripcion.addSubscriber(cliente);
		subscripcion.removeSubscriber(cliente);
		
		subscripcion.sendMessage(message);
		
		verify(cliente, times(0)).receiveMessage(message);
	}
	
	
}

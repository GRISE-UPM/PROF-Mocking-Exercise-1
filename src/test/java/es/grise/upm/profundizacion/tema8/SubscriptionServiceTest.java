package es.grise.upm.profundizacion.tema8;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
	
	private SubscriptionService sut = null;
	
	@Before
	public void init() {
		sut = new SubscriptionService();
	}
	
	@Test (expected = NullClientException.class)
	public void noSePuedeAnadirClienteNull() throws NullClientException, ExistingClientException {
		sut.addSubscriber(null);
	}
	
	@Test 
	public void clienteSeAlmacenaEnLaLista() throws NullClientException, ExistingClientException  {
		
		Client cliente = mock(Client.class);
		
		sut.addSubscriber(cliente);
		
		assertTrue(sut.subscribers.contains(cliente));
	}
	
	@Test (expected = ExistingClientException.class)
	public void noSePudeAnadirDosVecesUnMismoCliente() throws NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		
		sut.addSubscriber(cliente);
		sut.addSubscriber(cliente);
	}
	
	@Test 
	public void variosClientesSeAlmacenanEnLaLista() throws NullClientException, ExistingClientException  {
		
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		
		sut.addSubscriber(cliente1);
		sut.addSubscriber(cliente2);
		
		assertTrue(sut.subscribers.contains(cliente1));
		assertTrue(sut.subscribers.contains(cliente2));
	}
	
	@Test (expected = NullClientException.class)
	public void noSePuedeEliminarClienteNull() throws NullClientException, NonExistingClientException {
		sut.removeSubscriber(null);
	}
	
	@Test (expected = NonExistingClientException.class)
	public void noSePuedeEliminarClienteNoAlmacenado() throws NullClientException, NonExistingClientException {
		
		Client cliente = mock(Client.class);
		
		sut.removeSubscriber(cliente);
	}
	
	@Test
	public void sePuedeEliminarClienteAlmacenado() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		
		sut.addSubscriber(cliente);
		sut.removeSubscriber(cliente);
		
		assertTrue(!sut.subscribers.contains(cliente));
	}
	
	@Test (expected = NonExistingClientException.class)
	public void noSePuedeEliminarDosVecesClienteAlmacenado() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		
		sut.addSubscriber(cliente);
		sut.removeSubscriber(cliente);
		sut.removeSubscriber(cliente);
	}
	
	@Test
	public void sePuedeEliminarVariosClientesAlmacenados() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		Client cliente3 = mock(Client.class);
		
		sut.addSubscriber(cliente1);
		sut.addSubscriber(cliente2);
		sut.addSubscriber(cliente3);
		sut.removeSubscriber(cliente1);
		sut.removeSubscriber(cliente2);
		
		assertTrue(!sut.subscribers.contains(cliente1));
		assertTrue(!sut.subscribers.contains(cliente2));
	}
	
	@Test
	public void sePuedeEliminarTodosClientesAlmacenados() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		Client cliente3 = mock(Client.class);
		
		sut.addSubscriber(cliente1);
		sut.addSubscriber(cliente2);
		sut.addSubscriber(cliente3);
		sut.removeSubscriber(cliente1);
		sut.removeSubscriber(cliente2);
		sut.removeSubscriber(cliente3);
		
		assertTrue(sut.subscribers.isEmpty());
	}
	
	@Test
	public void clienteRecibeMensajeSiTieneEmail() throws NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		Message mensaje = mock(Message.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		
		sut.addSubscriber(cliente);
		sut.sendMessage(mensaje);
		
		verify(cliente).receiveMessage(mensaje);
	}
	
	@Test
	public void clienteNoRecibeMensajeSiNoTieneEmail() throws NullClientException, ExistingClientException {
		
		Client cliente = mock(Client.class);
		Message mensaje = mock(Message.class);
		
		sut.addSubscriber(cliente);
		sut.sendMessage(mensaje);
		
		verify(cliente, times(0)).receiveMessage(mensaje);
	}
	
	@Test
	public void variosClientesRecibenMensajeSiTienenEmail() throws NullClientException, ExistingClientException {
		
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
		Message mensaje = mock(Message.class);
		
		when(cliente1.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		
		sut.addSubscriber(cliente1);
		sut.addSubscriber(cliente2);
		sut.sendMessage(mensaje);
		
		verify(cliente1).receiveMessage(mensaje);
		verify(cliente2).receiveMessage(mensaje);
	}
	
	@Test
	public void clienteDesSuscritoNoRecibeMasMensajes() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Client cliente = mock(Client.class);
		Message mensaje = mock(Message.class);
		
		when(cliente.hasEmail()).thenReturn(true);
		
		sut.addSubscriber(cliente);
		sut.removeSubscriber(cliente);
		sut.sendMessage(mensaje);
		
		verify(cliente, times(0)).receiveMessage(mensaje);
	}
}

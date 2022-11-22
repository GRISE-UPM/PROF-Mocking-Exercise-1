package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class SubscriptionServiceTest {
	
	@Test
	public void aniadirUno() throws NullClientException, ExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
		
		Client pepe = mock(Client.class);
		ss.addSubscriber(pepe);
		if(!ss.subscribers.contains(pepe)) {
			fail("el cliente no se aniade bien");
		}
	}
	@Test
	public void aniadirVarios() throws NullClientException, ExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
		
		Client pepe = mock(Client.class);
		Client manu = mock(Client.class);

		ss.addSubscriber(pepe);
		if(!ss.subscribers.contains(pepe)) {
			fail("el cliente no se aniade bien");
		}
		ss.addSubscriber(manu);
		if(!ss.subscribers.contains(pepe) && !ss.subscribers.contains(manu)) {
			fail("No se aniaden varios clientes bien");
		}
		
	}
	@Test
	public void aniadirNulo() {
		SubscriptionService ss =  new SubscriptionService();
		assertThrows(NullClientException.class,() -> { ss.addSubscriber(null); });
	}
	@Test
	public void aniadirExistente() throws NullClientException, ExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
		Client pepe = mock(Client.class);
		ss.addSubscriber(pepe);
		assertThrows(ExistingClientException.class,() -> { ss.addSubscriber(pepe); });
	}
	@Test
	public void eliminarUno_y_Todos() throws NullClientException, NonExistingClientException, ExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
		
		Client juan = mock(Client.class);
		ss.addSubscriber(juan);
		ss.removeSubscriber(juan);
		if(ss.subscribers.contains(juan)) {
			fail("el cliente no se elimina bien");
		}
	}
	@Test
	public void eliminarNulo() {
		SubscriptionService ss =  new SubscriptionService();
		assertThrows(NullClientException.class,() -> { ss.removeSubscriber(null); });
	}
	@Test
	public void eliminarNoExistente() throws NullClientException, NonExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
		
		Client juan = mock(Client.class);
		assertThrows(NonExistingClientException.class,() -> { ss.removeSubscriber(juan); });
	}
	@Test
	public void enviarVariosEmails() throws NullClientException, ExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
		
		Client pepe = mock(Client.class);
		Client manu = mock(Client.class);
		
		Message mensaje = mock(Message.class);
		
		when(pepe.hasEmail()).thenReturn(true);
		when(manu.hasEmail()).thenReturn(true);
		
		ss.addSubscriber(manu);
		ss.addSubscriber(pepe);
		
		ss.sendMessage(mensaje);
		
		verify(manu).receiveMessage(mensaje);
		verify(pepe).receiveMessage(mensaje);
		
		
	}
	@Test
	public void enviarUnEmail() throws NullClientException, ExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
	
		Client pepe = mock(Client.class);
		Message mensaje = mock(Message.class);
		when(pepe.hasEmail()).thenReturn(true);
		ss.addSubscriber(pepe);
		ss.sendMessage(mensaje);
		
		verify(pepe).receiveMessage(mensaje);
		
	}
	@Test
	public void noEnviarUnEmail() throws NullClientException, ExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
	
		Client pepe = mock(Client.class);
		Message mensaje = mock(Message.class);
		when(pepe.hasEmail()).thenReturn(false);
		ss.addSubscriber(pepe);
		ss.sendMessage(mensaje);
		
		verify(pepe).hasEmail();
		verifyNoMoreInteractions(pepe);	
	}
	
	@Test
	public void noEnviarUnEmailTrasDes() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService ss =  new SubscriptionService();
	
		Client pepe = mock(Client.class);
		Message mensaje = mock(Message.class);
		when(pepe.hasEmail()).thenReturn(true);
		ss.addSubscriber(pepe);
		ss.removeSubscriber(pepe);
		
		ss.sendMessage(mensaje);

		verifyNoMoreInteractions(pepe);	
	}
	
	
}

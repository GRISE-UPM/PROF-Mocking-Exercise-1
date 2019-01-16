package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	private Client cliente,cliente2,cliente3;
	private Message mensaje;
	private SubscriptionService ss;
	
	@Before
	public void before(){
		cliente = mock(Client.class);
		cliente2 = mock(Client.class);
		cliente3 = mock(Client.class);
		mensaje = mock(Message.class);
		ss = new SubscriptionService();	
	}
	
	@Test(expected = NullClientException.class)
	public void state_test1() throws NullClientException, ExistingClientException{
		ss.addSubscriber(null);
	}
	
	@Test
	public void state_test2() throws NullClientException, ExistingClientException{
		ss.addSubscriber(cliente);
		assertTrue(ss.subscribers.contains(cliente));
	}
	
	@Test(expected=ExistingClientException.class)
	public void state_test3() throws NullClientException, ExistingClientException{
		ss.addSubscriber(cliente);
		ss.addSubscriber(cliente);
	}
	
	@Test
	public void state_test4() throws NullClientException, ExistingClientException{
		ss.addSubscriber(cliente);
		ss.addSubscriber(cliente2);
		assertTrue(ss.subscribers.contains(cliente)&&ss.subscribers.contains(cliente2));
	}
	
	@Test(expected=NullClientException.class)
	public void state_test5() throws NullClientException, NonExistingClientException{
		ss.removeSubscriber(null);
	}
	
	@Test(expected=NonExistingClientException.class)
	public void state_test6() throws NullClientException, NonExistingClientException{
		ss.removeSubscriber(cliente);
	}
	
	@Test
	public void state_test7() throws NullClientException, ExistingClientException, NonExistingClientException{
		ss.addSubscriber(cliente);
		ss.removeSubscriber(cliente);
		assertTrue(!ss.subscribers.contains(cliente));
	}
	
	@Test(expected=NonExistingClientException.class)
	public void state_test8() throws NullClientException, ExistingClientException, NonExistingClientException{
		ss.addSubscriber(cliente);
		ss.removeSubscriber(cliente);
		ss.removeSubscriber(cliente);
	}
	
	
	@Test()
	public void state_test9() throws NullClientException, ExistingClientException, NonExistingClientException{
		ss.addSubscriber(cliente);
		ss.addSubscriber(cliente2);
		ss.removeSubscriber(cliente);
		ss.removeSubscriber(cliente2);
		assertEquals(ss.subscribers.size(),0);
	}
	
	@Test()
	public void state_test10() throws NullClientException, ExistingClientException, NonExistingClientException{
		ss.addSubscriber(cliente);
		ss.addSubscriber(cliente2);
		ss.addSubscriber(cliente3);
		ss.removeSubscriber(cliente);
		ss.removeSubscriber(cliente2);
		ss.removeSubscriber(cliente3);
		assertEquals(ss.subscribers.size(),0);
	}
	
	@Test()
	public void interaction_test1() throws NullClientException, ExistingClientException{
		when(cliente.hasEmail()).thenReturn(true);
		ss.addSubscriber(cliente);
		ss.sendMessage(mensaje);
		verify(cliente).receiveMessage(mensaje);
	}
	
	@Test()
	public void interaction_test2() throws NullClientException, ExistingClientException{
		when(cliente.hasEmail()).thenReturn(false);
		ss.addSubscriber(cliente);
		ss.sendMessage(mensaje);
		verify(cliente,never()).receiveMessage(mensaje);
	}
	
	@Test()
	public void interaction_test3() throws NullClientException, ExistingClientException{
		when(cliente.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		ss.addSubscriber(cliente);
		ss.addSubscriber(cliente2);
		ss.sendMessage(mensaje);
		verify(cliente).receiveMessage(mensaje);
		verify(cliente2).receiveMessage(mensaje);
	}
	
	@Test()
	public void interaction_test4() throws NullClientException, ExistingClientException, NonExistingClientException{
		when(cliente.hasEmail()).thenReturn(false);
		ss.addSubscriber(cliente);
		ss.removeSubscriber(cliente);
		ss.sendMessage(mensaje);
		verify(cliente,never()).receiveMessage(mensaje);
	}
	
	

}

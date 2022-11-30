package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import es.grise.upm.profundizacion.mocking_ejercicio_1.Client;

public class SubscriptionServiceTest {
	
	@Test
	public void add_null_client_test() {
		SubscriptionService ss = new SubscriptionService();
		assertThrows(NullClientException.class,()->ss.addSubscriber(null));
	}
	
	@Test
	public void add_non_null_client_test() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		es.grise.upm.profundizacion.mocking_ejercicio_1.Client c = mock(Client.class);
		ss.addSubscriber(c);
		assertEquals(true,ss.subscribers.contains(c));
	}
	
	@Test
	public void add_same_client_twice_test() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		assertThrows(ExistingClientException.class,()->ss.addSubscriber(c));
	}
	
	@Test
	public void add_more_than_1_subscriber_test() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		
		assertEquals(true,ss.subscribers.contains(c1));
		assertEquals(true,ss.subscribers.contains(c2));
	}
	
	@Test
	public void remove_null_subscriber_test() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		assertThrows(NullClientException.class,()->ss.removeSubscriber(null));
	}
	
	@Test
	public void remove_non_existing_subscriber_test() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		ss.addSubscriber(c1);
		assertThrows(NonExistingClientException.class,()->ss.removeSubscriber(c2));
	}
	
	@Test
	public void remove_existing_subscriber_test() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		assertEquals(true,ss.subscribers.contains(c));
		ss.removeSubscriber(c);
		assertEquals(false,ss.subscribers.contains(c));
	}
	
	@Test
	public void remove_same_subscriber_twice_test() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		ss.removeSubscriber(c);
		assertThrows(NonExistingClientException.class,()->ss.removeSubscriber(c));
	}
	
	@Test
	public void remove_several_clients_test() throws NullClientException, ExistingClientException, NonExistingClientException{
		SubscriptionService ss = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		ss.removeSubscriber(c1);
		ss.removeSubscriber(c2);
		assertEquals(false,ss.subscribers.contains(c1));
		assertEquals(false,ss.subscribers.contains(c2));
		assertEquals(true,ss.subscribers.contains(c3));
	}
	
	@Test
	public void remove_all_subscribers_test() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		ss.removeSubscriber(c1);
		ss.removeSubscriber(c2);
		ss.removeSubscriber(c3);
		assertEquals(0,ss.subscribers.size());
	}
	
	//TESTS DE INTERACCIÓN
	@Test
	public void subscribed_client_with_email_message_test() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c = mock(Client.class);
		Message m = mock(Message.class);
		
		when(c.hasEmail()).thenReturn(true);
		ss.addSubscriber(c);
		
		ss.sendMessage(m);
		verify(c).receiveMessage(m);
		
		
	}
	
	@Test
	public void subscribed_client_without_email_message_test() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c = mock(Client.class);
		Message m = mock(Message.class);
		
		when(c.hasEmail()).thenReturn(false);
		ss.addSubscriber(c);
		
		ss.sendMessage(m);
		verify(c,times(0)).receiveMessage(m);
	}
	
	@Test
	public void subscribed_clients_with_email_message_test() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c1= mock(Client.class);
		Client c2= mock(Client.class);
		Client c3= mock(Client.class);
		
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		when(c3.hasEmail()).thenReturn(true);
		
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		
		Message m = mock(Message.class);
		ss.sendMessage(m);
		
		verify(c1).receiveMessage(m);
		verify(c2).receiveMessage(m);
		verify(c3).receiveMessage(m);
	}
	
	@Test
	public void desuscribed_client_receives_no_message() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		Client c= mock(Client.class);
		when(c.hasEmail()).thenReturn(true);
		ss.addSubscriber(c);
		ss.removeSubscriber(c);
		
		
		Message m = mock(Message.class);
		ss.sendMessage(m);
		verify(c,times(0)).receiveMessage(m);
	}
	
}

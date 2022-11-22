package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class SubscriptionServiceTest {
	
	@Test
	public void add_null_to_subscribers_test() {
		Client c = null;
		SubscriptionService s = new SubscriptionService();
		assertThrows(NullClientException.class, () -> s.addSubscriber(c));
	}
	
	@Test
	public void check_if_client_added() throws NullClientException, ExistingClientException{
		Client c = mock(Client.class);
		SubscriptionService s = new SubscriptionService();
		s.addSubscriber(c);
		assertTrue(s.subscribers.contains(c));
	}
	
	@Test
	public void add_same_client_twice() throws NullClientException, ExistingClientException{
		Client c = mock(Client.class);
		SubscriptionService s = new SubscriptionService();
		s.addSubscriber(c);
		assertThrows(ExistingClientException.class, () -> s.addSubscriber(c));
	}
	
	@Test
	public void add_multiple_clients() throws NullClientException, ExistingClientException{
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		SubscriptionService s = new SubscriptionService();
		s.addSubscriber(c1);
		s.addSubscriber(c2);
		assertTrue(s.subscribers.contains(c1));
		assertTrue(s.subscribers.contains(c2));
	}
	
	@Test
	public void remove_null_client() {
		Client c = null;
		SubscriptionService s = new SubscriptionService();
		assertThrows(NullClientException.class, () -> s.removeSubscriber(c));
	}
	
	@Test
	public void remove_one_client() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client c = mock(Client.class);
		SubscriptionService s = new SubscriptionService();
		s.addSubscriber(c);
		s.removeSubscriber(c);
		assertTrue(!s.subscribers.contains(c));
	}
	
	@Test
	public void remove_twice() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client c = mock(Client.class);
		SubscriptionService s = new SubscriptionService();
		s.addSubscriber(c);
		s.removeSubscriber(c);
		assertThrows(NonExistingClientException.class, () -> s.removeSubscriber(c));
	}
	
	@Test
	public void remove_multiple_clients() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		Client c4 = mock(Client.class);
		SubscriptionService s = new SubscriptionService();
		s.addSubscriber(c1);
		s.addSubscriber(c2);
		s.addSubscriber(c3);
		s.addSubscriber(c4);
		s.removeSubscriber(c2);
		s.removeSubscriber(c4);
		assertTrue(s.subscribers.contains(c1));
		assertTrue(!s.subscribers.contains(c2));
		assertTrue(s.subscribers.contains(c3));
		assertTrue(!s.subscribers.contains(c4));
	}
	
	public void remove_all_clients() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		Client c4 = mock(Client.class);
		SubscriptionService s = new SubscriptionService();
		s.addSubscriber(c1);
		s.addSubscriber(c2);
		s.addSubscriber(c3);
		s.addSubscriber(c4);
		s.removeSubscriber(c1);
		s.removeSubscriber(c2);
		s.removeSubscriber(c3);
		s.removeSubscriber(c4);
		assertTrue(!s.subscribers.contains(c1));
		assertTrue(!s.subscribers.contains(c2));
		assertTrue(!s.subscribers.contains(c3));
		assertTrue(!s.subscribers.contains(c4));
	}
	
	@Test
	public void receive_message() throws NullClientException, ExistingClientException{
		SubscriptionService s = new SubscriptionService();
		Client c = mock(Client.class);
		when(c.hasEmail()).thenReturn(true);
		Message m = mock(Message.class);
		s.addSubscriber(c);
		s.sendMessage(m);
		verify(c, times(1)).receiveMessage(m);
	}
	
	@Test
	public void receive_no_message() throws NullClientException, ExistingClientException{
		SubscriptionService s = new SubscriptionService();
		Client c = mock(Client.class);
		when(c.hasEmail()).thenReturn(false);
		Message m = mock(Message.class);
		s.addSubscriber(c);
		s.sendMessage(m);
		verify(c, never()).receiveMessage(m);
	}
	
	@Test
	public void receive_multiple_message() throws NullClientException, ExistingClientException{
		SubscriptionService s = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		when(c3.hasEmail()).thenReturn(true);
		Message m = mock(Message.class);
		s.addSubscriber(c1);
		s.addSubscriber(c2);
		s.addSubscriber(c3);
		s.sendMessage(m);
		verify(c1, times(1)).receiveMessage(m);
		verify(c2, times(1)).receiveMessage(m);
		verify(c3, times(1)).receiveMessage(m);
	}
	
	@Test
	public void unsubscribe_and_receive_no_message() throws NullClientException, ExistingClientException, NonExistingClientException{
		SubscriptionService s = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		when(c3.hasEmail()).thenReturn(true);
		Message m = mock(Message.class);
		s.addSubscriber(c1);
		s.addSubscriber(c2);
		s.addSubscriber(c3);
		s.removeSubscriber(c2);
		s.sendMessage(m);
		verify(c1, times(1)).receiveMessage(m);
		verify(c2, never()).receiveMessage(m);
		verify(c3, times(1)).receiveMessage(m);
	}
}

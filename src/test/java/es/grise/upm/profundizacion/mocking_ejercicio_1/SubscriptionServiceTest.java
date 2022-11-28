package es.grise.upm.profundizacion.mocking_ejercicio_1;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Test;
public class SubscriptionServiceTest {
	private SubscriptionService sub = new SubscriptionService();
	@Test
	public void add_null_test() {
		Client c = null;
		
		assertThrows(NullClientException.class, () -> sub.addSubscriber(c));
	}

	@Test
	public void check_client_added() throws NullClientException, ExistingClientException{
		Client c = mock(Client.class);
		sub.addSubscriber(c);
		assertTrue(sub.subscribers.contains(c));
	}

	@Test
	public void add_client_twice() throws NullClientException, ExistingClientException{
		Client c = mock(Client.class);
		sub.addSubscriber(c);
		assertThrows(ExistingClientException.class, () -> sub.addSubscriber(c));
	}

	@Test
	public void add_multiple_clients() throws NullClientException, ExistingClientException{
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		
		sub.addSubscriber(c1);
		sub.addSubscriber(c2);
		assertTrue(sub.subscribers.contains(c1));
		assertTrue(sub.subscribers.contains(c2));
	}

	@Test
	public void remove_null() {
		Client c = null;
		assertThrows(NullClientException.class, () -> sub.removeSubscriber(c));
	}

	@Test
	public void remove_client() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client c = mock(Client.class);
		sub.addSubscriber(c);
		sub.removeSubscriber(c);
		assertTrue(!sub.subscribers.contains(c));
	}

	@Test
	public void remove_twice() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client c = mock(Client.class);
		sub.addSubscriber(c);
		sub.removeSubscriber(c);
		assertThrows(NonExistingClientException.class, () -> sub.removeSubscriber(c));
	}

	@Test
	public void remove_multiple_clients() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		sub.addSubscriber(c1);
		sub.addSubscriber(c2);
		sub.addSubscriber(c3);
		sub.removeSubscriber(c2);
		assertTrue(sub.subscribers.contains(c1));
		assertTrue(!sub.subscribers.contains(c2));
		assertTrue(sub.subscribers.contains(c3));
	}

	public void remove_all() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		SubscriptionService s = new SubscriptionService();
		s.addSubscriber(c1);
		s.addSubscriber(c2);
		s.addSubscriber(c3);
		s.removeSubscriber(c1);
		s.removeSubscriber(c2);
		s.removeSubscriber(c3);
		assertTrue(!s.subscribers.contains(c1));
		assertTrue(!s.subscribers.contains(c2));
		assertTrue(!s.subscribers.contains(c3));
	}

	@Test
	public void receive_message() throws NullClientException, ExistingClientException{
		
		Client c = mock(Client.class);
		when(c.hasEmail()).thenReturn(true);
		Message m = mock(Message.class);
		sub.addSubscriber(c);
		sub.sendMessage(m);
		verify(c, times(1)).receiveMessage(m);
	}

	@Test
	public void receive_no_message() throws NullClientException, ExistingClientException{
		
		Client c = mock(Client.class);
		when(c.hasEmail()).thenReturn(false);
		Message m = mock(Message.class);
		sub.addSubscriber(c);
		sub.sendMessage(m);
		verify(c, never()).receiveMessage(m);
	}

	@Test
	public void receive_multiple_message() throws NullClientException, ExistingClientException{
		
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		when(c3.hasEmail()).thenReturn(true);
		Message m = mock(Message.class);
		sub.addSubscriber(c1);
		sub.addSubscriber(c2);
		sub.addSubscriber(c3);
		sub.sendMessage(m);
		verify(c1, times(1)).receiveMessage(m);
		verify(c2, times(1)).receiveMessage(m);
		verify(c3, times(1)).receiveMessage(m);
	}

	@Test
	public void unsubscribe_and_receive_no_message() throws NullClientException, ExistingClientException, NonExistingClientException{
		
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		when(c3.hasEmail()).thenReturn(true);
		Message m = mock(Message.class);
		sub.addSubscriber(c1);
		sub.addSubscriber(c2);
		sub.addSubscriber(c3);
		sub.sendMessage(m);
		verify(c1, times(1)).receiveMessage(m);
		verify(c2, times(1)).receiveMessage(m);
		verify(c3, times(1)).receiveMessage(m);
	}
}
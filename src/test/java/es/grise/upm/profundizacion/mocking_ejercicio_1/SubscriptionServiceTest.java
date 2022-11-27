package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class SubscriptionServiceTest {
	
	SubscriptionService s;
	Client c1;
	Client c2;
	Client c3;
	Message m;

	@Before
	public void init() {
		s = new SubscriptionService();
		c1 = Mockito.mock(Client.class);
		c2 = Mockito.mock(Client.class);
		c3 = Mockito.mock(Client.class);
		m = Mockito.mock(Message.class);
	}
	
	// STATE TESTS
	
	@Test
	public void addNullClientTest() {
		assertThrows(NullClientException.class, () -> s.addSubscriber(null));
	}
	
	@Test
	public void addClientTest() throws Exception {
		s.addSubscriber(c1);
		assertTrue(s.subscribers.contains(c1));
	}
	
	@Test
	public void addSameClientTest() throws Exception {
		s.addSubscriber(c1);
		assertThrows(ExistingClientException.class, () -> s.addSubscriber(c1));
	}
	
	@Test
	public void addSeveralClientTest() throws Exception {
		Client[] clients = {c1, c2, c3};
		for (Client c: clients)
			s.addSubscriber(c);
		
		for (Client c: clients)
			assertTrue(s.subscribers.contains(c));
	}
	
	@Test
	public void removeNullClient() throws Exception {
		assertThrows(NullClientException.class, () -> s.removeSubscriber(null));
	}
	
	@Test
	public void removeNonExistingClientTest() throws Exception {
		s.addSubscriber(c1);
		assertThrows(NonExistingClientException.class, () -> s.removeSubscriber(c2));
	}
	
	@Test
	public void removeClientTest() throws Exception {
		s.addSubscriber(c1);
		s.removeSubscriber(c1);
		assertTrue(s.subscribers.isEmpty());
	}
	
	@Test
	public void removeSameClientTest() throws Exception {
		s.addSubscriber(c1);
		s.removeSubscriber(c1);
		assertThrows(NonExistingClientException.class, () -> s.removeSubscriber(c1));
	}
	
	@Test
	public void removeSeveralClientTest() throws Exception {
		Client[] clients = {c1, c2, c3};
		for (Client c: clients)
			s.addSubscriber(c);
		
		s.removeSubscriber(c1);
		s.removeSubscriber(c3);
		assertTrue(s.subscribers.contains(c2));
		assertTrue(s.subscribers.size() == 1);
	}
	
	@Test
	public void removeAllClientTest() throws Exception {
		Client[] clients = {c1, c2, c3};
		for (Client c: clients)
			s.addSubscriber(c);
		
		for (Client c: clients)
			s.removeSubscriber(c);
		assertTrue(s.subscribers.isEmpty());
	}
	
	// INTERACTION TESTS
	
	@Test 
	public void clientReceivesMessageTest() throws Exception{
		s.addSubscriber(c1);
		when(c1.hasEmail()).thenReturn(true);
		s.sendMessage(m);
		verify(c1, times(1)).receiveMessage(m);
	}
	
	@Test
	public void clientDontReceiveMessageTest() throws Exception {
		s.addSubscriber(c1);
		when(c1.hasEmail()).thenReturn(false);
		s.sendMessage(m);
		verify(c1, times(0)).receiveMessage(m);
	}
	
	@Test
	public void clientsReceiveMessagesTest() throws Exception {
		Client[] clients = {c1, c2, c3};
		for (Client c: clients)
			s.addSubscriber(c);
		
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(false);
		when(c3.hasEmail()).thenReturn(true);
		
		s.sendMessage(m);
		verify(c1, times(1)).receiveMessage(m);
		verify(c2, times(0)).receiveMessage(m);
		verify(c3, times(1)).receiveMessage(m);
	}
	
	@Test
	public void removeSubscriptionTest() throws Exception {
		s.addSubscriber(c1);
		when(c1.hasEmail()).thenReturn(true);
		s.removeSubscriber(c1);
		s.sendMessage(m);
		verify(c1, times(0)).receiveMessage(m);
	}
		
}

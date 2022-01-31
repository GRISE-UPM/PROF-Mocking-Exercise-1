package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;



public class SubscriptionServiceTest {
	
	SubscriptionService service;


	@Before
	public void setUp() throws Exception {
		service = new SubscriptionService();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test(expected = NullClientException.class)
	public void nullUser() throws NullClientException, ExistingClientException
	{
		service.addSubscriber(null);
	}
	
	@Test
	public void addUser() throws NullClientException, ExistingClientException {
		Client c1 = mock(Client.class);
		service.addSubscriber(c1);
		assertTrue(service.subscribers.contains(c1));
	}
	
	@Test(expected = ExistingClientException.class)
	public void sameUser() throws NullClientException, ExistingClientException {
		Client c1 = mock(Client.class);
		service.addSubscriber(c1);
		service.addSubscriber(c1);
	}
	
	@Test
	public void differentUser() throws NullClientException, ExistingClientException {
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		service.addSubscriber(c1);
		service.addSubscriber(c2);
		assertTrue(service.subscribers.contains(c1) && service.subscribers.contains(c2));
	}
	
	@Test (expected = NullClientException.class)
	public void nullDeletion() throws NullClientException, NonExistingClientException {
		service.removeSubscriber(null);
		
	}
	
	@Test (expected = NonExistingClientException.class)
	public void deleteNotExistingClient() throws NullClientException, NonExistingClientException {
		Client c1 = mock(Client.class);
		service.removeSubscriber(c1);

	}
	
	@Test
	public void deleteExistingUser() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c1 = mock(Client.class);
		service.addSubscriber(c1);
		service.removeSubscriber(c1);
		assertFalse(service.subscribers.contains(c1));
		
	}
	
	@Test(expected = NonExistingClientException.class)
	public void deleteSameExistingUser() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c1 = mock(Client.class);
		service.addSubscriber(c1);
		service.removeSubscriber(c1);
		service.removeSubscriber(c1);
		
	}
	@Test
	public void deleteDifferentUser() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		service.addSubscriber(c1);
		service.addSubscriber(c2);
		service.removeSubscriber(c1);
		service.removeSubscriber(c2);
		assertFalse(service.subscribers.contains(c1) && service.subscribers.contains(c2));
	}
	
	@Test
	public void clientSubscriberReceiveMessage() throws NullClientException, ExistingClientException {
		
		Client c1 = mock(Client.class);
		Message message = mock(Message.class);
		service.addSubscriber(c1);
		when(c1.hasEmail()).thenReturn(true);
		service.sendMessage(message);
		verify(c1).receiveMessage(message);
	}
	@Test
	public void clientNoReceiveMessage() throws NullClientException, ExistingClientException {
		
		Client c1 = mock(Client.class);
		Message message = mock(Message.class);
		service.addSubscriber(c1);
		when(c1.hasEmail()).thenReturn(false);
		service.sendMessage(message);
		verify(c1,times(0)).receiveMessage(message);
	}
	@Test
	public void clientsReceiveMessage() throws NullClientException, ExistingClientException {
		
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Message message = mock(Message.class);
		service.addSubscriber(c1);
		service.addSubscriber(c2);
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		service.sendMessage(message);
		verify(c1).receiveMessage(message);
		verify(c2).receiveMessage(message);
	}
	@Test
	public void clientDesuscribeNoReceiveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Client c1 = mock(Client.class);
		Message message = mock(Message.class);
		service.addSubscriber(c1);
		service.removeSubscriber(c1);
		service.sendMessage(message);
		verify(c1,never()).receiveMessage(message);
	}
		
}

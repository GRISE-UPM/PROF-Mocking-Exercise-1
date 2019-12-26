package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	private SubscriptionService subscriptionService;
	
	@Before
	public void setUp() {
		
		subscriptionService = new SubscriptionService();
	}
	
	@Test(expected = NullClientException.class)
	public void shouldNotAddNullClient() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(null);
	}
	
	@Test
	public void shouldAddClientCorrectly() throws NullClientException, ExistingClientException{
		
		Client client = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		
		assertEquals(1, subscriptionService.subscribers.size());
		assertTrue(subscriptionService.subscribers.contains(client));
	}
	
	@Test(expected = ExistingClientException.class)
	public void shouldNotAddClientTwice() throws NullClientException, ExistingClientException{
		
		Client client = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client);
	}
	
	@Test
	public void shouldAddSeveralClientCorrectly() throws NullClientException, ExistingClientException{
		
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		assertEquals(3, subscriptionService.subscribers.size());
		assertTrue(subscriptionService.subscribers.contains(client1));
		assertTrue(subscriptionService.subscribers.contains(client2));
		assertTrue(subscriptionService.subscribers.contains(client3));
	}
	
	@Test(expected = NullClientException.class)
	public void shouldNotRemoveNullClient() throws NullClientException, NonExistingClientException  {
		subscriptionService.removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void shouldNotRemoveUnexistingClient() throws NullClientException, NonExistingClientException  {
		
		Client client = mock(Client.class);

		subscriptionService.removeSubscriber(client);
	}
	
	@Test
	public void shouldRemoveExistingClient() throws NullClientException, NonExistingClientException, ExistingClientException  {
		
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);
		
		subscriptionService.removeSubscriber(client);
		
		assertEquals(0, subscriptionService.subscribers.size());
		assertFalse(subscriptionService.subscribers.contains(client));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void shouldNotRemoveExistingClientTwice() throws NullClientException, NonExistingClientException, ExistingClientException  {
		
		Client client = mock(Client.class);
		subscriptionService.addSubscriber(client);
		
		subscriptionService.removeSubscriber(client);
		subscriptionService.removeSubscriber(client);
	}
	
	@Test
	public void shouldRemoveSeveralClientsCorrectly() throws NullClientException, ExistingClientException, NonExistingClientException{
		
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
		
		assertEquals(1, subscriptionService.subscribers.size());
		assertFalse(subscriptionService.subscribers.contains(client1));
		assertFalse(subscriptionService.subscribers.contains(client2));
		assertTrue(subscriptionService.subscribers.contains(client3));
	}
	
	@Test
	public void shouldRemoveAllClientsCorrectly() throws NullClientException, ExistingClientException, NonExistingClientException{
		
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
		subscriptionService.removeSubscriber(client3);
		
		assertEquals(0, subscriptionService.subscribers.size());
		assertFalse(subscriptionService.subscribers.contains(client1));
		assertFalse(subscriptionService.subscribers.contains(client2));
		assertFalse(subscriptionService.subscribers.contains(client3));
	}
}

package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SubscriptionServiceTest {
	SubscriptionService subsService = new SubscriptionService();
	
	//Mocks
	private Client clientOK = mock(Client.class);
	private Client clientOK2 = mock(Client.class);
	
	@Test (expected = NullClientException.class)
	public void cantAddNullClient() throws NullClientException, ExistingClientException{
		this.subsService.addSubscriber(null);
	}
	
	@Test
	public void clientCorrectlyAdded() throws NullClientException, ExistingClientException{
		this.subsService.addSubscriber(clientOK);
		assertEquals(true, this.subsService.subscribers.contains(clientOK));
	}
	
	@Test (expected = ExistingClientException.class)
	public void clientAddedTwoTimesReturnException() throws NullClientException, ExistingClientException{
		this.subsService.addSubscriber(clientOK);
		this.subsService.addSubscriber(clientOK);
		assertEquals(true, this.subsService.subscribers.contains(clientOK));
	}
	
	@Test
	public void variousClientsCorrectlyAdded() throws NullClientException, ExistingClientException{
		this.subsService.addSubscriber(clientOK);
		this.subsService.addSubscriber(clientOK2);
		assertEquals(2, this.subsService.subscribers.size());
		assertEquals(true, this.subsService.subscribers.contains(clientOK));
		assertEquals(true, this.subsService.subscribers.contains(clientOK2));
	}
}

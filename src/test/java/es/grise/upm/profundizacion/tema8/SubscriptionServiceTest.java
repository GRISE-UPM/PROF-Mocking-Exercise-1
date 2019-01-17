package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SubscriptionServiceTest {
	SubscriptionService subsService = new SubscriptionService();
	
	//Mocks
	private Client clientOK = mock(Client.class);
	private Client clientOK2 = mock(Client.class);
	private Message mes = mock(Message.class);
	
	// STATE TESTS
	
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
	
	@Test (expected = NullClientException.class)
	public void cantRemoveNullClient() throws NullClientException, NonExistingClientException{
		this.subsService.removeSubscriber(null);
	}
	
	@Test (expected = NonExistingClientException.class)
	public void removeNonExisingThrowsException() throws NullClientException, NonExistingClientException{
		this.subsService.removeSubscriber(clientOK);
	}
	
	@Test
	public void clientCorrectlyRemoved() throws NullClientException, ExistingClientException, NonExistingClientException{
		this.subsService.addSubscriber(clientOK);
		this.subsService.removeSubscriber(clientOK);
		assertEquals(false, this.subsService.subscribers.contains(clientOK));
	}
	
	@Test (expected = NonExistingClientException.class)
	public void removTwoTimesThrowsException() throws NullClientException, ExistingClientException, NonExistingClientException{
		this.subsService.addSubscriber(clientOK);
		this.subsService.removeSubscriber(clientOK);
		this.subsService.removeSubscriber(clientOK);
	}
	
	@Test 
	public void variousClientsCorrectlyRemovedAndAllClients() throws NullClientException, ExistingClientException, NonExistingClientException{
		this.subsService.addSubscriber(clientOK);
		this.subsService.addSubscriber(clientOK2);
		this.subsService.removeSubscriber(clientOK);
		this.subsService.removeSubscriber(clientOK2);
		assertEquals(0, this.subsService.subscribers.size());
		assertEquals(false, this.subsService.subscribers.contains(clientOK));
		assertEquals(false, this.subsService.subscribers.contains(clientOK2));
	}
	
	// INTERACTION TESTS
	
	@Test
	public void oneClientReceiveCorrectlyMessages() throws NullClientException, ExistingClientException{
		when(this.clientOK.hasEmail()).thenReturn(true);
		this.subsService.addSubscriber(clientOK);
		this.subsService.sendMessage(mes);
		verify(clientOK,times(1)).receiveMessage(mes);
	}
	
	@Test
	public void notMailClientDontReceive() throws NullClientException, ExistingClientException{
		when(this.clientOK.hasEmail()).thenReturn(false);
		this.subsService.addSubscriber(clientOK);
		this.subsService.sendMessage(mes);
		verify(clientOK,times(0)).receiveMessage(mes);
	}
	
	@Test
	public void variousClientsReceiveCorrectlyMessages() throws NullClientException, ExistingClientException{
		when(this.clientOK.hasEmail()).thenReturn(true);
		this.subsService.addSubscriber(clientOK);
		when(this.clientOK2.hasEmail()).thenReturn(true);
		this.subsService.addSubscriber(clientOK2);
		this.subsService.sendMessage(mes);
		verify(clientOK,times(1)).receiveMessage(mes);
		verify(clientOK2,times(1)).receiveMessage(mes);
	}
	
	@Test
	public void unsubscribedClientDontReceive() throws NullClientException, ExistingClientException, NonExistingClientException{
		when(this.clientOK.hasEmail()).thenReturn(true);
		this.subsService.addSubscriber(clientOK);
		this.subsService.sendMessage(mes);
		this.subsService.sendMessage(mes);
		verify(clientOK,times(2)).receiveMessage(mes);
		this.subsService.removeSubscriber(clientOK);
		this.subsService.sendMessage(mes);
		verify(clientOK,times(2)).receiveMessage(mes);
	}
}

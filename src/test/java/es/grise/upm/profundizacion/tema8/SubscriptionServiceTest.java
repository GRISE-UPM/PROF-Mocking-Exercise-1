package es.grise.upm.profundizacion.tema8;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Iterator;

public class SubscriptionServiceTest {
	//SUT whose behavior we want to test (we need the real instance here, not a double)
	SubscriptionService subsService = new SubscriptionService();
	
	
	//STATE TESTS
	
	@Test(expected = NullClientException.class)
	public void cannotAddaNullClientToSubscribersList() throws NullClientException, ExistingClientException {
		subsService.addSubscriber(null);
	}
	
	@Test
	public void clientActuallyAddedToSubscriberList() throws NullClientException, ExistingClientException {
		Client dummyClient = mock(Client.class);
		
		subsService.addSubscriber(dummyClient);
		
		assertTrue(subsService.subscribers.contains(dummyClient));
	}
	
	@Test(expected = ExistingClientException.class)
	public void cannotAddTheSameClientTwice() throws NullClientException, ExistingClientException {
		Client dummyClient = mock(Client.class);
		subsService.addSubscriber(dummyClient);
		subsService.addSubscriber(dummyClient);
	}
	
	@Test
	public void severalClientsAreCorrectlyAddedToSubscribers() throws NullClientException, ExistingClientException {
		Client dummyClient1 = mock(Client.class);
		Client dummyClient2 = mock(Client.class);
		subsService.addSubscriber(dummyClient1);
		subsService.addSubscriber(dummyClient2);
		
		assertTrue(subsService.subscribers.contains(dummyClient1));
		assertTrue(subsService.subscribers.contains(dummyClient2));
	}
	
	@Test(expected = NullClientException.class)
	public void cannotRemoveANullClient() throws NullClientException, NonExistingClientException {
		subsService.removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void cannotRemoveANonExistingClient() throws NullClientException, NonExistingClientException {
		Client dummyClient = mock(Client.class);
		
		subsService.removeSubscriber(dummyClient);
	}
	
	@Test
	public void canRemoveAPreviouslyAddedClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client dummyClient = mock(Client.class);
		
		subsService.addSubscriber(dummyClient);
		subsService.removeSubscriber(dummyClient);
		assertFalse(subsService.subscribers.contains(dummyClient));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void cannotRemoveAClientTwice() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client dummyClient = mock(Client.class);
		
		subsService.addSubscriber(dummyClient);
		subsService.removeSubscriber(dummyClient);
		subsService.removeSubscriber(dummyClient);
	}
	
	@Test
	public void canRemoveSeveralClientsPreviouslyAddedToSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client dummyClient1 = mock(Client.class);
		Client dummyClient2 = mock(Client.class);
		
		subsService.addSubscriber(dummyClient1);
		subsService.addSubscriber(dummyClient2);
		
		subsService.removeSubscriber(dummyClient1);
		subsService.removeSubscriber(dummyClient2);
	}
	
	@Test
	public void canRemoveAllClientsFromSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client dummyClient1 = mock(Client.class);
		Client dummyClient2 = mock(Client.class);
		
		subsService.addSubscriber(dummyClient1);
		subsService.addSubscriber(dummyClient2);
		
		for (Iterator<Client> i = subsService.subscribers.iterator(); i.hasNext();) {
			Client client = i.next();
			
			subsService.removeSubscriber(client);
		}
	}
	
	//INTERACTION TESTS
	
	@Test
	public void aSubscribedClientReceivesMessagesIfItHasAnEmail() throws NullClientException, ExistingClientException {
		Client dummyClient = mock(Client.class);
		Message msg = mock(Message.class);
		
		subsService.addSubscriber(dummyClient);
		when(dummyClient.hasEmail()).thenReturn(true);
		
		subsService.sendMessage(msg);
		
		verify(dummyClient).receiveMessage(msg);
	}
	
	@Test
	public void aSubscribedClientDoesntReceiveMessagesIfNoEmail() throws NullClientException, ExistingClientException {
		Client dummyClient = mock(Client.class);
		Message msg = mock(Message.class);
		
		subsService.addSubscriber(dummyClient);
		when(dummyClient.hasEmail()).thenReturn(false);
		
		subsService.sendMessage(msg);
		
		verify(dummyClient, times(0)).receiveMessage(msg);
	}
	
	@Test
	public void severalSubscribedClientsReceiveMessagesIfTheyHaveAnEmail() throws NullClientException, ExistingClientException {
		Client dummyClient1 = mock(Client.class);
		Client dummyClient2 = mock(Client.class);
		
		Message msg1 = mock(Message.class);
		Message msg2 = mock(Message.class);
		
		subsService.addSubscriber(dummyClient1);
		subsService.addSubscriber(dummyClient2);
		
		when(dummyClient1.hasEmail()).thenReturn(true);
		when(dummyClient2.hasEmail()).thenReturn(true);
		
		subsService.sendMessage(msg1);
		subsService.sendMessage(msg2);
		
		verify(dummyClient1).receiveMessage(msg1);
		verify(dummyClient2).receiveMessage(msg2);
	}
	
	@Test
	public void anUnSubscribedClientDoesntReceiveMessages() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client dummyClient = mock(Client.class);
		Message msg = mock(Message.class);
		
		subsService.addSubscriber(dummyClient);
		subsService.removeSubscriber(dummyClient);
		
		verify(dummyClient, never()).receiveMessage(msg);
	}
}

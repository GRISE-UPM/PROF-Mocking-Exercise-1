package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class SubscriptionServiceTest {
	
	@Mock
	private Client client;
	
	@Mock
	private Message message;
	
	private SubscriptionService service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = new SubscriptionService();
	}
	
	//State tests
	
	@Test(expected = NullClientException.class)
	public void canNotAddNullClient() throws NullClientException, ExistingClientException {
		service.addSubscriber(null);
	}
	
	@Test
	public void canAddClient() throws NullClientException, ExistingClientException {
		service.addSubscriber(client);
		Assert.assertTrue(service.subscribers.contains(client));
	}
	
	@Test(expected = ExistingClientException.class)
	public void canNotAddTwiceAClient() throws NullClientException, ExistingClientException {
		service.addSubscriber(client);
		service.addSubscriber(client);
	}
	
	@Test
	public void canAddMoreThanOneClient() throws NullClientException, ExistingClientException {
		service.addSubscriber(client);
		Client clientTwo = mock(Client.class);
		service.addSubscriber(clientTwo);
		Assert.assertTrue(service.subscribers.contains(client));
		Assert.assertTrue(service.subscribers.contains(clientTwo));
	}
	
	@Test(expected = NullClientException.class)
	public void canNotRemoveNullClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		service.removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void canNotRemoveNonSavedClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client clientTwo = mock(Client.class);
		service.removeSubscriber(clientTwo);
	}
	
	@Test
	public void canRemoveClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		service.addSubscriber(client);
		Assert.assertTrue(service.subscribers.contains(client));
		service.removeSubscriber(client);
		Assert.assertTrue(!service.subscribers.contains(client));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void canNotRemoveTwiceClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		service.addSubscriber(client);
		Assert.assertTrue(service.subscribers.contains(client));
		service.removeSubscriber(client);
		service.removeSubscriber(client);
	}
	
	@Test
	public void canRemoveTwoClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		service.addSubscriber(client);
		Client clientTwo = mock(Client.class);
		service.addSubscriber(clientTwo);
		Assert.assertTrue(service.subscribers.contains(client));
		Assert.assertTrue(service.subscribers.contains(clientTwo));
		service.removeSubscriber(client);
		service.removeSubscriber(clientTwo);
		Assert.assertTrue(!service.subscribers.contains(client));
		Assert.assertTrue(!service.subscribers.contains(clientTwo));
	}
	
	@Test
	public void canRemoveAllClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		service.addSubscriber(client);
		Client clientTwo = mock(Client.class);
		service.addSubscriber(clientTwo);
		Assert.assertTrue(service.subscribers.contains(client));
		Assert.assertTrue(service.subscribers.contains(clientTwo));
		service.removeSubscriber(client);
		service.removeSubscriber(clientTwo);
		Assert.assertTrue(service.subscribers.size()==0);
	}
	
	//Interaction tests
	
	@Test
	public void ShouldReceiveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
		given(client.hasEmail()).willReturn(true);
		service.addSubscriber(client);
		service.sendMessage(message);
		verify(client).receiveMessage(message);
	}

	@Test
	public void ShouldNotReceiveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
		given(client.hasEmail()).willReturn(false);
		service.addSubscriber(client);
		service.sendMessage(message);
		verify(client, times(0)).receiveMessage(message);
	}
	
	@Test
	public void ShouldReceiveMessageTwoClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		given(client.hasEmail()).willReturn(true);
		Client clientTwo = mock(Client.class);
		given(clientTwo.hasEmail()).willReturn(true);
		service.addSubscriber(client);
		service.addSubscriber(clientTwo);
		service.sendMessage(message);
		verify(client).receiveMessage(message);
		verify(clientTwo).receiveMessage(message);
	}
	
	@Test
	public void ShouldNotReceiveMessageWhenRemoved() throws NullClientException, ExistingClientException, NonExistingClientException {
		given(client.hasEmail()).willReturn(true);
		service.addSubscriber(client);
		service.removeSubscriber(client);
		service.sendMessage(message);
		verify(client, times(0)).receiveMessage(message);
	}

}

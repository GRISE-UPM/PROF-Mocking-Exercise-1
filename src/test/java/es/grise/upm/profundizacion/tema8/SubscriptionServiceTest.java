package es.grise.upm.profundizacion.tema8;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

	private SubscriptionService ss;
	private Client mockClient1 = mock(Client.class);
	private Client mockClient2 = mock(Client.class);
	private Message msg = mock(Message.class);

	@Before
	public void before() {
		ss = new SubscriptionService();
	}

	@Test(expected = NullClientException.class)
	public void ExceptionOnNullClient() throws ExistingClientException, NullClientException {
		ss.addSubscriber(null);
	}

	@Test
	public void ClientAddedInList() throws ExistingClientException, NullClientException {
		ss.addSubscriber(mockClient1);
		assert(ss.subscribers.contains(mockClient1));
	}

	@Test(expected = ExistingClientException.class)
	public void ExceptionOnRepeatedClient() throws ExistingClientException, NullClientException {
		ss.addSubscriber(mockClient1);
		ss.addSubscriber(mockClient1);
	}

	@Test
	public void SeveralClientsAddedInList() throws ExistingClientException, NullClientException {
		ss.addSubscriber(mockClient1);
		ss.addSubscriber(mockClient2);
		assert(ss.subscribers.contains(mockClient1) && ss.subscribers.contains(mockClient2));
	}

	@Test(expected = NullClientException.class)
	public void ExceptionOnDeleteNullClient() throws NonExistingClientException, NullClientException {
		ss.removeSubscriber(null);
	}

	@Test(expected = NonExistingClientException.class)
	public void ExceptionOnDeleteNotExistingClient() throws NonExistingClientException, NullClientException {
		ss.removeSubscriber(mockClient1);
	}

	@Test
	public void DeleteClientFromService() throws ExistingClientException, NonExistingClientException, NullClientException {
		ss.addSubscriber(mockClient1);
		ss.removeSubscriber(mockClient1);
		assertEquals(ss.subscribers.size(), 0);

	}

	@Test(expected = NonExistingClientException.class)
	public void ExceptionOnDeletingSameClientTwice() throws ExistingClientException, NonExistingClientException, NullClientException {
		ss.addSubscriber(mockClient1);
		ss.removeSubscriber(mockClient1);
		ss.removeSubscriber(mockClient1);
	}

	// 9 and 10 together
	@Test
	public void SeveralClientDeletions() throws ExistingClientException, NonExistingClientException, NullClientException {
		ss.addSubscriber(mockClient1);
		ss.addSubscriber(mockClient2);
		ss.removeSubscriber(mockClient1);
		ss.removeSubscriber(mockClient2);
		assertEquals(ss.subscribers.size(), 0);
	}

	@Test
	public void ClientReceivesMessageIfHeHasEmail() throws ExistingClientException, NullClientException {
		when(mockClient1.hasEmail()).thenReturn(true);
		ss.addSubscriber(mockClient1);
		ss.sendMessage(msg);
		verify(mockClient1).receiveMessage(msg);
	}

	@Test
	public void ClientDoesNotReceiveMessageIfHeHasNotEmail() throws ExistingClientException, NullClientException {
		when(mockClient1.hasEmail()).thenReturn(false);
		ss.addSubscriber(mockClient1);
		ss.sendMessage(msg);
		verify(mockClient1, never()).receiveMessage(msg);
	}

	@Test
	public void SeveralClientsReceiveMessageIfTheyHaveEmail() throws ExistingClientException, NullClientException {
		when(mockClient1.hasEmail()).thenReturn(true);
		when(mockClient2.hasEmail()).thenReturn(true);
		ss.addSubscriber(mockClient1);
		ss.addSubscriber(mockClient2);
		ss.sendMessage(msg);
		verify(mockClient1).receiveMessage(msg);
		verify(mockClient2).receiveMessage(msg);
	}

	@Test
	public void ClientDoesNotReceivesMessageIfUnsubscribed() throws NonExistingClientException, ExistingClientException, NullClientException {
		when(mockClient1.hasEmail()).thenReturn(true);
		ss.addSubscriber(mockClient1);
		ss.removeSubscriber(mockClient1);
		ss.sendMessage(msg);
		verify(mockClient1, never()).receiveMessage(msg);
	}


}

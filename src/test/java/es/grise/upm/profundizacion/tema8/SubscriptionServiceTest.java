package es.grise.upm.profundizacion.tema8;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SubscriptionServiceTest {

	private SubscriptionService ss;
	private Client mockClient1 = mock(Client.class);
	private Client mockClient2 = mock(Client.class);

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
}

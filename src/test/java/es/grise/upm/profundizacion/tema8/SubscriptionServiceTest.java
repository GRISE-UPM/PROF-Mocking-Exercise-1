package es.grise.upm.profundizacion.tema8;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {


	private static final int ONE_TIME = 1;
	private SubscriptionService service;
	private Client client_1 = mock(Client.class);
	private Client client_2 = mock(Client.class);
	private Message msg = mock(Message.class);



	@Test(expected = NullClientException.class)
	public void shouldThrowExceptionWhenAddNullClient()
			throws ExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		this.service.addSubscriber(null);
	}

	@Test
	public void shouldAddClientOk()
			throws ExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		this.service.addSubscriber(this.client_1);
		assertEquals(this.service.subscribers.size(),1);
	}

	@Test(expected = ExistingClientException.class)
	public void shouldThrowExceptionWhenClientIsAlreadyAdded()
			throws ExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		this.service.addSubscriber(this.client_1);
		this.service.addSubscriber(this.client_1);
	}

	@Test
	public void shouldAddMultipleClients()
			throws ExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		this.service.addSubscriber(this.client_1);
		this.service.addSubscriber(this.client_2);
		assertEquals(this.service.subscribers.size(),2);
	}

	@Test(expected = NonExistingClientException.class)
	public void shouldThrowExceptionWhenRemoveNullList()
			throws NonExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		this.service.removeSubscriber(this.client_1);
	}

	@Test(expected = NonExistingClientException.class)
	public void shouldThrowExceptionWhenRemoveNotStoredClient()
			throws NonExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		addClientToList(this.client_1);
		this.service.removeSubscriber(this.client_2);
	}

	@Test
	public void shouldRemoveClientOK()
			throws NonExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		addClientToList(this.client_1);
		this.service.removeSubscriber(this.client_1);
		assertTrue(this.service.subscribers.isEmpty());
	}

	@Test(expected = NonExistingClientException.class)
	public void shouldThrowExceptionWhenRemoveTwiceSameClient()
			throws NonExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		addClientToList(this.client_1);
		this.service.removeSubscriber(this.client_1);
		this.service.removeSubscriber(this.client_1);
	}

	@Test
	public void shouldRemoveOkMultipleClients()
			throws NonExistingClientException, NullClientException {
		this.service = new SubscriptionService();
		addClientToList(this.client_1);
		addClientToList(this.client_2);
		this.service.removeSubscriber(this.client_1);
		this.service.removeSubscriber(this.client_2);
		assertTrue(this.service.subscribers.isEmpty());
	}

	@Test
	public void shouldReceiveMessageWhenIsSubscribed(){
		this.service = new SubscriptionService();
		doReturn(true).when(this.client_1).hasEmail();
		doNothing().when(this.client_1).receiveMessage(this.msg);
		addClientToList(this.client_1);
		this.service.sendMessage(this.msg);
		verify(this.client_1,times(ONE_TIME))
				.receiveMessage(this.msg);
	}

	@Test
	public void shouldNotReceiveMessageWhenIsNotSubscribed(){
		this.service = new SubscriptionService();
		doReturn(false).when(this.client_1).hasEmail();
		doNothing().when(this.client_1).receiveMessage(this.msg);
		addClientToList(this.client_1);
		this.service.sendMessage(this.msg);
		verify(this.client_1,times(0))
				.receiveMessage(this.msg);
	}

	@Test
	public void shouldReceiveMessagesMultipleClients(){
		this.service = new SubscriptionService();
		doReturn(true).when(this.client_1).hasEmail();
		doReturn(true).when(this.client_2).hasEmail();

		doNothing().when(this.client_1).receiveMessage(this.msg);
		doNothing().when(this.client_2).receiveMessage(this.msg);

		addClientToList(this.client_1);
		addClientToList(this.client_2);

		this.service.sendMessage(this.msg);
		verify(this.client_1,times(ONE_TIME))
				.receiveMessage(this.msg);
		verify(this.client_2,times(ONE_TIME))
				.receiveMessage(this.msg);
	}

	@Test
	public void shouldNotReceiveMessageAfterUnsubscribe(){
		this.service = new SubscriptionService();
		doReturn(true)
				.doReturn(false)
				.when(this.client_1).hasEmail();
		doNothing().when(this.client_1).receiveMessage(this.msg);
		addClientToList(this.client_1);

		this.service.sendMessage(this.msg);

		verify(this.client_1,times(ONE_TIME))
				.receiveMessage(this.msg);
	}

	private void addClientToList(Client client_2) {
		this.service.subscribers.add(client_2);
	}


}

package es.grise.upm.profundizacion.tema8;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
	
	SubscriptionService subscriptionService;
	Client NULL_CLIENT = null;
	Client MOCK_CLIENT1;
	Client MOCK_CLIENT2;
	Client MOCK_CLIENT3;
	Message MOCK_MESSAGE;
	
	//Comparison values
	static final int ZERO = 0;
	static final int ONE = 1;
	static final int THREE = 3;
	static final boolean HAS_EMAIL = true;
	static final boolean NO_EMAIL = false;
	
	@Before //Executes once before each test
	public void setup() {
		subscriptionService = new SubscriptionService();
		MOCK_CLIENT1 = mock(Client.class);
		MOCK_CLIENT2 = mock(Client.class);
		MOCK_CLIENT3 = mock(Client.class);
		MOCK_MESSAGE = mock(Message.class);
	}
	
	// ========================================= //
	// ============= STATE TESTS =============== //
	// ========================================= //
	
	/**
	 * You cannot add a NULL client to the Subscription Service and therefore
	 * a "NullClientException" will be raised
	 */
	@Test
	(expected = NullClientException.class)
	public void cantAddNullClient_AsSubscriber() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(NULL_CLIENT);
	}
	
	/**
	 * Client is added to the subscriber list. 
	 */
	@Test
	public void oneClientAddedCorrectly_ToSubscribersList() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(MOCK_CLIENT1);
		
		assertEquals(ONE, subscriptionService.subscribers.size());
		assertTrue(subscriptionService.subscribers.contains(MOCK_CLIENT1));
	}
	
	/**
	 * You cannot add the same client to the subscribers list and therefore
	 * a "ExistingClientException" will be raised 
	 */
	@Test
	(expected=ExistingClientException.class)
	public void cantAddSameClient_ToSubscribersList() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(MOCK_CLIENT1);
		subscriptionService.addSubscriber(MOCK_CLIENT1);
	}
	
	/**
	 * More than one client are added to the subscribers list. 
	 */
	@Test
	public void moreThanOneClientAddedCorrectly_ToSubscribersList() throws NullClientException, ExistingClientException {
		subscriptionService.addSubscriber(MOCK_CLIENT1);
		subscriptionService.addSubscriber(MOCK_CLIENT2);
		subscriptionService.addSubscriber(MOCK_CLIENT3);
		
		assertEquals(THREE, subscriptionService.subscribers.size());
		assertTrue(subscriptionService.subscribers.contains(MOCK_CLIENT1));
		assertTrue(subscriptionService.subscribers.contains(MOCK_CLIENT2));
		assertTrue(subscriptionService.subscribers.contains(MOCK_CLIENT3));
	}
	
	/**
	 * You cannot remove a null client from the subscribers list and thus a 
	 * NullClientExpcetion will be raised
	 */
	@Test
	(expected=NullClientException.class)
	public void cannotRemoveNullClient_FromSubscribersList() throws NullClientException, NonExistingClientException {
		subscriptionService.removeSubscriber(NULL_CLIENT);
	}
	
	/**
	 * You cannot remove a client that doesn't exist from the subscribers list and therefore a 
	 * NonExistingClientException will be raised
	 */
	@Test
	(expected=NonExistingClientException.class)
	public void cannotRemoveNonExistingClient_FromSubscribersList() throws NullClientException, NonExistingClientException {
		subscriptionService.removeSubscriber(MOCK_CLIENT1);
	}
	
	/**
	 * You can remove a client that exist in the subscribers list
	 */
	@Test
	public void canRemoveExistingClient_FromSubscribersList() throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(MOCK_CLIENT1);
		subscriptionService.removeSubscriber(MOCK_CLIENT1);
		assertEquals(ZERO,subscriptionService.subscribers.size());
	}
	
	/**
	 * You cannot remove the same client twice from the subscribers list and thus a 
	 * NonExistingClientException will be raised
	 */
	@Test
	(expected=NonExistingClientException.class)
	public void cannotRemoveSameClientTwice_FromSubscribersList() throws NullClientException, NonExistingClientException, ExistingClientException {
		subscriptionService.addSubscriber(MOCK_CLIENT1);
		subscriptionService.removeSubscriber(MOCK_CLIENT1);
		subscriptionService.removeSubscriber(MOCK_CLIENT1);
	}
	
	/**
	 * You can remove several existing clients from the subscribers list. 
	 */
	@Test
	public void removeSeveralExistingClients_FromSubscribersList() throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(MOCK_CLIENT1);
		subscriptionService.addSubscriber(MOCK_CLIENT2);
		subscriptionService.removeSubscriber(MOCK_CLIENT1);
		subscriptionService.removeSubscriber(MOCK_CLIENT2);
		
		assertEquals(ZERO,subscriptionService.subscribers.size());
	}
	
	/**
	 * You can remove all existing clients from the subscribers list. 
	 */
	@Test
	public void removeSeveralAndAllExistingClients_FromSubscribersList() throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService.addSubscriber(MOCK_CLIENT1);
		subscriptionService.addSubscriber(MOCK_CLIENT2);
		subscriptionService.addSubscriber(MOCK_CLIENT3);
		subscriptionService.removeSubscriber(MOCK_CLIENT1);
		subscriptionService.removeSubscriber(MOCK_CLIENT2);
		subscriptionService.removeSubscriber(MOCK_CLIENT3);
		
		assertEquals(ZERO,subscriptionService.subscribers.size());
	}
	
	// =============================================== //
	// ============= INTERACTION TESTS =============== //
	// =============================================== //
	
	/**
	 * A subscribed client will receive a message if he has email's 
	 */
	@Test
	public void subbedClientWillRecieveMessage_IfHeHasEmail() throws NullClientException, ExistingClientException {
		this.subscriptionService.addSubscriber(MOCK_CLIENT1);
		when(MOCK_CLIENT1.hasEmail()).thenReturn(HAS_EMAIL);
		
		subscriptionService.sendMessage(MOCK_MESSAGE);
		
		verify(MOCK_CLIENT1,times(ONE)).receiveMessage(MOCK_MESSAGE);
	}
	
	/**
	 * A subscribed client will NOT receive a message if he hasn't got any email's 
	 */
	@Test
	public void subbedClientWillNotRecieveMessage_IfHeHasNoEmail() throws NullClientException, ExistingClientException {
		this.subscriptionService.addSubscriber(MOCK_CLIENT1);
		when(MOCK_CLIENT1.hasEmail()).thenReturn(NO_EMAIL);
		subscriptionService.sendMessage(MOCK_MESSAGE);
		
		verify(MOCK_CLIENT1,times(ZERO)).receiveMessage(MOCK_MESSAGE);
	}
	
	/**
	 * Several subscribed clients will receive a message if they have email's 
	 */
	@Test
	public void severalSubbedClientWillRecieveMessages_IfTheyHaveEmail() throws NullClientException, ExistingClientException {
		this.subscriptionService.addSubscriber(MOCK_CLIENT1);
		this.subscriptionService.addSubscriber(MOCK_CLIENT2);
		this.subscriptionService.addSubscriber(MOCK_CLIENT3);
		when(MOCK_CLIENT1.hasEmail()).thenReturn(HAS_EMAIL);
		when(MOCK_CLIENT2.hasEmail()).thenReturn(HAS_EMAIL);
		when(MOCK_CLIENT3.hasEmail()).thenReturn(HAS_EMAIL);
		
		subscriptionService.sendMessage(MOCK_MESSAGE);
		
		verify(MOCK_CLIENT1,times(ONE)).receiveMessage(MOCK_MESSAGE);
		verify(MOCK_CLIENT2,times(ONE)).receiveMessage(MOCK_MESSAGE);
		verify(MOCK_CLIENT3,times(ONE)).receiveMessage(MOCK_MESSAGE);
	}
	
	/**
	 * A subscribed client will receive a message if he has email's 
	 */
	@Test
	public void unsubbedClientWillNotRecieveMessage_IfHeHasEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		this.subscriptionService.addSubscriber(MOCK_CLIENT1);
		when(MOCK_CLIENT1.hasEmail()).thenReturn(HAS_EMAIL);
		
		this.subscriptionService.removeSubscriber(MOCK_CLIENT1);
		subscriptionService.sendMessage(MOCK_MESSAGE);
		
		verify(MOCK_CLIENT1,times(ZERO)).receiveMessage(MOCK_MESSAGE);
	}
}

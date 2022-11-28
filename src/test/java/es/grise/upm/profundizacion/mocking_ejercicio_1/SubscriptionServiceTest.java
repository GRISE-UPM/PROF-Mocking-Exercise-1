package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

public class SubscriptionServiceTest {
	
	//*************
	//*** STATE ***
	//*************

	@Test
	public void onAddNullClient() {
		/* Throws NullClientException when adding a null Client to the subscribers list. */
		assertThrows(NullClientException.class, () -> new SubscriptionService().addSubscriber(null));
 	}
	
	@Test
	public void onAddClientOnce() throws NullClientException, ExistingClientException {
		/* Adds Client to subscribers list via addSubscriber() */
		SubscriptionService ss = new SubscriptionService();
		Client c = Mockito.mock(Client.class);
		ss.addSubscriber(c);
		assertEquals(true, ss.subscribers.contains(c)); // contains Client c
		assertEquals(1, ss.subscribers.size()); // and nothing else
 	}
	
	@Test
	public void onAddClientTwice() throws NullClientException, ExistingClientException {
		/* Throws ExistingClientException when adding a Client twice */
		SubscriptionService ss = new SubscriptionService();
		Client c = Mockito.mock(Client.class);
		ss.addSubscriber(c);
		assertThrows(ExistingClientException.class, () -> ss.addSubscriber(c)); // Client c for 2nd time
 	}
	
	@Test
	public void onAddSeveralClients() throws NullClientException, ExistingClientException {
		/* Adds Client(s) to subscribers list via addSubscriber() */
		SubscriptionService ss = new SubscriptionService();
		Client c1 = Mockito.mock(Client.class);
		Client c2 = Mockito.mock(Client.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		assertEquals(true, ss.subscribers.contains(c1)); // contains Client c1
		assertEquals(true, ss.subscribers.contains(c2)); // contains Client c2
		assertEquals(2, ss.subscribers.size()); // and nothing else
 	}
	
	@Test
	public void onRemoveNullClient() {
		/* Throws NullClientException when removing a null Client from the subscribers list. */
		assertThrows(NullClientException.class, () -> new SubscriptionService().removeSubscriber(null));
 	}
	
	@Test
	public void onRemoveNonExistentClient() throws NullClientException, ExistingClientException {
		/* Throws NonExistingClientException when removing a non existent Client from the subscribers list. */
		SubscriptionService ss = new SubscriptionService();
		Client c1 = Mockito.mock(Client.class);
		Client c2 = Mockito.mock(Client.class);
		ss.addSubscriber(c1);
		assertThrows(NonExistingClientException.class, () -> new SubscriptionService().removeSubscriber(c2));
 	}
	
	@Test
	public void onRemoveClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		/* Removes Client from the subscribers list. */
		SubscriptionService ss = new SubscriptionService();
		Client c1 = Mockito.mock(Client.class);
		ss.addSubscriber(c1);
		ss.removeSubscriber(c1);
		assertEquals(0, ss.subscribers.size()); // nothing
 	}
	
	@Test
	public void onRemoveClientTwice() throws NullClientException, ExistingClientException, NonExistingClientException {
		/* Throws NonExistingClientException when removing existing Client twice from the subscribers list. */
		SubscriptionService ss = new SubscriptionService();
		Client c1 = Mockito.mock(Client.class);
		ss.addSubscriber(c1);
		ss.removeSubscriber(c1);
		assertThrows(NonExistingClientException.class, () -> new SubscriptionService().removeSubscriber(c1));
 	}
	
	@Test
	public void onRemoveSeveralClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		/* Removes Client(s) from the subscribers list. */
		SubscriptionService ss = new SubscriptionService();
		Client c1 = Mockito.mock(Client.class);
		Client c2 = Mockito.mock(Client.class);
		Client c3 = Mockito.mock(Client.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		ss.removeSubscriber(c1);
		ss.removeSubscriber(c2);
		assertEquals(1, ss.subscribers.size()); // only 1 client remains
		assertEquals(true, ss.subscribers.contains(c3)); // in particular, Client c3
 	}
	
	@Test
	public void onRemoveAllClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		/* Removes all Client(s) from the subscribers list. */
		SubscriptionService ss = new SubscriptionService();
		Client c1 = Mockito.mock(Client.class);
		Client c2 = Mockito.mock(Client.class);
		Client c3 = Mockito.mock(Client.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		ss.removeSubscriber(c1);
		ss.removeSubscriber(c2);
		ss.removeSubscriber(c3);
		assertEquals(0, ss.subscribers.size()); // nothing
 	}
	
	//*******************
	//*** INTERACTION ***
	//*******************
	
	@Test
 	public void onHasEmailTrue() throws NullClientException, ExistingClientException {
 		/* Client receives email(s) if hasEmail() == true */
		SubscriptionService ss = new SubscriptionService();
		Client c = Mockito.mock(Client.class);
		Message m = Mockito.mock(Message.class);
		ss.addSubscriber(c);
		Mockito.when(c.hasEmail()).thenReturn(true);
		if(c.hasEmail()) {
			ss.sendMessage(m);
			ss.sendMessage(m);
	 	}
	 	Mockito.verify(c, Mockito.times(2)).receiveMessage(m);  // Client c receives 2 messages
 	}
	
	@Test
 	public void onHasEmailFalse() throws NullClientException, ExistingClientException {
 		/* Client doesn't receive email(s) if hasEmail() == false */
		SubscriptionService ss = new SubscriptionService();
		Client c = Mockito.mock(Client.class);
		Message m = Mockito.mock(Message.class);
		ss.addSubscriber(c);
		Mockito.when(c.hasEmail()).thenReturn(false);
		if(c.hasEmail()) {
			ss.sendMessage(m);
			ss.sendMessage(m);
	 	}
	 	Mockito.verify(c, Mockito.times(0)).receiveMessage(m); // Client c doesn't receive messages
 	}
	
	@Test
 	public void onSeveralClientsHaveEmailTrue() throws NullClientException, ExistingClientException {
 		/* Client(s) receive email(s) if hasEmail() == true */
		SubscriptionService ss = new SubscriptionService();
		Client c1 = Mockito.mock(Client.class);
		Client c2 = Mockito.mock(Client.class);
		Client c3 = Mockito.mock(Client.class);
		Message m = Mockito.mock(Message.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		Mockito.when(c1.hasEmail()).thenReturn(true);
		Mockito.when(c2.hasEmail()).thenReturn(true);
		Mockito.when(c3.hasEmail()).thenReturn(false);
		if(c1.hasEmail() || c2.hasEmail() || c3.hasEmail()) {
			ss.sendMessage(m);
			ss.sendMessage(m);
	 	}
	 	Mockito.verify(c1, Mockito.times(2)).receiveMessage(m); // Client c1 receives messages
	 	Mockito.verify(c2, Mockito.times(2)).receiveMessage(m); // Client c2 receives messages
	 	Mockito.verify(c3, Mockito.times(0)).receiveMessage(m);
 	}
	
	@Test
 	public void onUnsubscribedClient() throws NullClientException, ExistingClientException, NonExistingClientException {
 		/* Client doesn't receive email(s) after being removed as a subscriber */
		SubscriptionService ss = new SubscriptionService();
		Client c1 = Mockito.mock(Client.class);
		Client c2 = Mockito.mock(Client.class);
		Client c3 = Mockito.mock(Client.class);
		Message m = Mockito.mock(Message.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		Mockito.when(c1.hasEmail()).thenReturn(true);
		Mockito.when(c2.hasEmail()).thenReturn(true);
		Mockito.when(c3.hasEmail()).thenReturn(true);
		if(c1.hasEmail() || c2.hasEmail() || c3.hasEmail()) {
			ss.sendMessage(m);
			ss.sendMessage(m);
	 	}
	 	Mockito.verify(c3, Mockito.times(2)).receiveMessage(m); // Client c3 subscribed (receives 2 messages)
		ss.removeSubscriber(c3);
		if(c1.hasEmail() || c2.hasEmail() || c3.hasEmail()) {
			ss.sendMessage(m);
			ss.sendMessage(m);
	 	}
	 	Mockito.verify(c3, Mockito.times(2)).receiveMessage(m); // Client c3: not subscribed (stays at 2 messages)
 	}
	
}

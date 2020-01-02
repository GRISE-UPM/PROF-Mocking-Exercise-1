package es.grise.upm.profundizacion.tema8;


import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceTest {
	
	
	@Mock
	private Client client;
	
	@Mock
	private Iterator<Client> clientIterator;
	
	@Mock
	private Message mensaje;
	
	@Mock 
	Collection <Client> subscribersMock;
	
	@InjectMocks
	private SubscriptionService subcription;
	
	@Test(expected = NullClientException.class)
	public void testClientNull() throws NullClientException, ExistingClientException {
		subcription.addSubscriber(null);
		verify(subcription,atLeastOnce()).addSubscriber(null);
	}
	@Test()
	public void testAddSubscriber() throws NullClientException, ExistingClientException {
		subscribersMock.add(client);
		subcription.addSubscriber(client);
		assertEquals(subscribersMock, subcription.subscribers);
		verify(subscribersMock, atLeastOnce()).add(client);
	}
	@Test(expected = ExistingClientException.class)
	public void testAddTwoEqualSubscriber() throws NullClientException, ExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		when(subcription.subscribers.contains(client)).thenReturn(true);
		sub.addSubscriber(client);
		Client cliente2=client;
		sub.addSubscriber(cliente2);
		verify(sub, atLeast(2)).addSubscriber(client);
	}
	
	@Test
	public void testAddSubscriberWork() throws NullClientException, ExistingClientException {
		subscribersMock.add(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		assertEquals(subscribersMock, subcription.subscribers);
		verify(subscribersMock, atLeast(4)).add(client);
	}
	
	@Test(expected = NullClientException.class)
	public void testClientRemoveNull() throws NullClientException, NonExistingClientException {
		subcription.removeSubscriber(null);
		verify(subcription,atLeastOnce()).removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void testClientNotRemove() throws NullClientException, NonExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		when(subcription.subscribers.contains(client)).thenReturn(false);
		sub.removeSubscriber(client);
		verify(subcription,atLeastOnce()).removeSubscriber(client);
	}
	
	@Test
	public void testClientRemoveWork() throws NullClientException, NonExistingClientException, ExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		sub.addSubscriber(client);
		subscribersMock.add(client);
		sub.removeSubscriber(client);
		subscribersMock.remove(client);
		verify(subscribersMock,atLeastOnce()).add(client);
		verify(subscribersMock,atLeastOnce()).remove(client);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void testTwoClientNotRemove() throws NullClientException, NonExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		when(subcription.subscribers.contains(client)).thenReturn(false);
		sub.removeSubscriber(client);
		verify(subcription,atLeast(2)).removeSubscriber(client);
	}
	
	
	@Test
	public void testRemoveSubscriberWork() throws NullClientException, ExistingClientException, NonExistingClientException {
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		when(subcription.subscribers.contains(client)).thenReturn(true);
		subcription.removeSubscriber(client);
		subcription.removeSubscriber(client);
		subcription.removeSubscriber(client);
		subcription.removeSubscriber(client);
		subscribersMock.remove(client);
		subscribersMock.remove(client);
		subscribersMock.remove(client);
		subscribersMock.remove(client);
		assertEquals(subscribersMock, subcription.subscribers);
		verify(subscribersMock, atLeast(6)).add(client);
		verify(subscribersMock,atLeast(4)).remove(client);
	}
	
	@Test
	public void testRemoveAllSubscriberWork() throws NullClientException, ExistingClientException, NonExistingClientException {
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		subscribersMock.add(client);
		when(subcription.subscribers.contains(client)).thenReturn(true);
		subcription.removeSubscriber(client);
		subcription.removeSubscriber(client);
		subcription.removeSubscriber(client);
		subcription.removeSubscriber(client);
		subscribersMock.remove(client);
		subscribersMock.remove(client);
		subscribersMock.remove(client);
		subscribersMock.remove(client);
		assertEquals(subscribersMock, subcription.subscribers);
		verify(subscribersMock, atLeast(6)).add(client);
		verify(subscribersMock,atLeast(4)).remove(client);
	}
	@Test
	public void testReciveEmailWork() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		subcription.addSubscriber(client);
		when(client.hasEmail()).thenReturn(true);
		sub.sendMessage(mensaje);
		client.receiveMessage(mensaje);
		subscribersMock.add(client);
		verify(subscribersMock, atLeastOnce()).add(client);
		verify(client,atMost(1)).receiveMessage(mensaje);
	}
	
	@Test
	public void testNotReciveEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		Collection<Client> clients=new ArrayList<Client>();
		clients.add(client);
		sub.addSubscriber(client);
		when(client.hasEmail()).thenReturn(false);
		sub.subscribers.add(client);
		for(Client client: clients) {
			sub.subscribers.add(client);
			sub.sendMessage(mensaje);
		}
		verify(client,atMost(0)).receiveMessage(mensaje);
	}
	
	@Test
	public void testReciveEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		Collection<Client> clients=new ArrayList<Client>();
		clients.add(client);
		when(client.hasEmail()).thenReturn(true);
		sub.addSubscriber(client);
		for(Client client: clients) {
			sub.sendMessage(mensaje);
			sub.removeSubscriber(client);
			when(client.hasEmail()).thenReturn(false);
			sub.sendMessage(mensaje);
			clients.remove(client);
			if(clients.isEmpty())
				break;
		}
		verify(client,atMost(1)).receiveMessage(mensaje);
	}
}

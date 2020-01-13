package es.grise.upm.profundizacion.tema8;


import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
	
	
	//No se puede añadir un Client null a la lista subscribers.
	@Test(expected = NullClientException.class)
	public void testClientNull() throws NullClientException, ExistingClientException {
		subcription.addSubscriber(null);
		verify(subcription,atLeastOnce()).addSubscriber(null);
	}
	
	//Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	@Test()
	public void testAddSubscriber() throws NullClientException, ExistingClientException {
		subscribersMock.add(client);
		subcription.addSubscriber(client);
		assertEquals(subscribersMock, subcription.subscribers);
		verify(subscribersMock, atLeastOnce()).add(client);
	}
	
	
	//No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.
	@Test(expected = ExistingClientException.class)
	public void testAddTwoEqualSubscriber() throws NullClientException, ExistingClientException {
		subcription.addSubscriber(client);
		when(subcription.subscribers.contains(client)).thenReturn(true);
		subcription.addSubscriber(client);
		verify(subcription, atLeast(2)).addSubscriber(client);
	}
	
	
	//Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	@Test
	public void testAddSubscribersWork() throws NullClientException, ExistingClientException {
		when(subcription.subscribers.contains(client)).thenReturn(false);
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
	
	
	//No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException
	@Test(expected = NullClientException.class)
	public void testClientRemoveNull() throws NullClientException, NonExistingClientException {
		subcription.removeSubscriber(null);
		verify(subcription,atLeastOnce()).removeSubscriber(null);
	}
	
	
	//No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
	public void testClientNotRemove() throws NullClientException, NonExistingClientException {
		when(subcription.subscribers.contains(client)).thenReturn(false);
		subcription.removeSubscriber(client);
		verify(subcription,atLeastOnce()).removeSubscriber(client);
	}
	
	//Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	@Test
	public void testClientRemoveWork() throws NullClientException, NonExistingClientException, ExistingClientException {
		subcription.addSubscriber(client);
		subscribersMock.add(client);
		when(subcription.subscribers.contains(client)).thenReturn(true);
		subcription.removeSubscriber(client);
		subscribersMock.remove(client);
		assertEquals(subscribersMock, subcription.subscribers);
		verify(subscribersMock,atLeastOnce()).add(client);
		verify(subscribersMock,atLeastOnce()).remove(client);
	}
	
	//No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException
	@Test(expected = NonExistingClientException.class)
	public void testTwoClientEqualNotRemove() throws NullClientException, NonExistingClientException, ExistingClientException {
		subcription.addSubscriber(client);
		when(subcription.subscribers.contains(client)).thenReturn(false);
		subcription.addSubscriber(client);
		subcription.removeSubscriber(client);
		when(subcription.subscribers.contains(client)).thenReturn(false);
		subcription.removeSubscriber(client);
		verify(subcription,atMost(2)).removeSubscriber(client);
	}
	
	//Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
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
	
	//Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
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
	
	//Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
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
	
	//Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
	@Test
	public void testNotReciveEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		subcription.addSubscriber(client);
		when(client.hasEmail()).thenReturn(false);
		sub.sendMessage(mensaje);
		verify(client,atMost(0)).receiveMessage(mensaje);
	}
	
	//Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
	@Test
	public void testReciveEmailsClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService sub=new SubscriptionService();
		when(subcription.subscribers.contains(client)).thenReturn(false);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		subcription.addSubscriber(client);
		when(client.hasEmail()).thenReturn(true);
		sub.sendMessage(mensaje);
		sub.sendMessage(mensaje);
		sub.sendMessage(mensaje);
		sub.sendMessage(mensaje);
		client.receiveMessage(mensaje);
		client.receiveMessage(mensaje);
		client.receiveMessage(mensaje);
		client.receiveMessage(mensaje);
		verify(client,atMost(4)).receiveMessage(mensaje);
	}
	
	//Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
	@Test
	public void testNotRemoveClientReciveEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService sub=new SubscriptionService();	
		sub.addSubscriber(client);
		sub.removeSubscriber(client);
		when(client.hasEmail()).thenReturn(false);
		sub.sendMessage(mensaje);
		verify(client,atMost(0)).receiveMessage(mensaje);
	}
}

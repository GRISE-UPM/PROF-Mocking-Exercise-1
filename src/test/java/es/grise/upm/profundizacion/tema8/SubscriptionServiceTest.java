package es.grise.upm.profundizacion.tema8;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.Before;


//Se pide: implementar las siguientes pruebas sobre la clase SubscriptionService, utilizando mocks:
//
//State tests
//No se puede añadir un Client null a la lista subscribers.
//Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
//No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.
//Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
//No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
//No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
//Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
//No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
//Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
//Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
//Interaction tests
//Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
//Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
//Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
//Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).

public class SubscriptionServiceTest {

	private SubscriptionService sub;
	private Client client, client2;
	private Message msg;

	@Before
	public void beforeConfig(){
		sub = new SubscriptionService();
		client = mock(Client.class);
		client2 = mock(Client.class);
		msg = mock(Message.class);
	}

	@Test (expected = NullClientException.class)
	public void NonClientNull() throws NullClientException, ExistingClientException {
		sub.addSubscriber(null);
	}

	@Test
	public void addClient() throws NullClientException, ExistingClientException {
		sub.addSubscriber(client);
		assertEquals(sub.subscribers.size(),1);
	}

	@Test (expected = ExistingClientException.class)
	public void notRepeatSub() throws NullClientException, ExistingClientException {
		sub.addSubscriber(client);
		sub.addSubscriber(client);
	}

	@Test
	public void addManyClients() throws NullClientException, ExistingClientException {
		sub.addSubscriber(client);
		sub.addSubscriber(client2);
		assertEquals(sub.subscribers.size(),2);
	}

	@Test (expected = NullClientException.class)
	public void notRemoveNullClient() throws NullClientException, NonExistingClientException {
		sub.removeSubscriber(null);
	}

	@Test (expected = NonExistingClientException.class)
	public void notRemoveClientNotSaveInTheList() throws NullClientException, NonExistingClientException {
		sub.removeSubscriber(client);
	}

	@Test
	public void RemoveExistingClientinTheList() throws NullClientException, ExistingClientException, NonExistingClientException {
		sub.addSubscriber(client);
		sub.removeSubscriber(client);
		assertEquals(sub.subscribers.size(),0);
	}

	@Test (expected = NonExistingClientException.class)
	public void notDeleteTwoTimesAClient() throws NullClientException, NonExistingClientException, ExistingClientException {
		sub.addSubscriber(client);
		sub.removeSubscriber(client);
		sub.removeSubscriber(client);
	}

	//This test is for 2 conditions (remove multiple and all clients)
	@Test
	public void RemoveMultipleClients() throws NullClientException, ExistingClientException, NonExistingClientException {
		sub.addSubscriber(client);
		sub.addSubscriber(client2);
		sub.removeSubscriber(client);
		sub.removeSubscriber(client2);
		assertEquals(sub.subscribers.size(),0);
	}
	//Interactions test

	@Test 
	public void RecieveMessageWithEmail() throws NullClientException, ExistingClientException {		
		when(client.hasEmail()).thenReturn(true);
		sub.addSubscriber(client);
		sub.sendMessage(msg);	
		verify(client).receiveMessage(msg);		
	}

	@Test 
	public void NotRecieveMessageWithNoEmail() throws NullClientException, ExistingClientException {		
		when(client.hasEmail()).thenReturn(false);
		sub.addSubscriber(client);
		sub.sendMessage(msg);	
		verify(client,never()).receiveMessage(msg);		
	}

	@Test
	public void AllClientsRecieveMsg() throws NullClientException, ExistingClientException {
		when(client.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		sub.addSubscriber(client);
		sub.addSubscriber(client2);
		sub.sendMessage(msg);
		verify(client).receiveMessage(msg);
		verify(client2).receiveMessage(msg);
	}
	@Test 
	public void NotRecieveMessageIfUnsuscribed() throws NullClientException, ExistingClientException, NonExistingClientException {		
		when(client.hasEmail()).thenReturn(true);
		sub.addSubscriber(client);
		sub.removeSubscriber(client);
		sub.sendMessage(msg);	
		verify(client,never()).receiveMessage(msg);		
	}
}

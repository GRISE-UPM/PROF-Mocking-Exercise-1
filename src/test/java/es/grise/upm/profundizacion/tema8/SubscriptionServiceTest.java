package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SubscriptionServiceTest {

// Implementar las siguientes pruebas sobre la clase SubscriptionService, utilizando mocks:
	
// We can either create global mocks for each object or once per class, the latter was chosen
	
// Part 1: State tests

// 1-  No se puede añadir un Client null a la lista subscribers.

	@Test(expected = NullClientException.class)
	public void cannot_add_null_client_subscriptionservice() throws NullClientException, ExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client = null;
		c.addSubscriber(client);
	}

// 2- Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	
	@Test()
	public void add_client_store_subscriberslist() throws NullClientException, ExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client testC = mock(Client.class);
		
		c.addSubscriber(testC);
		// Check that there are clients on the list
		assertEquals(c.subscribers.size(),1);
	}
	
// 3- No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.

	@Test(expected = ExistingClientException.class)
	public void addSubscriber_duplicated_Client() throws NullClientException, ExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client testC = mock(Client.class);
		c.addSubscriber(testC);
		c.addSubscriber(testC);;	
	}


// 4- Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	
	@Test()
	public void check_multiple_clients_subscriberlist() throws NullClientException, ExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		Client client4 = mock(Client.class);
		Client client5 = mock(Client.class);
		
		c.addSubscriber(client1);
		c.addSubscriber(client2);
		c.addSubscriber(client3);
		c.addSubscriber(client4);
		c.addSubscriber(client5);
		
		assertEquals(true, c.subscribers.contains(client1));
		assertEquals(true, c.subscribers.contains(client2));
		assertEquals(true, c.subscribers.contains(client3));
		assertEquals(true, c.subscribers.contains(client4));
		assertEquals(true, c.subscribers.contains(client5));
		assertEquals(c.subscribers.size(),5);
	}
	

// 5 - No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	
	@Test(expected = NullClientException.class)
	public void cannot_remove_null_client() throws NullClientException, NonExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		c.removeSubscriber(null);
	}
	
// 6 - No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	
	@Test(expected = NonExistingClientException.class)
	public void cannot_remove_nonexistent_client() throws NullClientException, NonExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		// Mock a client and try to remove it from a list in which it is not included
		c.removeSubscriber(client1);
	}
	
// 7 - Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	
	@Test()
	public void can_remove_client_stored_subscriberlist() throws NullClientException, NonExistingClientException, ExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		c.addSubscriber(client1);
		// We should be able to remove an existing client on the subscriberlist
		c.removeSubscriber(client1);
	}
	
// 8 - No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	
	@Test(expected = NonExistingClientException.class)
	public void cannot_remove_client_twice() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		c.addSubscriber(client1);
		// We should be able to only remove the client once
		c.removeSubscriber(client1);
		c.removeSubscriber(client1);
	}
	
// 9 - Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
	
	
	@Test()
	public void can_remove_various_client_subscriptionlist() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		Client client4 = mock(Client.class);
		Client client5 = mock(Client.class);
		
		c.addSubscriber(client1);
		c.addSubscriber(client2);
		c.addSubscriber(client3);
		c.addSubscriber(client4);
		c.addSubscriber(client5);
		// We should be able to remove some of the clients that are on the list
		c.removeSubscriber(client1);
		c.removeSubscriber(client5);
		// Check that there are indeed 3 clients still on the list
		assertEquals(c.subscribers.size(),3);
	}
	
	
// 10 - Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.


	@Test()
	public void can_remove_all_clients_subscriberlist() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		Client client4 = mock(Client.class);
		Client client5 = mock(Client.class);
		
		c.addSubscriber(client1);
		c.addSubscriber(client2);
		c.addSubscriber(client3);
		c.addSubscriber(client4);
		c.addSubscriber(client5);
		// We should be able to remove all of the clients that are on the list
		c.removeSubscriber(client1);
		c.removeSubscriber(client2);
		c.removeSubscriber(client3);
		c.removeSubscriber(client4);
		c.removeSubscriber(client5);
		assertEquals(true, c.subscribers.isEmpty());
		
	}
	
// Part 2: Interaction tests
	
// 1 - Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
	
	@Test()
	public void client_receive_message_if_mail() throws NullClientException, ExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		Message message1 = mock(Message.class);
		when(client1.hasEmail()).thenReturn(true);

		c.addSubscriber(client1);
		c.sendMessage(message1);
		verify(client1).receiveMessage(message1);
		
	}
	
// 2 - Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
	
	@Test()
	public void client_doesnt_receive_message_if_nomail() throws NullClientException, ExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		Message message1 = mock(Message.class);
		when(client1.hasEmail()).thenReturn(false);

		c.addSubscriber(client1);
		c.sendMessage(message1);
		verify(client1,times(0)).receiveMessage(message1);
	}
	
	
// 3- Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
	
	
	@Test()
	public void multiple_clients_receive_messages_if_hasmail() throws NullClientException, ExistingClientException {
		
		SubscriptionService c = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		Client client4 = mock(Client.class);
		
		Message message1 = mock(Message.class);
		Message message2 = mock(Message.class);
		Message message3 = mock(Message.class);
		Message message4 = mock(Message.class);

		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		when(client3.hasEmail()).thenReturn(false);
		when(client4.hasEmail()).thenReturn(false);
		
		c.addSubscriber(client1);
		c.addSubscriber(client2);
		c.addSubscriber(client3);
		c.addSubscriber(client4);
		
		c.sendMessage(message1);
		c.sendMessage(message2);
		c.sendMessage(message3);
		c.sendMessage(message4);
		
		// The clients that have email should have hasEmail true while the ones that do not should have a false hasEmail
		
		verify(client1,times(1)).receiveMessage(message1);
		verify(client2,times(1)).receiveMessage(message2);
		verify(client3,times(0)).receiveMessage(message3);
		verify(client4,times(0)).receiveMessage(message4);

	}
	
	
	
// 4- Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
	
	

}

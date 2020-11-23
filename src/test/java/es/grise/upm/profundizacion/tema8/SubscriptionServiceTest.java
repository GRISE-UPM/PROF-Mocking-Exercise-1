package es.grise.upm.profundizacion.tema8;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

public class SubscriptionServiceTest {

	SubscriptionService service = new SubscriptionService();

	// State tests
	
	// No se puede añadir un Client null a la lista subscribers.
	@Test(expected = NullClientException.class)
	public void addNullClient() throws NullClientException, ExistingClientException {
		Client nullClient = null;
		service.addSubscriber(nullClient);
	}


	//	Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers
	@Test
	public void addClient() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);

		Collection <Client> clientList = new ArrayList<Client>();
		clientList.add(client);

		service.addSubscriber(client);
		assertEquals(clientList, service.subscribers);
	}

	// No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.
	@Test(expected = ExistingClientException.class)
	public void addSameClient() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		service.addSubscriber(client);
		service.addSubscriber(client);
	}

	// Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	@Test
	public void addDifferentClients() throws NullClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		
		Collection <Client> clientList = new ArrayList<Client>();
		clientList.add(client1);
		clientList.add(client2);
		
		
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		assertEquals(clientList, service.subscribers);
	}

	// No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	@Test(expected = NullClientException.class)
	public void removeNullClient() throws NullClientException, NonExistingClientException {
		Client nullClient = null;
		service.removeSubscriber(nullClient);
	}

	// No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
	public void removeNonExistingClient() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client client = mock(Client.class);
		service.removeSubscriber(client);

	}	

	// Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	@Test
	public void removeClient() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client client = mock(Client.class);
		
		Collection <Client> clientList = new ArrayList<Client>();
		clientList.add(client);
		
		service.addSubscriber(client);
		
		clientList.remove(client);
		service.removeSubscriber(client);
		
		assertEquals(clientList, service.subscribers);
	}	

	// No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
	public void removeTwiceSameClient() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		
		service.addSubscriber(client1);
		
		service.removeSubscriber(client1);
		service.removeSubscriber(client1);
	}

	// Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.	
	@Test
	public void removeMultipleClients() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		
		Collection <Client> clientList = new ArrayList<Client>();
		clientList.add(client1);
		clientList.add(client2);
		clientList.add(client3);
		
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.addSubscriber(client3);
		
		clientList.remove(client2);
		service.removeSubscriber(client2);
		
		clientList.remove(client3);
		service.removeSubscriber(client3);
		
		assertEquals(clientList, service.subscribers);
	}

	// Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
	@Test
	public void removeAllClients() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);

		Collection <Client> clientList = new ArrayList<Client>();
		clientList.add(client1);
		clientList.add(client2);
		clientList.add(client3);

		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.addSubscriber(client3);

		clientList.remove(client1);
		service.removeSubscriber(client1);
		
		clientList.remove(client2);
		service.removeSubscriber(client2);

		clientList.remove(client3);
		service.removeSubscriber(client3);

		assertEquals(clientList, service.subscribers);
	}

	// Interaction tests

	// Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
	@Test
	public void subscribedClientHasMail() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		
		// Mockito en el método hasEmail() del cliente, para simular el comportamiento y devolver true
		when(client.hasEmail()).thenReturn(true);
		
		service.addSubscriber(client);
		service.sendMessage(message);
		
		// Comprobar que se ha realizado receiveMessage() durante el sendMessage()
		verify(client, times(1)).receiveMessage(message);		
	}

	// Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
	@Test
	public void subscribedClientWithoutMail() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		
		// Mockito en el método hasEmail() del cliente, para simular el comportamiento y devolver false
		when(client.hasEmail()).thenReturn(false);
		
		service.addSubscriber(client);
		service.sendMessage(message);

		// Se comprueba que se ha realizado receiveMessage() en ambos clientes
		verify(client, times(0)).receiveMessage(message);		
	}	

	// Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
	@Test
	public void multipleSubscribedClientsHaveMail() throws NullClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Message message = mock(Message.class);
		
		// Mockito en el método hasEmail() de los clientes, para simular el comportamiento y devolver true
		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.sendMessage(message);
		
		// Se comprueba que se ha realizado receiveMessage() en ambos clientes
		verify(client1, times(1)).receiveMessage(message);
		verify(client2, times(1)).receiveMessage(message);
	}	

	// Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
	@Test
	public void nonSubscribedClientHasMail() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		
		// Mockito en el método hasEmail() del cliente, para simular el comportamiento y devolver true
		when(client.hasEmail()).thenReturn(true);
		
		// Se manda directamente el mensaje, no es necesario suscribir y des-suscribir el cliente, esto ya ha sido probado
		service.sendMessage(message);
		
		// Se comprueba que se ha realizado receiveMessage() en ambos clientes
		verify(client, times(0)).receiveMessage(message);
	}	

}

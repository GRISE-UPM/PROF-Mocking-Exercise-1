package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SubscriptionServiceTest {
	
	SubscriptionService service = new SubscriptionService();
	
	// State tests
	
	// No se puede añadir un Client null a la lista subscribers.
	@Test(expected = NullClientException.class)
	public void nullClientInsert() throws NullClientException, ExistingClientException {
		Client nullClient = null;
		service.addSubscriber(nullClient);
	}
	
	// Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	@Test
	public void clientInsert() throws NullClientException, ExistingClientException {
		Collection <Client> clientList = new ArrayList<Client>();
		Client client = mock(Client.class);
		// Lista con el mock
		clientList.add(client);
		// Servicio
		service.addSubscriber(client);
		assertEquals(clientList, service.subscribers);
	}
	
	// No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.
	@Test(expected = ExistingClientException.class)
	public void sameClientInsert() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		service.addSubscriber(client);
		service.addSubscriber(client);
	}
	
	// Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	@Test
	public void differentclientsInsert() throws NullClientException, ExistingClientException {
		Collection <Client> clientList = new ArrayList<Client>();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		// Lista con el mock
		clientList.add(client1);
		clientList.add(client2);
		// Servicio
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		assertEquals(clientList, service.subscribers);
	}
	
	// No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	@Test(expected = NullClientException.class)
	public void nullClientRemove() throws NullClientException, NonExistingClientException {
		Client nullClient = null;
		service.removeSubscriber(nullClient);
	}
	
	// No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
	public void notFoundClientRemove() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		service.addSubscriber(client1);
		service.removeSubscriber(client2);

	}	
	
	// Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	@Test
	public void clientRemove() throws NullClientException, NonExistingClientException, ExistingClientException {
		Collection <Client> clientList = new ArrayList<Client>();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		// Lista con el mock
		clientList.add(client1);
		clientList.add(client2);
		// Servicio
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		// Quitar el mismo
		clientList.remove(client1);
		service.removeSubscriber(client1);
		assertEquals(clientList, service.subscribers);
	}	
	
	// No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
	public void sameClientRemoveTwice() throws NullClientException, NonExistingClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		service.addSubscriber(client1);
		service.removeSubscriber(client1);
		service.removeSubscriber(client1);
	}
	
	// Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.	
	@Test
	public void multipleClientsRemove() throws NullClientException, NonExistingClientException, ExistingClientException {
		Collection <Client> clientList = new ArrayList<Client>();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		// Lista con el mock
		clientList.add(client1);
		clientList.add(client2);
		clientList.add(client3);
		// Servicio
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.addSubscriber(client3);
		// Quitar los mismos
		clientList.remove(client2);
		service.removeSubscriber(client2);
		clientList.remove(client3);
		service.removeSubscriber(client3);
		assertEquals(clientList, service.subscribers);
	}
	
	// Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
	@Test
	public void allClientsRemove() throws NullClientException, NonExistingClientException, ExistingClientException {
		Collection <Client> clientList = new ArrayList<Client>();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		// Lista con el mock
		clientList.add(client1);
		clientList.add(client2);
		clientList.add(client3);
		// Servicio
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.addSubscriber(client3);
		// Quitar los mismos
		clientList.remove(client2);
		service.removeSubscriber(client2);
		clientList.remove(client3);
		service.removeSubscriber(client3);
		clientList.remove(client1);
		service.removeSubscriber(client1);
		assertEquals(clientList, service.subscribers);
	}
	
	// Interaction tests
	
	// Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
	@Test
	public void subscribedClientHasMail() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		// Mandar un mail a un cliente suscrito deberia devolver true en el hasMail, definirlo en mockito
		when(client.hasEmail()).thenReturn(true);
		// Sub y mandar
		service.addSubscriber(client);
		service.sendMessage(message);
		// Comprobar que se ha realizado un receive message durante el send message
		verify(client, times(1)).receiveMessage(message);		
	}
	
	// Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
	@Test
	public void subscribedClientNotHasMail() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		// Mandar un mail a un cliente suscrito deberia devolver true en el hasMail, definirlo en mockito
		when(client.hasEmail()).thenReturn(false);
		// Sub y mandar
		service.addSubscriber(client);
		service.sendMessage(message);
		// Igual que antes pero ahora el cliente se ignora al no tener mail
		verify(client, times(0)).receiveMessage(message);		
	}	
	
	// Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
	@Test
	public void multipleSubscribedClientsHaveMail() throws NullClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Message message = mock(Message.class);
		// Mandar un mail a un cliente suscrito deberia devolver true en el hasMail, definirlo en mockito
		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		// Sub y mandar
		service.addSubscriber(client1);
		service.addSubscriber(client2);
		service.sendMessage(message);
		// Ahora comprobar para ambos clientes
		verify(client1, times(1)).receiveMessage(message);
		verify(client2, times(1)).receiveMessage(message);
	}	
	
	// Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
	@Test
	public void unsubbedClientHasMail() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		// Mandar un mail a un cliente suscrito deberia devolver true en el hasMail, definirlo en mockito
		when(client.hasEmail()).thenReturn(true);
		// Mandar directamente. Mantener el test simple, la desuscripcion ya se ha probado, no hay necesidad de suscribir+mandar+desuscribir+mandar.
		service.sendMessage(message);
		// Comprobar que no lo recibe
		verify(client, times(0)).receiveMessage(message);
	}	
}

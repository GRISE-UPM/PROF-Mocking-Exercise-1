package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubscriptionServiceTest {

	// 1. No se puede añadir un Client null a la lista subscribers.
	@Test
	public void test1_addnull() {
		SubscriptionService subService = new SubscriptionService();
		assertThrows(NullClientException.class, () -> subService.addSubscriber(null));

	}

	// 2. Al añadir un Clientmediante addSubscriber(), éste Client se almacena en la lista subscribers.
	@Test
	public void test2_addClient() throws ExistingClientException, NullClientException {
		SubscriptionService subService = new SubscriptionService();
		Client client = mock(Client.class);
		subService.addSubscriber(client);
		assertTrue(subService.subscribers.contains(client));
	}

	// 3. No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException
	@Test
	public void test3_addClientTwice() throws ExistingClientException, NullClientException{
		SubscriptionService subService = new SubscriptionService();
		Client client = mock(Client.class);
		subService.addSubscriber(client);
		assertThrows(ExistingClientException.class, () -> subService.addSubscriber(client));
	}


	//4. Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.
	@Test
	public void test4_addClients() throws ExistingClientException, NullClientException{
		SubscriptionService subService = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		subService.addSubscriber(client1);
		subService.addSubscriber(client2);
		assertTrue(subService.subscribers.contains(client1));
		assertTrue(subService.subscribers.contains(client2));
	}

	//5. No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	@Test
	public void test5_removeNull() {
		SubscriptionService subService = new SubscriptionService();
		assertThrows(NullClientException.class, () -> subService.removeSubscriber(null));
	}

	//6. No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException
	@Test
	public void test6_removeNoClient() throws ExistingClientException, NullClientException{
		SubscriptionService subService = new SubscriptionService();
		Client client = mock(Client.class);
		assertThrows(NonExistingClientException.class, () -> subService.removeSubscriber(client));
	}


	//7. Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
	@Test
	public void test7_removeClient() throws ExistingClientException, NullClientException, NonExistingClientException {
		SubscriptionService subService = new SubscriptionService();
		Client client = mock(Client.class);
		subService.addSubscriber(client);
		subService.removeSubscriber(client);
		assertFalse(subService.subscribers.contains(client));
	}

	//8. No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test
	public void test8_removeClientTwice() throws ExistingClientException, NullClientException, NonExistingClientException {
		SubscriptionService subService = new SubscriptionService();
		Client client = mock(Client.class);
		subService.addSubscriber(client);
		subService.removeSubscriber(client);
		assertThrows(NonExistingClientException.class, () -> subService.removeSubscriber(client));
	}

	//9. Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.
	@Test
	public void test9_addRemoveClientTwice() throws ExistingClientException, NullClientException, NonExistingClientException{
		SubscriptionService subService = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		subService.addSubscriber(client1);
		subService.addSubscriber(client2);
		subService.removeSubscriber(client1);
		subService.removeSubscriber(client2);
		assertFalse(subService.subscribers.contains(client1));
		assertFalse(subService.subscribers.contains(client2));
	}

	//10. Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.
	@Test
	public void test10_removeAll() throws ExistingClientException, NullClientException, NonExistingClientException{
		SubscriptionService subService = new SubscriptionService();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		subService.addSubscriber(client1);
		subService.addSubscriber(client2);
		subService.removeSubscriber(client1);
		subService.removeSubscriber(client2);
		assertTrue(subService.subscribers.isEmpty());
	}

	//11. Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).

	@Test
	public void test11_clientHasMail() throws NullClientException, ExistingClientException {
		SubscriptionService subService = new SubscriptionService();
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(true);
		subService.addSubscriber(client);
		subService.sendMessage(message);

		verify(client).hasEmail();
		verify(client, times(1)).receiveMessage(message);
	}

	//12. Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).
	@Test
	public void test11_clientHasNoMail() throws NullClientException, ExistingClientException {
		SubscriptionService subService = new SubscriptionService();
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(false);
		subService.addSubscriber(client);
		subService.sendMessage(message);

		verify(client, never()).receiveMessage(message);
	}


	//13.  Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
	@Test
	public void test12_clientNotSubscribed() throws NullClientException, ExistingClientException {
		SubscriptionService subService = new SubscriptionService();

		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(false);

		subService.sendMessage(message);
		verify(client, never()).receiveMessage(message);
	}
}


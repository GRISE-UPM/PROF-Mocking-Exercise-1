package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class SubscriptionServiceTest {

	Client client1;
	Client client2;
	Client client3;
	Message message;
	SubscriptionService subService;

	@BeforeEach
	public void setUp(TestInfo info) {
		if (!info.getDisplayName().contains("Test 1") || !info.getDisplayName().contains("Test 7")) {
			if (info.getDisplayName().contains("Test 4") || info.getDisplayName().contains("Test 8")
					|| info.getDisplayName().contains("Test 9") || info.getDisplayName().contains("Test 12")) {
				client2 = mock(Client.class);
				client3 = mock(Client.class);
			}
			client1 = mock(Client.class);
			
			if (info.getDisplayName().contains("Test 10") || info.getDisplayName().contains("Test 11")
					|| info.getDisplayName().contains("Test 12") || info.getDisplayName().contains("Test 13")) {
				message = mock(Message.class);
			}
		}

		subService = new SubscriptionService();
	}


	@Test
	@DisplayName("Test 1: suscribe null client.")
	public void subscribeNullClient() {
		assertThrows(NullClientException.class, () -> subService.addSubscriber(null));
	}

	@Test
	@DisplayName("Test 2: suscribe client.")
	public void subscribeClient() {
		try {
			subService.addSubscriber(client1);
			
			assertTrue(subService.subscribers.contains(client1));
		} catch (NullClientException e) {
			fail("No se ha podido suscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		}
	}

	@Test
	@DisplayName("Test 3: subscribe existing client.")
	public void subscribeExistingClient() {
		try {
			subService.addSubscriber(client1);
			
			assertThrows(ExistingClientException.class, () -> subService.addSubscriber(client1));
		} catch (NullClientException e) {
			fail("No se ha podido suscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		}
	}

	@Test
	@DisplayName("Test 4: subscribe multiple clients.")
	public void subscribeMultipleClients() {
		try {
			subService.addSubscriber(client1);
			subService.addSubscriber(client2);
			subService.addSubscriber(client3);
			
			assertTrue(subService.subscribers.contains(client1));
			assertTrue(subService.subscribers.contains(client2));
			assertTrue(subService.subscribers.contains(client3));
		} catch (NullClientException e) {
			fail("No se ha podido suscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		}
	}

	@Test
	@DisplayName("Test 5: delete client subscription.")
	public void deleteClientSubscription() {
		try {
			subService.addSubscriber(client1);
			
			subService.removeSubscriber(client1);
			
			assertTrue(!subService.subscribers.contains(client1) && subService.subscribers.size() == 0);
		} catch (NullClientException e) {
			fail("No se ha podido suscribir o desuscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		} catch (NonExistingClientException e) {
			fail("No se ha podido desuscribir al cliente debido a que no existe dicho cliente.");
		}
	}

	@Test
	@DisplayName("Test 6: delete non existing client subscription.")
	public void deleteNonExistingClientSubscription() {
		assertThrows(NonExistingClientException.class, () -> subService.removeSubscriber(client1));
	}
	
	@Test
	@DisplayName("Test 7: delete null client susbscription.")
	public void deleteNullClientSubscription() {
		assertThrows(NullClientException.class, () -> subService.removeSubscriber(null));
	}


	@Test
	@DisplayName("Test 8: delete multiple client subscription.")
	public void deleteMultipleClientSubscriptions() {
		try {
			subService.addSubscriber(client1);
			subService.addSubscriber(client2);
			subService.addSubscriber(client3);
			
			subService.removeSubscriber(client1);
			subService.removeSubscriber(client3);
			
			assertTrue(!subService.subscribers.contains(client1) && subService.subscribers.contains(client2) 
					&& !subService.subscribers.contains(client3) && subService.subscribers.size() == 1);
		} catch (NullClientException e) {
			fail("No se ha podido suscribir o desuscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		} catch (NonExistingClientException e) {
			fail("No se ha podido desuscribir al cliente debido a que no existe dicho cliente.");
		}
	}

	@Test
	@DisplayName("Test 9: delete all client subscriptions.")
	public void deleteAllClientSubscriptions() {
		try {
			subService.addSubscriber(client1);
			subService.addSubscriber(client2);
			subService.addSubscriber(client3);
			
			subService.removeSubscriber(client1);
			subService.removeSubscriber(client2);
			subService.removeSubscriber(client3);
			
			assertTrue(!subService.subscribers.contains(client1) && !subService.subscribers.contains(client2) 
					&& !subService.subscribers.contains(client3) && subService.subscribers.size() == 0);
		} catch (NullClientException e) {
			fail("No se ha podido suscribir o desuscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		} catch (NonExistingClientException e) {
			fail("No se ha podido desuscribir al cliente debido a que no existe dicho cliente.");
		}
	}

	@Test
	@DisplayName("Test 10: receive message if client has email.")
	public void receiveMessageIfClientHasEmail() {
		try {
			subService.addSubscriber(client1);
			
			when(client1.hasEmail()).thenReturn(true);
			
			subService.sendMessage(message);
			
			verify(client1, times(1)).receiveMessage(message);
			
			subService.sendMessage(message);
			
			verify(client1, times(2)).receiveMessage(message);
		} catch (NullClientException e) {
			fail("No se ha podido suscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		}
	}

	@Test
	@DisplayName("Test 11: does not receive message if client has not email.")
	public void doesNotReceiveMessageIfClientHasNotEmail() {
		try {
			subService.addSubscriber(client1);
			
			when(client1.hasEmail()).thenReturn(false);
			
			subService.sendMessage(message);
			
			verify(client1, times(0)).receiveMessage(message);
		} catch (NullClientException e) {
			fail("No se ha podido suscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		}
	}
	
	@Test
	@DisplayName("Test 12: receive message if clients has email.")
	public void receiveMessageIfClientsHasEmail() {
		try {
			subService.addSubscriber(client1);
			subService.addSubscriber(client2);
			subService.addSubscriber(client3);
			
			when(client1.hasEmail()).thenReturn(true);
			when(client2.hasEmail()).thenReturn(true);
			when(client3.hasEmail()).thenReturn(false);
			
			subService.sendMessage(message);
			
			verify(client1, times(1)).receiveMessage(message);
			verify(client2, times(1)).receiveMessage(message);
			verify(client3, times(0)).receiveMessage(message);
		} catch (NullClientException e) {
			fail("No se ha podido suscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		}
	}

	@Test
	@DisplayName("Test 13: does not receive message if client unsuscribe.")
	public void doesNotReceiveMessageIfClientUnsubscribe() {
		try {
			subService.addSubscriber(client1);
			when(client1.hasEmail()).thenReturn(true);
			subService.removeSubscriber(client1);
			verify(client1, times(0)).receiveMessage(message);
		} catch (NullClientException e) {
			fail("No se ha podido suscribir o desuscribir al cliente debido a que no se ha inicializado.");
		} catch (ExistingClientException e) {
			fail("No se ha podido suscribir al cliente debido a que ya existe.");
		} catch (NonExistingClientException e) {
			fail("No se ha podido desuscribir al cliente debido a que no existe dicho cliente.");
		}
	}


}

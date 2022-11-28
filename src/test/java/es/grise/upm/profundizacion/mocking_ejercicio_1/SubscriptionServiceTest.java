package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


public class SubscriptionServiceTest {

	SubscriptionService subscriptionService;
	Client client1;
	Client client2;
    Message message;

	@BeforeAll
	static public void beforeAll() {
		System.out.println("Empiezan tests");
	}

	@BeforeEach
	public void beforeEach() {
		client1 = mock(Client.class);
		client2 = mock(Client.class);
		message = mock(Message.class);
		subscriptionService = new SubscriptionService();
		System.out.println("Empieza test");
	}
	
	@AfterEach
	public void afterEach() {
		System.out.println("Test terminado");
	}

	@AfterAll
	static public void afterAll() {
		System.out.println("Terminan tests");
	}	
	
	// STATE TESTS
	
	@Test
    public void addNullClientTest() {
        assertThrows(NullClientException.class, () -> subscriptionService.addSubscriber(null));
    }
	
	@Test
    public void addNewClientTest() throws Exception {
        subscriptionService.addSubscriber(client1);
        assertEquals(true, subscriptionService.subscribers.contains(client1));
    }
	
	@Test
    public void addSameClientTest() throws Exception {
        subscriptionService.addSubscriber(client1);
        assertThrows(ExistingClientException.class, () -> subscriptionService.addSubscriber(client1));
    }
	
	@Test
    public void addMultipleClientsTest() throws Exception {
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        assertEquals(true, subscriptionService.subscribers.contains(client1));
        assertEquals(true, subscriptionService.subscribers.contains(client2));
    }
	
	@Test
    public void removeNullClientTest() {
        assertThrows(NullClientException.class, () -> subscriptionService.removeSubscriber(null));
    }
	
	@Test
    public void removeNotStoredClientTest() throws Exception {
        subscriptionService.addSubscriber(client1);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client2));
    }
	
	@Test
    public void removeStoredClientTest() throws Exception {
        subscriptionService.addSubscriber(client1);
        subscriptionService.removeSubscriber(client1);
        assertEquals(false, subscriptionService.subscribers.contains(client1));
    }
	
	@Test
    public void removeStoredClientTwiceTest() throws Exception {
        subscriptionService.addSubscriber(client1);
        subscriptionService.removeSubscriber(client1);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client1));
    }
	
	@Test
    public void removeMultipleStoredClientsTest() throws Exception {
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        assertEquals(false, subscriptionService.subscribers.contains(client1));
        assertEquals(false, subscriptionService.subscribers.contains(client2));
    }
	
	
	
	// INTERACTION TESTS
	
	/* 

		Interaction tests
		
		Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).
		
		Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).
		
		Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).
		
		Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
		

	 */
	 
}

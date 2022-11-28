package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SubscriptionServiceTest {
	
	private SubscriptionService ss;
	private Client c;
	
	@BeforeEach
	public void setUp() {
		ss = new SubscriptionService();
	}
	
	// Apartado 1
	@Test
	public void addNullClientTest() {
		
		// Initialization
		c = null;
		
		// Assertion
		assertThrows(NullClientException.class, () -> {
			ss.addSubscriber(c);
		});
	}

	// Apartado 2
	@Test
	public void storeClientTest() {
		
		// Initialization
		c = mock(Client.class);
		
		// Operation
		try {
			ss.addSubscriber(c);
		} catch (NullClientException | ExistingClientException e) {
			fail("No debería lanzar excpción " + e.toString());
		}
		
		// Assertion
		assertTrue(ss.subscribers.contains(c));
	}
	
	// Apartado 3
	@Test
	public void addClientTwiceTest() {
		
		// Initialization
		c = mock(Client.class);
		Client c_aux = c;
		
		// Assertion
		assertThrows(ExistingClientException.class, () -> {
			ss.addSubscriber(c);
			ss.addSubscriber(c_aux);
		});
	}
	
	// Apartado 4
	@Test
	public void storeManyClientsTest() {
		
		// Initialization
		c = mock(Client.class);
		Client c_aux_1 = mock(Client.class);
		Client c_aux_2 = mock(Client.class);

		// Operation
		try {
			ss.addSubscriber(c);
			ss.addSubscriber(c_aux_1);
			ss.addSubscriber(c_aux_2);
		} catch (NullClientException | ExistingClientException e) {
			fail("No debería lanzar excpción " + e.toString());
		}
		
		// Assertion
		assertTrue(ss.subscribers.contains(c) &&
				ss.subscribers.contains(c_aux_1) &&
				ss.subscribers.contains(c_aux_2));
	}
	
	// Apartado 5
	@Test
	public void removeNullClient() {
		
		// Initialization
		c = null;
		
		// Assertion
		assertThrows(NullClientException.class, () -> {
			ss.removeSubscriber(c);
		});
	}
	
	// Apartado 6
	@Test
	public void removeNoExistingClientTest() {
		
		// Initialization
		c = mock(Client.class);
		
		// Assertion
		assertThrows(NonExistingClientException.class, () -> {
			ss.removeSubscriber(c);
		});
	}
	
	// Apartado 7
	@Test
	public void removeClientTest() {
		
		// Initialization
		c = mock(Client.class);

		// Operation
		try {
			ss.addSubscriber(c);
		} catch (NullClientException | ExistingClientException e) {
			fail("Al añadir el Cliente no debería lanzar excpción " + e.toString());
		}
		
		// Assertion
		try {
			ss.removeSubscriber(c);
		} catch (NullClientException | NonExistingClientException e) {
			fail("No debería lanzar excpción " + e.toString());
		}
	}
	
	// Apartado 8
	@Test
	public void removeClientTwiceTest() {
		
		// Initialization
		c = mock(Client.class);

		// Operation
		try {
			ss.addSubscriber(c);
			ss.removeSubscriber(c);
		} catch (NullClientException | ExistingClientException | 
				NonExistingClientException e) {
			fail("Al añadir/borrar el Cliente la primera vez no debería lanzar excpción " + e.toString());
		}
		
		// Assertion
		assertThrows(NonExistingClientException.class, () -> {
			ss.removeSubscriber(c);
		});
	}
	
	// Apartado 9
	@Test
	public void removeManyClientsTest() {
		
		// Initialization
		c = mock(Client.class);
		Client c_aux_1 = mock(Client.class);
		Client c_aux_2 = mock(Client.class);

		// Operation
		try {
			ss.addSubscriber(c);
			ss.addSubscriber(c_aux_1);
			ss.addSubscriber(c_aux_2);
		} catch (NullClientException | ExistingClientException e) {
			fail("Al añadir los Cliente(s) no debería lanzar excpción " + e.toString());
		}
		
		// Assertion
		try {
			ss.removeSubscriber(c);
			ss.removeSubscriber(c_aux_1);
		} catch (NullClientException | NonExistingClientException e) {
			fail("Al eliminar los Cliente(s) no debería lanzar excpción " + e.toString());
		}
	}
	
	// Apartado 10
	@Test
	public void removeAllClientsTest() {
		
		// Initialization
		c = mock(Client.class);
		Client c_aux_1 = mock(Client.class);
		Client c_aux_2 = mock(Client.class);

		// Operation
		try {
			ss.addSubscriber(c);
			ss.addSubscriber(c_aux_1);
			ss.addSubscriber(c_aux_2);
		} catch (NullClientException | ExistingClientException e) {
			fail("Al añadir los Cliente(s) no debería lanzar excpción " + e.toString());
		}
		
		// Assertion
		try {
			ss.removeSubscriber(c);
			ss.removeSubscriber(c_aux_1);
			ss.removeSubscriber(c_aux_2);
		} catch (NullClientException | NonExistingClientException e) {
			fail("Al eliminar los Cliente(s) no debería lanzar excpción " + e.toString());
		}
	}
	
	// Interaction tests
	
	// Apartado 11
	@Test
	public void emailSentTest() {
		
		// Initialization
		c = mock(Client.class);
		Message m = mock(Message.class);
		when(c.hasEmail()).thenReturn(true);

		// Operation
		try {
			ss.addSubscriber(c);
		} catch (NullClientException | ExistingClientException e) {
			fail("No debería lanzar excpción " + e.toString());
		}
		ss.sendMessage(m);
		
		// Assertion
		verify(c, times(1)).receiveMessage(any(Message.class));
	}
	
	// Apartado 12
	@Test
	public void emailNotSentToSubscriberTest() {
		
		// Initialization
		c = mock(Client.class);
		Message m = mock(Message.class);
		when(c.hasEmail()).thenReturn(false);

		// Operation
		try {
			ss.addSubscriber(c);
		} catch (NullClientException | ExistingClientException e) {
			fail("No debería lanzar excpción " + e.toString());
		}
		ss.sendMessage(m);
		
		// Assertion
		verify(c, never()).receiveMessage(any(Message.class));
	}
	
	// Apartado 13
	@Test
	public void manyEmailsSentTest() {
		
		// Initialization
		c = mock(Client.class);
		Client c_aux_1 = mock(Client.class);
		Client c_aux_2 = mock(Client.class);
		Message m = mock(Message.class);
		when(c.hasEmail()).thenReturn(true);
		when(c_aux_1.hasEmail()).thenReturn(true);
		when(c_aux_2.hasEmail()).thenReturn(true);

		// Operation
		try {
			ss.addSubscriber(c);
			ss.addSubscriber(c_aux_1);
			ss.addSubscriber(c_aux_2);
		} catch (NullClientException | ExistingClientException e) {
			fail("No debería lanzar excpción " + e.toString());
		}
		ss.sendMessage(m);
		
		// Assertion
		verify(c, times(1)).receiveMessage(any(Message.class));
		verify(c_aux_1, times(1)).receiveMessage(any(Message.class));
		verify(c_aux_2, times(1)).receiveMessage(any(Message.class));
	}
	
	// Apartado 14
	@Test
	public void emailNotSentToNonSubscriberTest() {
		
		// Initialization
		c = mock(Client.class);
		Message m = mock(Message.class);
		when(c.hasEmail()).thenReturn(true);

		// Operation
		try {
			ss.addSubscriber(c);
			ss.removeSubscriber(c);
		} catch (NullClientException | ExistingClientException |
				NonExistingClientException e) {
			fail("No debería lanzar excpción " + e.toString());
		}
		ss.sendMessage(m);
		
		// Assertion
		verify(c, never()).receiveMessage(any(Message.class));
	}
}

package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SubscriptionServiceTest {
	
	private SubscriptionService s;
	private Client c1;
	private Client c2;
	private Client c3;
	private Message m;
	
	@BeforeEach
	public void setup() {
		s = new SubscriptionService();
		c1 = mock(Client.class);
		c2 = mock(Client.class);
		c3 = mock(Client.class); 
		m = mock(Message.class);
	}
	
	//state tests
	
	@Test
	@DisplayName(value = "No se puede añadir un Client null a la lista subscribers.")
	public void nullClientTest() {
		assertThrows(NullClientException.class, () -> s.addSubscriber(null));
	}
	
	@Test
	@DisplayName(value = "Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.")
	public void checkAddSubscriberTest() throws NullClientException, ExistingClientException {
		s.addSubscriber(c1);
        assertEquals(true, s.subscribers.contains(c1));    
	}
	
	@Test
	@DisplayName(value = "No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.")
	public void checkRepeatedClientTest() {
		try {
			s.addSubscriber(c1);
			assertThrows(ExistingClientException.class, () -> s.addSubscriber(c1));
		} catch (NullClientException | ExistingClientException e) {
			fail();
		}
        
	}
	
	@Test
	@DisplayName(value = "Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.")
	public void almacenarClientesTest() {
		try {
			s.addSubscriber(c1);
			s.addSubscriber(c2);
			s.addSubscriber(c3);
			assertTrue(s.subscribers.contains(c1));
			assertTrue(s.subscribers.contains(c2));
			assertTrue(s.subscribers.contains(c3));
		} catch (NullClientException | ExistingClientException e) {
			fail();
		}
	}
	
	@Test
	@DisplayName(value = "No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.")
	public void noEliminarClienteTest() {
		assertThrows(NullClientException.class, () -> s.removeSubscriber(null));
	}
	
	@Test
	@DisplayName(value = "No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
	public void noExisteClienteEliminarTest() {
		 assertThrows(NonExistingClientException.class, () -> s.removeSubscriber(c1));
	}
	
	@Test
	@DisplayName(value = "Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.")
	public void eliminarClienteExistente() {
		try {
			s.addSubscriber(c1);
			s.removeSubscriber(c1);
	        assertFalse(s.subscribers.contains(c1));
		} catch (NullClientException | ExistingClientException | NonExistingClientException e) {
			fail();
		}
        
	}
	
	@Test
	@DisplayName(value = "No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
	public void noEliminarClienteVariasVecesTest() {
		try {
			s.addSubscriber(c1);
	        s.removeSubscriber(c1);
	        assertThrows(NonExistingClientException.class, () -> s.removeSubscriber(c1));
		} catch (NullClientException | ExistingClientException | NonExistingClientException e) {
			fail();
		}

	}
	
	@Test
	@DisplayName(value = "Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.")
	public void eliminarVariosClientesTest() {
		try {
			s.addSubscriber(c1);
	        s.addSubscriber(c2);
	
	        s.removeSubscriber(c1);
	        s.removeSubscriber(c2);
	
	        assertFalse(s.subscribers.contains(c1));
	        assertFalse(s.subscribers.contains(c2));
		} catch (NullClientException | ExistingClientException | NonExistingClientException e) {
			fail();
		}
	}
	
	@Test
	@DisplayName(value = "Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.")
	public void eliminarTodosClientesTest() {
		try {
			s.addSubscriber(c1);
	        s.addSubscriber(c2);

	        s.removeSubscriber(c1);
	        s.removeSubscriber(c2);

	        assertFalse(s.subscribers.contains(c1));
	        assertFalse(s.subscribers.contains(c2));
	        assertTrue(s.subscribers.isEmpty());
		} catch (NullClientException | ExistingClientException | NonExistingClientException e) {
			fail();
		}
	}
	
	
	//interaction tests
	
	@Test
	@DisplayName(value = "Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).")
	public void enviarMensajeSiHayCorreoTest() {
		when(c1.hasEmail()).thenReturn(true);
        try {
			s.addSubscriber(c1);
	        s.sendMessage(m);
	        verify(c1).receiveMessage(m);
		} catch (NullClientException | ExistingClientException e) {
			fail();
		}

	}
	
	@Test
	@DisplayName(value = "Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).")
	public void noRecibirMensajeSiNoHayCorreoTest() {
		when(c1.hasEmail()).thenReturn(false);
		try {
			s.addSubscriber(c1);
			s.sendMessage(m);
			verify(c1).hasEmail();
			verifyNoMoreInteractions(c1);
		} catch (NullClientException | ExistingClientException e) {
			fail();
		}
		
	}
	
	@Test
	@DisplayName(value = "Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).")
	public void variosMensajesVariosUsuariosTest() {
		when(c1.hasEmail()).thenReturn(true);
        when(c2.hasEmail()).thenReturn(true);
        when(c3.hasEmail()).thenReturn(true);

        try {
			s.addSubscriber(c1);
	        s.addSubscriber(c2);
	        s.addSubscriber(c3);
	        s.sendMessage(m);

	        verify(c1).receiveMessage(m);
	        verify(c2).receiveMessage(m);
	        verify(c3).receiveMessage(m);
		} catch (NullClientException | ExistingClientException e) {
			fail();
		}

	}
	
	@Test
	@DisplayName(value = "Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).")
	public void noRecibirMensajeAlDesuscribirTest() {
		when(c1.hasEmail()).thenReturn(true);
        when(c2.hasEmail()).thenReturn(true);
        try {
	        s.addSubscriber(c1);
	        s.addSubscriber(c2);
	        s.removeSubscriber(c1);
	        s.sendMessage(m);
	
	        verifyNoMoreInteractions(c1);
	        verify(c2).receiveMessage(m);
		} catch (NullClientException | ExistingClientException | NonExistingClientException e) {
			fail();
		}
	}
	
}

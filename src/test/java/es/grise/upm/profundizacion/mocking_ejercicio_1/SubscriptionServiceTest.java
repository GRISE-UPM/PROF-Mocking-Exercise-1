package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.*;



public class SubscriptionServiceTest {
	
	
	
	private SubscriptionService ss;
	
	@BeforeEach
	public void init() {
		ss = new SubscriptionService();	
		
	}
	
	@Test
	@DisplayName(value = "No se puede añadir un Client null a la lista subscribers.")
	public void Tes_Add_Null_CLient() throws NullClientException, ExistingClientException {
	


		assertThrows(NullClientException.class, () -> ss.addSubscriber(null));
		
	}
	@Test
	@DisplayName(value ="Al añadir un Clientmediante addSubscriber(), éste Client se almacena en la lista subscribers.")
	public void Test_add_non_null_Client() throws NullClientException, ExistingClientException {
		
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		assertTrue(ss.subscribers.contains(c));
	}
	
	
	@Test
	@DisplayName(value ="No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.")
	public void Test_Add_Client_Contained() throws NullClientException, ExistingClientException {
		
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		assertThrows(ExistingClientException.class, () -> ss.addSubscriber(c));
	}
	
	@Test
	@DisplayName(value ="Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.")
	public void Test_Add_Multiple_Clients() throws NullClientException, ExistingClientException {
		
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		assertTrue(ss.subscribers.contains(c1));
		assertTrue(ss.subscribers.contains(c2));
	}
	
	@Test
	@DisplayName(value ="No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.")
	public void Test_Delete_Null_CLient() {
	
		assertThrows(NullClientException.class, () -> ss.removeSubscriber(null));
	}
	
	@Test
	@DisplayName(value ="No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
	public void Test_Delete_NON_Contained_CLient() {
		Client c = mock(Client.class);
		assertThrows(NonExistingClientException.class, () -> ss.removeSubscriber(c));
	}
	
	@Test
	@DisplayName(value ="Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.")
	public void Test_Delete_Contained_CLient() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		assertTrue(ss.subscribers.contains(c));
		ss.removeSubscriber(c);
        assertFalse(ss.subscribers.contains(c));
	}
	
	@Test
	@DisplayName(value ="No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
	public void Test_Delete_Contained_CLient_Twice() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		
		ss.removeSubscriber(c);
		assertThrows(NonExistingClientException.class, () -> ss.removeSubscriber(c));
	}
	
	@Test
	@DisplayName(value ="Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.")
	public void Test_Delete_Multiple_Contained_Clients() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		ss.removeSubscriber(c1);
		ss.removeSubscriber(c2);
		
		assertFalse(ss.subscribers.contains(c1));
		assertFalse(ss.subscribers.contains(c2));
		assertTrue(ss.subscribers.contains(c3));
		
		
	}
	
	@Test
	@DisplayName(value ="Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.")
	public void Test_Delete_All_Contained_Clients() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		ss.removeSubscriber(c1);
		ss.removeSubscriber(c2);
		ss.removeSubscriber(c3);
		assertFalse(ss.subscribers.contains(c1));
		assertFalse(ss.subscribers.contains(c2));
		assertFalse(ss.subscribers.contains(c2));
		assertTrue(ss.subscribers.isEmpty());
		
	}
	
	@Test
	@DisplayName(value = "Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).")
	public void Test_Receive_Message_If_Email() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client c = mock(Client.class);
		Message mensaje = mock(Message.class);
		when(c.hasEmail()).thenReturn(true);
		ss.addSubscriber(c);

		if(c.hasEmail()) {
			ss.sendMessage(mensaje);
	        verify(c).receiveMessage(mensaje);
		}else {
			fail();
		}
	}
		
		@Test
		@DisplayName(value = "Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).")
		public void Test__NOT_Receive_Message_If_NOT_Email() throws NullClientException, ExistingClientException, NonExistingClientException {
			Client c = mock(Client.class);
			Message mensaje = mock(Message.class);
			when(c.hasEmail()).thenReturn(false);
			ss.addSubscriber(c);
			ss.sendMessage(mensaje);
			
				
			verify(c).hasEmail();
			verifyNoMoreInteractions(c);
			
		 
		
		
	}
		
		@Test
		@DisplayName(value = "Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).")
		public void Test_Receive_Message_If_Email_Various_Clients() throws NullClientException, ExistingClientException, NonExistingClientException {
			Client c1 = mock(Client.class);
			Client c2 = mock(Client.class);
			Client c3 = mock(Client.class);
			when(c1.hasEmail()).thenReturn(true);
	        when(c2.hasEmail()).thenReturn(true);
	        when(c3.hasEmail()).thenReturn(true);
			Message mensaje = mock(Message.class);
			
			ss.addSubscriber(c1);
	        ss.addSubscriber(c2);
	        ss.addSubscriber(c3);
	        ss.sendMessage(mensaje);
			if(c1.hasEmail()) {
				verify(c1).receiveMessage(mensaje);
			}else if(c2.hasEmail()) {
				verify(c2).receiveMessage(mensaje);
			} else if (c3.hasEmail()) {
				verify(c3).receiveMessage(mensaje);
			}else {
				fail();
			}	
		
	}
		
		@Test
		@DisplayName(value = "Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).")
		public void Test_Not_Recive_Message_If_Desuscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
			Client c1 = mock(Client.class);
			Client c2 = mock(Client.class);
			when(c1.hasEmail()).thenReturn(true);
			when(c2.hasEmail()).thenReturn(true);
			Message mensaje = mock(Message.class);
			
			ss.addSubscriber(c1);
			ss.addSubscriber(c2);
			ss.removeSubscriber(c1);

	        ss.sendMessage(mensaje);
	        verifyNoMoreInteractions(c1);
	        verify(c2).receiveMessage(mensaje);
			
		
	}
		
	
}

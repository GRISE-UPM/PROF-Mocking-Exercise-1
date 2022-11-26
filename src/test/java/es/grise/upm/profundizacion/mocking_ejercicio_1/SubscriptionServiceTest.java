package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscriptionServiceTest {
	
	Client cl;
	SubscriptionService sserv;
	Message mssg;
	
	@BeforeEach
	public void start() {
		cl=mock(Client.class);
		sserv = new SubscriptionService();
		mssg = mock(Message.class);
	}

	/* ------------------ STATE TESTS --------------------- */
	
	/* - No se puede añadir un Client null a la lista subscribers. */
	@Test
	public void null_client() throws NullClientException{
		assertThrows(NullClientException.class, ()->sserv.addSubscriber(null));
	}
	
	/* - Al añadir un Clientmediante addSubscriber(), éste Client se almacena en la 
	 * lista subscribers. */
	@Test
	public void add_client_correctly() throws NullClientException, ExistingClientException{
		sserv.addSubscriber(cl);
		assertTrue(sserv.subscribers.contains(cl));
	}
	
	/* - No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista 
	 * subscribers Al hacerlo, se lanza la excepción ExistingClientException.*/
	@Test
	public void same_client() throws NullClientException, ExistingClientException {
		sserv.addSubscriber(cl);
		assertThrows(ExistingClientException.class, ()->sserv.addSubscriber(cl));
	}
		
	/* - Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en 
	 * la lista subscribers.*/
	@Test
	public void some_clients() throws NullClientException, ExistingClientException {
		Client cl_aux=mock(Client.class);
		Client cl_aux2=mock(Client.class);
		Client cls[]= {cl,cl_aux,cl_aux2};
		for(int i=0;i<cls.length;i++) {
			sserv.addSubscriber(cls[i]);
			assertTrue(sserv.subscribers.contains(cls[i]));
		}		
		
	}
	
	/* - No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NullClientException.*/
	@Test
	public void not_null_client_remove() {
		assertThrows(NullClientException.class, ()-> sserv.removeSubscriber(null));
	}
	
	/* - No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la 
	 * lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.*/
	@Test
	public void not_client_remove() throws NullClientException, ExistingClientException {
		Client cl_aux=mock(Client.class);
		sserv.addSubscriber(cl_aux);
		assertThrows(NonExistingClientException.class,()->sserv.removeSubscriber(cl));
	}
	
	/* - Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista 
	 * subscribers.*/
		@Test
	public void remove_client() throws NullClientException, ExistingClientException {
		sserv.addSubscriber(cl);
		assertFalse(sserv.subscribers.contains(cl));
	}
	
	/* - No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista 
	 * subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.*/
	@Test
	public void remove_two_times_same_client() throws NullClientException, ExistingClientException,
	NonExistingClientException {
		Client cl_aux=mock(Client.class);
		Client cl_aux2=mock(Client.class);
		Client cls[]= {cl,cl_aux,cl_aux2};
		for(int i=0;i<cls.length;i++) {
			sserv.addSubscriber(cls[i]);
		}		
		sserv.removeSubscriber(cl);
		assertThrows(NonExistingClientException.class,()->sserv.removeSubscriber(cl));			
	}
	
	/* - Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en 
	 * la lista subscribers.*/
	@Test
	public void remove_some_clients() throws NullClientException, ExistingClientException,
	NonExistingClientException {
		Client cl_aux=mock(Client.class);
		Client cl_aux2=mock(Client.class);
		Client cls[]= {cl,cl_aux,cl_aux2};
		for(int i=0;i<cls.length;i++) {
			sserv.addSubscriber(cls[i]);
		}
		for(int j=1;j<cls.length;j++) {
			sserv.removeSubscriber(cls[j]);
			assertFalse(sserv.subscribers.contains(cls[j]));
		}
	}
	
	/* - Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en 
	 * la lista subscribers.*/
	@Test
	public void remove_all_clients() throws NullClientException, ExistingClientException,
	NonExistingClientException {
		Client cl_aux=mock(Client.class);
		Client cl_aux2=mock(Client.class);
		Client cls[]= {cl,cl_aux,cl_aux2};
		for(int i=0;i<cls.length;i++) {
			sserv.addSubscriber(cls[i]);
			sserv.removeSubscriber(cls[i]);
			assertFalse(sserv.subscribers.contains(cls[i]));
		}
	}
	
	/* ------------ INTERACTION TESTS ------------ */
	
	/* - Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método 
	 * hasEmail() == true).*/
	@Test
	public void send_message() throws NullClientException, ExistingClientException {
		sserv.addSubscriber(cl);
		when(cl.hasEmail()).thenReturn(true);
		sserv.sendMessage(mssg);
		verify(cl).receiveMessage(mssg);
	}
	
	/* - Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email 
	 * (método hasEmail() == false).*/
	@Test
	public void not_send_message() throws NullClientException, ExistingClientException {
		sserv.addSubscriber(cl);
		when(cl.hasEmail()).thenReturn(false);		
		sserv.sendMessage(mssg);
		verify(cl).hasEmail();
		verifyNoMoreInteractions(cl);
	}
	
	
	/* - Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email 
	 * (método hasEmail() == true).*/
	@Test
	public void some_messages() throws NullClientException, ExistingClientException {
		Client cl_aux=mock(Client.class);
		Client cl_aux2=mock(Client.class);
		Client cls[]= {cl,cl_aux,cl_aux2};
		for(int i=0;i<cls.length;i++) {		
			sserv.addSubscriber(cls[i]);
			when(cls[i].hasEmail()).thenReturn(true);
			sserv.sendMessage(mssg); //Añadido ahora 
			verify(cls[i]).receiveMessage(mssg);
		}
	}
	
	/* - Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).*/
	@Test
	public void less_messages() throws NullClientException, ExistingClientException,
	NonExistingClientException {
		Client cl_aux=mock(Client.class);
		Client cl_aux2=mock(Client.class);
		Client cls[]= {cl,cl_aux,cl_aux2};
		for(int i=0;i<cls.length;i++) {		
			sserv.addSubscriber(cls[i]);
			when(cls[i].hasEmail()).thenReturn(true);			
		}
		sserv.removeSubscriber(cl);
		sserv.sendMessage(mssg);
		for(int j=1;j<cls.length;j++) {
			verify(cls[j]).receiveMessage(mssg);
		}
		
	}
	
}
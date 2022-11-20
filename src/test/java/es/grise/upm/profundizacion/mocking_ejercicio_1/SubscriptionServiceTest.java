package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class SubscriptionServiceTest {
	
	private SubscriptionService s = new SubscriptionService();
	private Client c1;
	private Client c2;
	private Client c3;
	private Message m;
	
	@BeforeEach
	public void setup() {
		c1 = mock(Client.class);
		c2 = mock(Client.class);
		c3 = mock(Client.class); 
		m = mock(Message.class);
	}
	
	//state tests
	
	@Test
	@DisplayName(value = "No se puede añadir un Client null a la lista subscribers.")
	public void nullClientTest() {
		fail();
	}
	
	@Test
	@DisplayName(value = "Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.")
	public void checkAddSubscriberTest() {
		fail();      
	}
	
	@Test
	@DisplayName(value = "No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.")
	public void checkRepeatedClientTest() {
		
	}
	
	@Test
	@DisplayName(value = "Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.")
	public void almacenarClientesTest() {
		fail();
	}
	
	@Test
	@DisplayName(value = "No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.")
	public void noEliminarClienteTest() {
		fail();
	}
	
	@Test
	@DisplayName(value = "No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
	public void noExisteClienteEliminarTest() {
		fail();
	}
	
	@Test
	@DisplayName(value = "Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.")
	public void eliminarClienteExistente() {
		fail();
	}
	
	@Test
	@DisplayName(value = "No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
	public void noEliminarClienteVariasVecesTest() {
		fail();
	}
	
	@Test
	@DisplayName(value = "Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.")
	public void eliminarVariosClientesTest() {
		
	}
	
	@Test
	@DisplayName(value = "Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.")
	public void eliminarTodosClientesTest() {
		fail();
	}
	
	
	//interaction tests
	
	@Test
	@DisplayName(value = "Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).")
	public void enviarMensajeSiHayCorreoTest() {
		fail();
	}
	
	@Test
	@DisplayName(value = "Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).")
	public void noRecibirMensajeSiNoHayCorreoTest() {
		fail();
	}
	
	@Test
	@DisplayName(value = "Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).")
	public void variosMensajesVariosUsuariosTest() {
		fail();
	}
	
	@Test
	@DisplayName(value = "Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).")
	public void noRecibirMensajeAlDesuscribirTest() {
		fail();
	}
	
}

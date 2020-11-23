package es.grise.upm.profundizacion.tema8;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.internal.listeners.CollectCreatedMocks;

import static org.mockito.Mockito.*;
public class SubscriptionServiceTest {
	
	
	/*
	 * State tests
	 */
	private SubscriptionService subs;
	
	
	@BeforeEach
	public void setUp() {
		subs = new SubscriptionService(); 
	}
	
	
	/**
	 * Test que trara de añadir un usuario null
	 * resultado: Lanza la excepcion NullClientException()
	 */
	@Test 
	public void user_Null_Test() {
		
		Client client = null;
		assertThrows(NullClientException.class, ()-> subs.addSubscriber(client));
		
	}
	
	/**
	 * Test que prueba a añadir un usuario correcto
	 * resultado: El test no lanza ninguna excepción ya que se ha almacenado el usuario debidamente
	 */ 
	@Test
	public void correct_User_Test() throws NullClientException, ExistingClientException {
		
		Client client = mock(Client.class);	
		subs.addSubscriber(client);
		
		assertTrue(subs.subscribers.contains(client));
	
	}
	
	/**
	 * Test que prueba que pasa cuando se añaden dos clientes iguales
	 * resultado: El test genera una excepcion ya que no esta permitido que haya dos clientes iguales
	 */
	@Test
	public void Duplicated_add_Fail_Test() {
		
		Client client = mock(Client.class);
		subs.subscribers.add(client);
		assertThrows(ExistingClientException.class, () -> subs.addSubscriber(client));
	}
	
	/**
	 * Test que prueba que pasa cuando se añaden dos clientes iguales
	 * resultado: El test genera una excepcion ya que no esta permitido que haya dos clientes iguales
	 */
	@Test
	public void add_Multiple_Correct_Users() throws NullClientException, ExistingClientException {
		
		Client client = mock(Client.class);
		Client client_1 = mock(Client.class);
		Client client_2 = mock(Client.class);
		
		subs.addSubscriber(client);
		subs.addSubscriber(client_1);
		subs.addSubscriber(client_2);
		
		assertTrue(subs.subscribers.contains(client) &&
				subs.subscribers.contains(client_1) &&
				subs.subscribers.contains(client_2));
	}
	
	/**
	 * Test que trata de eliminar un cliente nulo
	 * resultado: Devuelve una excepción ya que no se puede elminar un cliente nulo
	 */
	@Test
	public void remove_Null_User_Exception() {
		Client cliente = null;
		assertThrows(NullClientException.class, ()-> subs.addSubscriber(cliente));
	}
	
	/**
	 * Test que trata de borar un cliente que no existe
	 * resultado: Se genera una excepción ya que no se puede borrar un cliente no existente
	 */
	@Test
	public void remove_A_Non_Exist_User_Test() {
	
		Client cliente = mock(Client.class);
		assertThrows(NonExistingClientException.class, ()-> subs.removeSubscriber(cliente) );
	}
	
	/**
	 * Test que trata de eleminar un cliente existe
	 * resultado: El test no genera niguna excepcion 
	 * @throws ExistingClientException 
	 * @throws NullClientException 
	 * @throws NonExistingClientException 
	 */
	@Test
	public void remove_Existing_User_Test() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Client client = mock(Client.class);
		subs.addSubscriber(client);
		subs.removeSubscriber(client);
		assertTrue(!subs.subscribers.contains(client));
	}
	
	
	/**
	 * El test trata de borrar dos veces el mismo cliente
	 * resultado: El test genera una excepcion ya que la primera vez  
	 * se elimina el cliente pero luego intenta borra uno no existente
	 * @throws NonExistingClientException 
	 * @throws NullClientException 
	 */
	@Test
	public void remove_Duplicated_User_Exception_Test() throws NullClientException, NonExistingClientException {
		
		Client client = mock(Client.class);
		subs.subscribers.add(client);
		subs.removeSubscriber(client);
		
		assertThrows(NonExistingClientException.class, ()-> subs.removeSubscriber(client));
	}
	
	/**
	 * Test que trata de borrar varios clientes de la lista de suscriptores
	 * resultado: El test no genera ninguna excepcion
	 * @throws NonExistingClientException 
	 * @throws NullClientException 
	 */
	@Test
	public void remove_Correctly_Multiple_Clients_Test() throws NullClientException, NonExistingClientException {
		
		Client client = mock(Client.class);
		Client client_1 = mock(Client.class);
		Client client_2 = mock(Client.class);
		
		subs.subscribers.add(client);
		subs.subscribers.add(client_1);
		subs.subscribers.add(client_2);
		
		subs.removeSubscriber(client);
		subs.removeSubscriber(client_1);
		
		assertTrue(!subs.subscribers.contains(client) && 
				!subs.subscribers.contains(client_1));
		
	}
	
	/**
	 * Test que prueba a borrar todos los elementos de la lista de clientes
	 * resultado:  El test no genera ninguna excepcion
	 * @throws NonExistingClientException 
	 * @throws NullClientException 
	 */
	@Test
	public void remove_Correctly_All_Clients_Test() throws NullClientException, NonExistingClientException {
		
		Client client = mock(Client.class);
		Client client_1 = mock(Client.class);
		Client client_2 = mock(Client.class);
		
		subs.subscribers.add(client);
		subs.subscribers.add(client_1);
		subs.subscribers.add(client_2);
		
		subs.removeSubscriber(client);
		subs.removeSubscriber(client_1);
		subs.removeSubscriber(client_2);
		
		assertTrue(subs.subscribers.isEmpty());

	}
	
	/**
	 * Test que prueba si un cliente suscrito recibe mensajes
	 * resultado: El test no devolverá ninguna excepción verificando
	 * que todo funciona de manera correcta
	 */
	@Test
	public void client_Subscribed_recived_Correctly_Msg_Test() {
		
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(true);
		
		subs.subscribers.add(client);
		subs.sendMessage(message);
		
		
		verify(client).receiveMessage(message);
	}
	
	
	
	/**
	 * Test que prueba si un cliente suscrito no recibe mensajes
	 * resultado: El test no devolverá ninguna excepción verificando
	 * que todo funciona de manera correcta
	 */
	@Test
	public void client_Subscribed_No_Recived_Correctly_Msg_Test() {
		
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(false);
		
		subs.subscribers.add(client);
		subs.sendMessage(message);
		
		verify(client,times(0)).receiveMessage(message);
	}
	
	/**
	 * Test que prueba si un varios clientes suscritos reciben mensajes
	 * resultado: El test no devolverá ninguna excepción verificando
	 * que todo funciona de manera correcta
	 */
	@Test
	public void Multiple_Client_Subscribed_Recived_Correctly_Msg_Test() {
		
		Client client = mock(Client.class);
		Client client_1 = mock(Client.class);
		Client client_2 = mock(Client.class);

		Message message = mock(Message.class);
		when(client.hasEmail()).thenReturn(true);
		when(client_1.hasEmail()).thenReturn(true);
		when(client_2.hasEmail()).thenReturn(true);
		
		subs.subscribers.add(client);
		subs.subscribers.add(client_1);
		subs.subscribers.add(client_2);
		subs.sendMessage(message);
		
		verify(client).receiveMessage(message);
		verify(client_1).receiveMessage(message);
		verify(client_2).receiveMessage(message);

	}
	
	/**
	 * Test que prueba si un cliente no suscritos no recibe mensajes
	 * resultado: El test no devolverá ninguna excepción verificando
	 * que todo funciona de manera correcta
	 * @throws NonExistingClientException 
	 * @throws NullClientException 
	 */
	@Test
	public void Multiple_Client_Subscribed_No_Recived_Correctly_Msg_Test() throws NullClientException, NonExistingClientException {
		
		Client client = mock(Client.class);
		Message message = mock(Message.class);
		
		subs.subscribers.add(client);
		subs.removeSubscriber(client);
		subs.sendMessage(message);
		
		verify(client,times(0)).receiveMessage(message);
	}
}

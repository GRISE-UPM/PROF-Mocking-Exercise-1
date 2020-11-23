package es.grise.upm.profundizacion.tema8;


import static org.mockito.Mockito.*;

import java.util.Iterator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SubscriptionServiceTest {
	private SubscriptionService servicio;
	private Client clienteNull;
	private Client clienteMock1, clienteMock2;
	private Message msg;
	
	
	@Before
	public void setUp() {
		servicio = new SubscriptionService();
		clienteNull = null;
		clienteMock1 = mock(Client.class);
		clienteMock2 = mock(Client.class);
		msg = mock(Message.class);
	}
	
	
	/**
	 * No se puede añadir un cliente null a suscriber
	 * @throws NullClientException
	 * @throws ExistingClientException
	 */
	@Test(expected = NullClientException.class)
	public void verificandoClienteNulo() throws NullClientException, ExistingClientException {
		servicio.addSubscriber(clienteNull); 
	}
	
	/**
	 * Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 */
	@Test
	public void anadirClienteConComprobacion() throws NullClientException, ExistingClientException	{
		servicio.addSubscriber(clienteMock1);
		assertTrue(servicio.subscribers.contains(clienteMock1));
		
	}
	
	/**
	 * No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. 
	 * Al hacerlo, se lanza la excepción ExistingClientException.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 */
	@Test(expected = ExistingClientException.class)
	public void anadirClienteUnicaVez() throws NullClientException, ExistingClientException {
		servicio.addSubscriber(clienteMock1);
		servicio.addSubscriber(clienteMock1);
	}
	
	/**
	 * Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 */
	@Test
	public void anadirVariosClienteConComprobacion() throws NullClientException, ExistingClientException {
		servicio.addSubscriber(clienteMock1);
		servicio.addSubscriber(clienteMock2);
		assertTrue(servicio.subscribers.contains(clienteMock1) &&
				servicio.subscribers.contains(clienteMock2));
		
	}
	
	/**
	 * No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NullClientException.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test(expected = NullClientException.class)
	public void eliminarClienteNull() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.removeSubscriber(clienteNull);
		
	}
	
	/**
	 * No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NonExistingClientException.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test(expected = NonExistingClientException.class)
	public void eliminarClienteNoAlmacenado() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		servicio.removeSubscriber(clienteMock2);
		
	}
	
	/**
	 * Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test
	public void eliminarClienteAlmacenado() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		servicio.removeSubscriber(clienteMock1);
		assertTrue(servicio.subscribers.isEmpty());
	}
	
	/**
	 * No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NonExistingClientException.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test(expected = NonExistingClientException.class)
	public void eliminarDosVecesClienteAlmacenado() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		servicio.removeSubscriber(clienteMock1);
		servicio.removeSubscriber(clienteMock1);
		
	}
	
	
	/**
	 * Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test
	public void eliminarVariosClientesAlmacenado() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		servicio.addSubscriber(clienteMock2);
		servicio.removeSubscriber(clienteMock1);
		servicio.removeSubscriber(clienteMock2);
		assertTrue(servicio.subscribers.isEmpty());
		
	}
	
	
	/**
	 * Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test
	public void eliminarTodosClientesAlmacenado() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		servicio.addSubscriber(clienteMock2);
		Iterator<Client> it = servicio.subscribers.iterator();
		{
			servicio.removeSubscriber(it.next());
		}while(it.hasNext()) 
		
		assertTrue(servicio.subscribers.isEmpty());
		
	}
	
	/**
	 * Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test
	public void clienteSuscritoRecibeMensaje() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		
		when(clienteMock1.hasEmail()).thenReturn(true);
		
		servicio.sendMessage(msg);
		
		verify(clienteMock1, times(1)).receiveMessage(msg);
		
	}
	
	/**
	 * Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test
	public void clienteSuscritoNoRecibeMensaje() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		
		when(clienteMock1.hasEmail()).thenReturn(false);
		
		servicio.sendMessage(msg);
		
		verify(clienteMock1, times(0)).receiveMessage(msg);
		
	}
	
	/**
	 * Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test
	public void clientesSuscritosRecibenMensaje() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		servicio.addSubscriber(clienteMock2);
		
		when(clienteMock1.hasEmail()).thenReturn(true);
		when(clienteMock2.hasEmail()).thenReturn(true);
		
		servicio.sendMessage(msg);
		
		verify(clienteMock1, times(1)).receiveMessage(msg);
		verify(clienteMock1, times(1)).receiveMessage(msg);
		
	}
	
	/**
	 * Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
	 * @throws NullClientException
	 * @throws ExistingClientException
	 * @throws NonExistingClientException 
	 */
	@Test
	public void clientesSuscritosNoRecibenMensaje() throws NullClientException, ExistingClientException, NonExistingClientException {
		servicio.addSubscriber(clienteMock1);
		
		when(clienteMock1.hasEmail()).thenReturn(true);
		
		servicio.removeSubscriber(clienteMock1);
		servicio.sendMessage(msg);
		
		verify(clienteMock1, times(0)).receiveMessage(msg);
	
	}
}

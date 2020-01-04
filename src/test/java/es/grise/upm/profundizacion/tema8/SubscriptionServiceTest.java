package es.grise.upm.profundizacion.tema8;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceTest {
	
	//Clientes
	@Mock
	private Client cliente1;
	@Mock
	private Collection <Client> listaSus;
	@Mock
	private Message mensaje;
	//State tests
	@InjectMocks
	private SubscriptionService suscripcion;

	//No se puede añadir un Client null a la lista subscribers.
	@Test(expected = NullClientException.class)
 	public void noSePuedeAñadirClienteNull () throws NullClientException, ExistingClientException {
		suscripcion.addSubscriber(null);
	    verify(suscripcion, atLeastOnce()).addSubscriber(null);
 	}
	
	//Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	@Test
 	public void añadirClienteAListaDeSuscriptores () throws NullClientException, ExistingClientException {
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		assertEquals(listaSus, suscripcion.subscribers);
		verify(listaSus, atLeastOnce()).add(cliente1);

 		
 	}
	
	//No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.
	@Test(expected = ExistingClientException.class)
 	public void noSePuedeAñadirMismoCliente () throws NullClientException, ExistingClientException {
		when(suscripcion.subscribers.contains(cliente1)).thenReturn(true);
		suscripcion.addSubscriber(cliente1);
 		
 	}
	//Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	
	@Test
 	public void seAñadenVariosClientesAListaSuscriptores () throws NullClientException, ExistingClientException {
		
		listaSus.add(cliente1);
		listaSus.add(cliente1);
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		suscripcion.addSubscriber(cliente1);
		suscripcion.addSubscriber(cliente1);
		assertEquals(listaSus, suscripcion.subscribers);
		verify(listaSus, atLeastOnce()).add(cliente1);

		
 		
 	}
	//No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	@Test(expected = NullClientException.class)
 	public void noSePuedeEliminarClienteNull () throws  ExistingClientException, NonExistingClientException, NullClientException {
		suscripcion.removeSubscriber(null);
	    verify(suscripcion, atLeastOnce()).removeSubscriber(null);
 		
 	}
	//No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
 	public void noSePuedeEliminarClienteNoAlmacenado () throws NullClientException, ExistingClientException, NonExistingClientException {
		when(suscripcion.subscribers.contains(cliente1)).thenReturn(false);
		suscripcion.removeSubscriber(cliente1);
 		
	}
 		
	//Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	@Test
 	public void seEliminaClienteDeListaSuscriptores () throws NullClientException, ExistingClientException, NonExistingClientException {
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		when(suscripcion.subscribers.contains(cliente1)).thenReturn(true);
		listaSus.remove(cliente1);
		suscripcion.removeSubscriber(cliente1);
		assertEquals(listaSus, suscripcion.subscribers);
		verify(listaSus, atLeastOnce()).remove(cliente1);

 	}
	//No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	@Test(expected = NonExistingClientException.class)
 	public void noSePuedeEliminarDosVecesAlMismoCliente () throws NullClientException, ExistingClientException, NonExistingClientException {
		when(suscripcion.subscribers.contains(cliente1)).thenReturn(false);
		suscripcion.removeSubscriber(cliente1);
		suscripcion.removeSubscriber(cliente1);
	}
	//Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
	@Test
 	public void seEliminanVariosClientesDeListaSuscriptores () throws NullClientException, ExistingClientException, NonExistingClientException {
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		when(suscripcion.subscribers.contains(cliente1)).thenReturn(true);
		listaSus.remove(cliente1);
		suscripcion.removeSubscriber(cliente1);
		listaSus.remove(cliente1);
		suscripcion.removeSubscriber(cliente1);
		assertEquals(listaSus, suscripcion.subscribers);
		verify(listaSus, atLeast(2)).remove(cliente1);
 	}
	//Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
	@Test
 	public void seEliminanTodosLosClientesDeListaSuscriptores () throws NullClientException, ExistingClientException, NonExistingClientException {
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		listaSus.add(cliente1);
		suscripcion.addSubscriber(cliente1);
		when(suscripcion.subscribers.contains(cliente1)).thenReturn(true);
		listaSus.remove(cliente1);
		suscripcion.removeSubscriber(cliente1);
		listaSus.remove(cliente1);
		suscripcion.removeSubscriber(cliente1);
		listaSus.remove(cliente1);
		suscripcion.removeSubscriber(cliente1);
		assertEquals(listaSus, suscripcion.subscribers);
		verify(listaSus, atLeast(3)).remove(cliente1);
 	}
	
	
	//Interaction tests
	
	//Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
	@Test
 	public void clienteReciveMensaje () throws NullClientException, ExistingClientException {
		SubscriptionService suscripcion2 = new SubscriptionService();
		suscripcion2.addSubscriber(cliente1);
		when(cliente1.hasEmail()).thenReturn(true);
		suscripcion2.sendMessage(mensaje);
		cliente1.receiveMessage(mensaje);
		verify(cliente1, atLeast(1)).receiveMessage(mensaje);
			
		
 	}
	//Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
	@Test
 	public void clienteNoReviceMensaje () throws NullClientException, ExistingClientException {
		SubscriptionService suscripcion2 = new SubscriptionService();
		suscripcion2.addSubscriber(cliente1);
		when(cliente1.hasEmail()).thenReturn(false);
		suscripcion2.sendMessage(mensaje);
		verify(cliente1, atMost(0)).receiveMessage(mensaje);
 	}
	//Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
	@Test
 	public void variosSuscritosRecibenMensajes () throws NullClientException, ExistingClientException {
		SubscriptionService suscripcion2 = new SubscriptionService();
		when(suscripcion.subscribers.contains(cliente1)).thenReturn(false);
		suscripcion.addSubscriber(cliente1);
		suscripcion.addSubscriber(cliente1);
		suscripcion.addSubscriber(cliente1);
		suscripcion.addSubscriber(cliente1);
		when(cliente1.hasEmail()).thenReturn(true);
		suscripcion2.sendMessage(mensaje);
		suscripcion2.sendMessage(mensaje);
		suscripcion2.sendMessage(mensaje);
		suscripcion2.sendMessage(mensaje);
		cliente1.receiveMessage(mensaje);
		cliente1.receiveMessage(mensaje);
		cliente1.receiveMessage(mensaje);
		cliente1.receiveMessage(mensaje);
		verify(cliente1, atLeast(4)).receiveMessage(mensaje);
 	}
	//Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
	@Test
 	public void alQuitarseClienteNoRecibeMensaje () throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService suscripcion2 = new SubscriptionService();
		suscripcion2.addSubscriber(cliente1);
		suscripcion2.removeSubscriber(cliente1);
		when(cliente1.hasEmail()).thenReturn(false);
		suscripcion2.sendMessage(mensaje);
		verify(cliente1, atMost(0)).receiveMessage(mensaje);
		
 	}

	
}

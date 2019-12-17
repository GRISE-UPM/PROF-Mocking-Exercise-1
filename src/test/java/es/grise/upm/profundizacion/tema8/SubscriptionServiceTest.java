package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

public class SubscriptionServiceTest {
	
	SubscriptionService subscriptionService = new SubscriptionService();
	
	// State tests
	@Test(expected = NullClientException.class)
	public void noSePuedeAnadirClientNullASuscribers() throws NullClientException, ExistingClientException {
		Client CLIENTE_NULL = null;
		
		subscriptionService.addSubscriber(CLIENTE_NULL);
	}
	
	@Test
	public void alAnadirClientSeAlmacenaEnSubscribers() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		Collection <Client> LISTA_CON_CLIENTE = new ArrayList<Client>();
		LISTA_CON_CLIENTE.add(client);
		
		subscriptionService.addSubscriber(client);
		
		assertEquals("Correcto: se introduce el cliente", LISTA_CON_CLIENTE, subscriptionService.subscribers);
	}
	
	@Test(expected = ExistingClientException.class)
	public void noSePuedeRepetirClientAlAnadir() throws NullClientException, ExistingClientException {
		Client client = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		subscriptionService.addSubscriber(client);
	}
	
	@Test
	public void alAnadirVariosClientSeAlmacenanEnSubscribers() throws NullClientException, ExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Collection <Client> LISTA_CON_DOS_CLIENTES = new ArrayList<Client>();
		LISTA_CON_DOS_CLIENTES.add(client1);
		LISTA_CON_DOS_CLIENTES.add(client2);
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		
		assertEquals("Correcto: se introducen sendos clientes", LISTA_CON_DOS_CLIENTES, subscriptionService.subscribers);
	}
	
	@Test(expected = NullClientException.class)
	public void noSePuedeEliminarUnClientNullDeSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client CLIENTE_NULL = null;
		
		//Para poder probarlo, se introduce manualmente un cliente NULL
		subscriptionService.subscribers.add(CLIENTE_NULL);
		
		subscriptionService.removeSubscriber(CLIENTE_NULL);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void noSePuedeEliminarUnClientQueNoEsteEnSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		
		subscriptionService.removeSubscriber(client);
	}
	
	@Test
	public void sePuedeEliminarUnClientQueEsteEnSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		Collection <Client> LISTA_VACIA = new ArrayList<Client>();
		
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);
		
		assertEquals("Correcto: se ha quitado el cliente previamente introducido", LISTA_VACIA, subscriptionService.subscribers);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void noSePuedeEliminarUnClientDosVeces() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		
		subscriptionService.addSubscriber(client);
		subscriptionService.removeSubscriber(client);
		subscriptionService.removeSubscriber(client);
	}
	
	@Test
	public void sePuedenEliminarVariosClientsQueEstenEnSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		Collection <Client> LISTA_CON_SOLO_CLIENT_2 = new ArrayList<Client>();
		LISTA_CON_SOLO_CLIENT_2.add(client2);
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client3);
		
		assertEquals("Correcto: se han quitado el cliente 1 y 3, previamente introducidos", LISTA_CON_SOLO_CLIENT_2, subscriptionService.subscribers);
	}
	
	@Test
	public void sePuedenEliminarTodosLosClientsQueEstenEnSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		Collection <Client> LISTA_VACIA = new ArrayList<Client>();
		
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
		subscriptionService.removeSubscriber(client3);
		
		assertEquals("Correcto: se han quitado todos los clientes previamente introducidos", LISTA_VACIA, subscriptionService.subscribers);
	}
	
	
	// Interaction tests
	@Test
	public void unClientSuscritoRecibeMensajesSiTieneEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		when(client.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(client);
		
		Message message = mock(Message.class);
		subscriptionService.sendMessage(message);
		
		verify(client, times(1)).receiveMessage(message);
	}
	
	
	@Test
	public void unClientSuscritoNoRecibeMensajesSiNoTieneEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		when(client.hasEmail()).thenReturn(false);
		subscriptionService.addSubscriber(client);
		
		Message message = mock(Message.class);
		subscriptionService.sendMessage(message);
		
		verify(client, never()).receiveMessage(message);
	}
	
	@Test
	public void variosClientsSuscritosRecibenMensajesSiTienenEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		when(client1.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		
		Message message = mock(Message.class);
		subscriptionService.sendMessage(message);
		
		verify(client1, times(1)).receiveMessage(message);
		verify(client2, times(1)).receiveMessage(message);
	}
	
	@Test
	public void alDesuscribirClientYaNoRecibeMensajes() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client = mock(Client.class);
		when(client.hasEmail()).thenReturn(true);
		subscriptionService.addSubscriber(client);

		subscriptionService.removeSubscriber(client);
		Message message = mock(Message.class);
		subscriptionService.sendMessage(message);
		
		verify(client, never()).receiveMessage(message);
	}
}

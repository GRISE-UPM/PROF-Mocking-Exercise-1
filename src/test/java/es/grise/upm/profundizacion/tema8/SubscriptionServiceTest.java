package es.grise.upm.profundizacion.tema8;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Collection;

public class SubscriptionServiceTest {
	
	private SubscriptionService subs = new SubscriptionService();
	private Client client = mock(Client.class);
	
	//Prueba cliente nulo
	@Test(expected = NullClientException.class)
	public void notAbleToAddNullClient() throws NullClientException, ExistingClientException {
		subs.addSubscriber(null);
	}
	
	//Prueba añadir cliente bien
	@Test
	public void ableToAddClientToSubscriber() throws NullClientException, ExistingClientException {
		Collection <Client> aux_list = new ArrayList<Client>();
		aux_list.add(client);
		subs.addSubscriber(client);
		assertEquals("Ok", aux_list, subs.subscribers);
	}
	
	//Prueba añadir dos veces el mismo cliente
	@Test(expected = ExistingClientException.class)
	public void notAbleToSubTwoTimesSameClient() throws NullClientException, ExistingClientException {
		subs.addSubscriber(client);
		subs.addSubscriber(client);
	}
	
	//Prueba añadir varios clientes bien
	@Test
	public void addSomeClientsToSubscriber() throws NullClientException, ExistingClientException {
		Collection <Client> aux_list = new ArrayList<Client>();
		Client client1 = mock(Client.class);
		Client client2 = mock(Client.class);
		aux_list.add(client);
		aux_list.add(client1);
		aux_list.add(client2);
		subs.addSubscriber(client);
		subs.addSubscriber(client1);
		subs.addSubscriber(client2);
		assertEquals("Ok", aux_list, subs.subscribers);
	}
	
	//Prueba quitar cliente nulo
	@Test(expected = NullClientException.class)
	public void removeNullClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		subs.removeSubscriber(null);
	}
	
	//Prueba quitar cliente no suscrito
	@Test(expected = NonExistingClientException.class)
	public void removeClientWhoIsNotSubscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
		subs.removeSubscriber(client);
	}
	
	//Prueba quitar cliente suscrito bien
	@Test
	public void removeClientWhoIsSubscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
		Collection <Client> lista_aux = new ArrayList<Client>();
		subs.addSubscriber(client);
		subs.removeSubscriber(client);
		
		assertEquals("Ok", lista_aux, subs.subscribers);
	}
	
	//Prueba quitar cliente suscrito dos veces
	@Test(expected = NonExistingClientException.class)
	public void removeClientWhoIsSubscribedTwoTimes() throws NullClientException, ExistingClientException, NonExistingClientException {
		subs.addSubscriber(client);
		subs.removeSubscriber(client);
		subs.removeSubscriber(client);
	}
	
	//Prueba quitar varios cleintes bien
	@Test
	public void removeDiferentsClientsWhoAreSubscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		Collection <Client> aux_list = new ArrayList<Client>();
		aux_list.add(client1);
		subs.addSubscriber(client1);
		subs.addSubscriber(client);
		subs.removeSubscriber(client);
		assertEquals("Ok", aux_list, subs.subscribers);
	}
	
	//Prueba quitar todos los clientes bien
	@Test
	public void removeAllDiferentsClientsWhoAreSubscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		Collection <Client> aux_list = new ArrayList<Client>();
		subs.addSubscriber(client1);
		subs.addSubscriber(client);
		subs.removeSubscriber(client1);
		subs.removeSubscriber(client);
		assertEquals("Ok", aux_list, subs.subscribers);
	}
	
	
	//Prueba recibir mensaje bien
	@Test
	public void clientWithMailReceiveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
		when(client.hasEmail()).thenReturn(true);
		subs.addSubscriber(client);
		Message message = mock(Message.class);
		subs.sendMessage(message);		
		verify(client,atMost(1)).receiveMessage(message);
	}
	
	//Prueba mensaje a cliente sin e-mail
	@Test
	public void clientWithoutMailDoNotReceiveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
		when(client.hasEmail()).thenReturn(false);
		subs.addSubscriber(client);
		Message message = mock(Message.class);
		subs.sendMessage(message);		
		verify(client,atMost(0)).receiveMessage(message);
	}
	
	//Prueba mensaje a cliente con mail bien
	@Test
	public void someClientWithMailReceiveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
		Client client1 = mock(Client.class);
		when(client1.hasEmail()).thenReturn(true);
		subs.addSubscriber(client);
		subs.addSubscriber(client1);
		Message message = mock(Message.class);
		subs.sendMessage(message);
		verify(client, atMost(1)).receiveMessage(message);
		verify(client1, atMost(1)).receiveMessage(message);
	}
	
	//Prueba cliente desubscrito no recibe mensaje
	@Test
	public void clientWithMailDoNotReceiveMessageAfterDesubscribe() throws NullClientException, ExistingClientException, NonExistingClientException {
		when(client.hasEmail()).thenReturn(true);
		subs.addSubscriber(client);
		subs.removeSubscriber(client);
		Message message = mock(Message.class);
		subs.sendMessage(message);		
		verify(client,atMost(0)).receiveMessage(message);
	}
}
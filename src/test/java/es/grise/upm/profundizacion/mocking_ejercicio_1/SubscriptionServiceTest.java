package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceTest {

	@InjectMocks
	SubscriptionService sv;

	@Mock
	Client client;

	@Mock
	Message message;

	@Before
	public void setup(){
		sv = new SubscriptionService();
		//sv = mock(SubscriptionService.class);
		//client = mock(Client.class);
	}

	/*	STATE TESTS	*/
	@Test(expected = NullClientException.class)
	public void excepcionAniadirNullClient() throws NullClientException, ExistingClientException{
		sv.addSubscriber(null);
	}

	@Test
	public void aniadirClienteConAddSubcriber() throws NullClientException, ExistingClientException {
		client = mock(Client.class);
		sv.addSubscriber(client);
		assertEquals(1, sv.subscribers.size());
	}

	@Test(expected = ExistingClientException.class)
	public void aniadirClienteDuplicado() throws NullClientException, ExistingClientException {
		client = mock(Client.class);
		sv.addSubscriber(client);
		sv.addSubscriber(client);
	}

	@Test
	public void aniadirMultiplesClientesConAddSubcriber() throws NullClientException, ExistingClientException {
		client = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);

		sv.addSubscriber(client);
		sv.addSubscriber(client2);
		sv.addSubscriber(client3);

		if (sv.subscribers.contains(client) && sv.subscribers.contains(client2) && sv.subscribers.contains(client3))
			assertEquals(3, sv.subscribers.size());
	}

	@Test(expected = NullClientException.class)
	public void eliminarClientNullConRemoveSubscriber() throws NullClientException, ExistingClientException, NonExistingClientException {
		client = mock(Client.class);
		sv.addSubscriber(client);

		sv.removeSubscriber(null);
	}

	@Test(expected = NonExistingClientException.class)
	public void eliminarClienteQueNoExisteConRemoveSubscriber() throws NullClientException, NonExistingClientException {
		client = mock(Client.class);

		sv.removeSubscriber(client);
	}

	@Test
	public void eliminarClientConRemoveSubscriber() throws NullClientException, ExistingClientException, NonExistingClientException {
		client = mock(Client.class);
		sv.addSubscriber(client);

		sv.removeSubscriber(client);

		assertEquals(0, sv.subscribers.size());
	}

	@Test(expected = NonExistingClientException.class)
	public void eliminarClientDosVecesConRemoveSubscriber() throws NullClientException, ExistingClientException, NonExistingClientException {
		client = mock(Client.class);
		sv.addSubscriber(client);

		sv.removeSubscriber(client);
		sv.removeSubscriber(client);
	}

	@Test
	public void eliminarMultiplesClientesConRemoveSubcriber() throws NullClientException, ExistingClientException, NonExistingClientException {
		client = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);

		sv.addSubscriber(client);
		sv.addSubscriber(client2);
		sv.addSubscriber(client3);

		sv.removeSubscriber(client);
		sv.removeSubscriber(client2);

		assertEquals(1, sv.subscribers.size());
	}


	@Test
	public void eliminarTodosLosClientesConRemoveSubcriber() throws NullClientException, ExistingClientException, NonExistingClientException {
		client = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);

		sv.addSubscriber(client);
		sv.addSubscriber(client2);
		sv.addSubscriber(client3);

		sv.removeSubscriber(client);
		sv.removeSubscriber(client2);
		sv.removeSubscriber(client3);


		assertEquals(0, sv.subscribers.size());

	}




	/*	INTERACTION TESTS	*/
	@Test
	public void recibirMensajeCLienteConEmail() throws NullClientException, ExistingClientException {
		client = mock(Client.class);
		message = mock(Message.class);
		when(client.hasEmail()).thenReturn(true);

		sv.addSubscriber(client);
		sv.sendMessage(message);
		verify(client).receiveMessage(message);
	}

	@Test
	public void recibirMensajeCLienteSinEmail() throws NullClientException, ExistingClientException {
		client = mock(Client.class);
		message = mock(Message.class);
		when(client.hasEmail()).thenReturn(false);

		sv.addSubscriber(client);
		sv.sendMessage(message);


		//No se puede utlizar verifyZeroInteractions(); verifyNoMoreInteractions(); porque el hemos realizado el sendMessage y addSubscriber
		verify(client, times(0)).receiveMessage(message);
	}

	@Test
	public void recibirMensajeCariosCLienteConEmail() throws NullClientException, ExistingClientException {
		client = mock(Client.class);
		Client client2 = mock(Client.class);
		Client client3 = mock(Client.class);
		message = mock(Message.class);
		when(client.hasEmail()).thenReturn(true);
		when(client2.hasEmail()).thenReturn(true);
		when(client3.hasEmail()).thenReturn(true);

		sv.addSubscriber(client);
		sv.addSubscriber(client2);
		sv.addSubscriber(client3);
		sv.sendMessage(message);
		verify(client, times(1)).receiveMessage(message);
		verify(client2, times(1)).receiveMessage(message);
		verify(client3, times(1)).receiveMessage(message);
	}

	@Test
	public void recibirMensajeCLienteFueraListaSubscriber() throws NullClientException, ExistingClientException, NonExistingClientException {
		client = mock(Client.class);
		message = mock(Message.class);
		when(client.hasEmail()).thenReturn(false);

		sv.addSubscriber(client);
		sv.removeSubscriber(client);
		sv.sendMessage(message);


		//No se puede utlizar verifyZeroInteractions(); verifyNoMoreInteractions(); porque el hemos realizado el sendMessage y addSubscriber
		verify(client, times(0)).receiveMessage(message);
	}
}

package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SubscriptionServiceTest {


	SubscriptionService service;
	Client clientMocked;

	@BeforeEach
	public void setUp() {
		
		service = new SubscriptionService();
		clientMocked = mock(Client.class);
	
	}

	//State tests

	@Test
	public void aniadirClienteNulo() {

		assertThrows(NullClientException.class, ()->service.addSubscriber(null));
	}

	@Test
	public void almacenarEnListaSubscriber() throws NullClientException, ExistingClientException {

		service.addSubscriber(clientMocked);
		assertTrue(service.subscribers.contains(clientMocked));

	}

	@Test
	public void aniadirDosClientesIguales_ThrowsExistingClientException() throws NullClientException, ExistingClientException {


		assertThrows(ExistingClientException.class, ()->{service.addSubscriber(clientMocked);
		service.addSubscriber(clientMocked);
		});

	}

	@Test
	public void anadirVariosClientes() throws NullClientException, ExistingClientException {


		Client cm2 =   mock(Client.class);
		Client cm3 =   mock(Client.class);
		service.addSubscriber(clientMocked);
		service.addSubscriber(cm2);
		service.addSubscriber(cm3);
		assertEquals(3, service.subscribers.size());

	}

	@Test
	public void quitarClienteConParametroNull_ThrowsNullClienteException() {

		assertThrows(NullClientException.class, ()->service.removeSubscriber(null));
	}

	@Test
	public void quitarClienteQueNoEstaEnSubscribers_NonExistingClienteException() {

		assertThrows(NonExistingClientException.class, ()->service.removeSubscriber(clientMocked));
	}

	@Test
	public void quitarUnClienteDeSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {

		service.addSubscriber(clientMocked);
		service.removeSubscriber(clientMocked);
		assertFalse(service.subscribers.contains(clientMocked));
	}

	@Test
	public void quitarDosClientesIguales_ThrowsNonExistingClientException() throws NullClientException, ExistingClientException, NonExistingClientException {

		service.addSubscriber(clientMocked);
		service.removeSubscriber(clientMocked);
		assertThrows(NonExistingClientException.class,()->service.removeSubscriber(clientMocked));
	}

	@Test
	public void quitarVariosClientesDeSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {

		Client cm2 =   mock(Client.class);
		Client cm3 =   mock(Client.class);

		service.addSubscriber(clientMocked);
		service.addSubscriber(cm2);
		service.addSubscriber(cm3);

		service.removeSubscriber(cm2);
		service.removeSubscriber(cm3);

		assertFalse(service.subscribers.contains(cm2));
		assertFalse(service.subscribers.contains(cm3));
		assertTrue(service.subscribers.contains(clientMocked));
	}

	@Test
	public void quitarTodosClientesDeSubscribers() throws NullClientException, ExistingClientException, NonExistingClientException {

		Client cm2 =   mock(Client.class);
		Client cm3 =   mock(Client.class);

		service.addSubscriber(clientMocked);
		service.addSubscriber(cm2);
		service.addSubscriber(cm3);

		service.removeSubscriber(clientMocked);
		service.removeSubscriber(cm2);
		service.removeSubscriber(cm3);

		assertTrue(service.subscribers.size()==0);
	}

	//Interaction tests


	@Test
	public void subscriber_W_Email_recibeMensajes() throws NullClientException, ExistingClientException, NonExistingClientException {

		Message mm = mock(Message.class);

		service.subscribers.add(clientMocked);
		when(clientMocked.hasEmail()).thenReturn(true);

		service.sendMessage(mm);
		verify(clientMocked).receiveMessage(mm);
	}

	@Test
	public void Subscriber_WO_Email_NoRecibeMensajes() throws NullClientException, ExistingClientException, NonExistingClientException {

		Message mm = mock(Message.class);

		service.subscribers.add(clientMocked);
		when(clientMocked.hasEmail()).thenReturn(false);

		service.sendMessage(mm);
		verify(clientMocked,times(0)).receiveMessage(mm);
	}

	@Test
	public void VariosSubscriber_W_Email_recibeMensajes() throws NullClientException, ExistingClientException, NonExistingClientException {

		Message mm = mock(Message.class);
		Client cm2 =   mock(Client.class);
		Client cm3 =   mock(Client.class);

		service.subscribers.add(clientMocked);
		service.subscribers.add(cm2);
		service.subscribers.add(cm3);



		when(clientMocked.hasEmail()).thenReturn(true);
		when(cm2.hasEmail()).thenReturn(false);
		when(cm3.hasEmail()).thenReturn(true);

		service.sendMessage(mm);

		verify(clientMocked).receiveMessage(mm);
		verify(cm2,times(0)).receiveMessage(mm);
		verify(cm3).receiveMessage(mm);
	}

	@Test
	public void desuscrito_noRecibeMensajes() throws NullClientException, ExistingClientException, NonExistingClientException {
		
		Message mm = mock(Message.class);
		service.subscribers.add(clientMocked);
		service.removeSubscriber(clientMocked);
		
		when(clientMocked.hasEmail()).thenReturn(true);
		service.sendMessage(mm);
		verify(clientMocked,times(0)).receiveMessage(mm);
	}

}

package es.grise.upm.profundizacion.tema8;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SubscriptionServiceTest {
	private SubscriptionService service;

	@BeforeEach
	public void init() {
		service = new SubscriptionService();
		//service.subscribers = Mockito.mock(Collection.class);
	}

	@Test
	public void testClientNull() {
		assertThrows(NullClientException.class, () -> service.addSubscriber(null));
	}

	@Test
	public void testSize1AddSuscriber() throws NullClientException, ExistingClientException {
		Client cliente = Mockito.mock(Client.class);
		service.addSubscriber(cliente);
		assertEquals(1, service.subscribers.size());
	}

	@Test
	public void testClientDuplicado() throws NullClientException, ExistingClientException {
		Client cliente = Mockito.mock(Client.class);
		service.addSubscriber(cliente);
		assertThrows(ExistingClientException.class, () -> service.addSubscriber(cliente));
	}

	@Test
	public void testAddMultipleClients() throws NullClientException, ExistingClientException {
		Client cliente1 = Mockito.mock(Client.class);
		service.addSubscriber(cliente1);
		Client cliente2 = Mockito.mock(Client.class);
		service.addSubscriber(cliente2);
		Client cliente3 = Mockito.mock(Client.class);
		service.addSubscriber(cliente3);
		assertEquals(3, service.subscribers.size());
	}

	@Test
	public void testDeleteClientNull(){
		assertThrows(NullClientException.class, () -> service.removeSubscriber(null));
	}
	
	@Test
	public void testDeleteClientDoesntExist(){
		Client cliente = Mockito.mock(Client.class);
		assertThrows(NonExistingClientException.class, () -> service.removeSubscriber(cliente));
	}
	
	@Test
	public void testDeleteClientCorrectly() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client cliente = Mockito.mock(Client.class);
		service.addSubscriber(cliente);
		service.removeSubscriber(cliente);
		assertEquals(0, service.subscribers.size());
	}
	
	@Test
	public void testDeleteClientTwice() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client cliente = Mockito.mock(Client.class);
		service.addSubscriber(cliente);
		service.removeSubscriber(cliente);
		assertThrows(NonExistingClientException.class, () -> service.removeSubscriber(cliente));
	}
	
	@Test
	public void testDeleteSomeMultipleClients() throws NonExistingClientException, NullClientException, ExistingClientException {
		Client cliente1 = Mockito.mock(Client.class);
		service.addSubscriber(cliente1);
		Client cliente2 = Mockito.mock(Client.class);
		service.addSubscriber(cliente2);
		Client cliente3 = Mockito.mock(Client.class);
		service.addSubscriber(cliente3);
		
		service.removeSubscriber(cliente1);
		service.removeSubscriber(cliente2);
		
		assertEquals(1, service.subscribers.size());
	}
	
	@Test
	public void testDeleteAllMultipleClients() throws NonExistingClientException, NullClientException, ExistingClientException {
		Client cliente1 = Mockito.mock(Client.class);
		service.addSubscriber(cliente1);
		Client cliente2 = Mockito.mock(Client.class);
		service.addSubscriber(cliente2);
		Client cliente3 = Mockito.mock(Client.class);
		service.addSubscriber(cliente3);
		
		service.removeSubscriber(cliente1);
		service.removeSubscriber(cliente2);
		service.removeSubscriber(cliente3);
		
		assertEquals(0, service.subscribers.size());
	}
	
	@Test
	public void testReceibeMessageWithMail() throws NonExistingClientException, NullClientException, ExistingClientException {
		Client cliente1 = Mockito.mock(Client.class);
		service.addSubscriber(cliente1);
		
		Mockito.when(cliente1.hasEmail()).thenReturn(true);
		
		Message mensaje = Mockito.mock(Message.class);
		service.sendMessage(mensaje);
		Mockito.verify(cliente1, Mockito.times(1)).receiveMessage(mensaje);
	}
	
	@Test
	public void testReceibeMessageWithNoMail() throws NonExistingClientException, NullClientException, ExistingClientException {
		Client cliente1 = Mockito.mock(Client.class);
		service.addSubscriber(cliente1);
		
		Mockito.when(cliente1.hasEmail()).thenReturn(false);
		
		Message mensaje = Mockito.mock(Message.class);
		service.sendMessage(mensaje);
		Mockito.verify(cliente1, Mockito.times(0)).receiveMessage(mensaje);
	}
	
	@Test
	public void testReceibeMessageVariosWithMail() throws NonExistingClientException, NullClientException, ExistingClientException {
		Client cliente1 = Mockito.mock(Client.class);
		service.addSubscriber(cliente1);
		Client cliente2 = Mockito.mock(Client.class);
		service.addSubscriber(cliente2);
		Client cliente3 = Mockito.mock(Client.class);
		service.addSubscriber(cliente3);
		
		Mockito.when(cliente1.hasEmail()).thenReturn(true);
		Mockito.when(cliente2.hasEmail()).thenReturn(true);
		Mockito.when(cliente3.hasEmail()).thenReturn(true);
		
		Message mensaje = Mockito.mock(Message.class);
		service.sendMessage(mensaje);
		Mockito.verify(cliente1, Mockito.times(1)).receiveMessage(mensaje);
		Mockito.verify(cliente2, Mockito.times(1)).receiveMessage(mensaje);
		Mockito.verify(cliente3, Mockito.times(1)).receiveMessage(mensaje);
	}
	
	@Test
	public void testReceibeMessageWithMailDesuscribe() throws NonExistingClientException, NullClientException, ExistingClientException {
		Client cliente1 = Mockito.mock(Client.class);
		service.addSubscriber(cliente1);
		service.removeSubscriber(cliente1);
		Mockito.when(cliente1.hasEmail()).thenReturn(true);
		
		Message mensaje = Mockito.mock(Message.class);
		service.sendMessage(mensaje);
		Mockito.verify(cliente1, Mockito.times(0)).receiveMessage(mensaje);
	}
}

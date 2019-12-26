package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class SubscriptionServiceTest {
	
	Collection <Client> lista;
	
	SubscriptionService servicioS;
	Client clienteNulo= null;
	Client cliente1;
	Client cliente2;
	Client cliente3;
	Client cliente4;
	
	Message msg1;
	Message msg2;
	
	@Before
	public void setUp() {
		cliente1= mock(Client.class);
		cliente2= mock(Client.class);
		cliente3= mock(Client.class);
		cliente4= mock(Client.class);
		servicioS= new SubscriptionService();
		
		msg1=mock(Message.class);
		msg2=mock(Message.class);
	}
	
	
	/////////////////// State Tests/////////////////
	
	@Test(expected = NullClientException.class)
	public void addNullClientGivesError() throws NullClientException, ExistingClientException {
		servicioS.addSubscriber(clienteNulo);
	}
	
	@Test
	public void addClientToSuscriberListCorrect() throws NullClientException, ExistingClientException {
		assertFalse(servicioS.subscribers.contains(cliente1));
		servicioS.addSubscriber(cliente1);
		assertTrue(servicioS.subscribers.contains(cliente1));
	}
	
	@Test(expected = ExistingClientException.class)
	public void addRepeatedClientGivesException() throws NullClientException, ExistingClientException {
		servicioS.addSubscriber(cliente1);
		servicioS.addSubscriber(cliente1);
	}
	
	@Test
	public void addMultipleClientsCorrectly() throws NullClientException, ExistingClientException {
		lista = new ArrayList<Client>();
		servicioS.addSubscriber(cliente1);
		servicioS.addSubscriber(cliente2);
		servicioS.addSubscriber(cliente3);
		servicioS.addSubscriber(cliente4);
		
		lista.add(cliente1);
		lista.add(cliente2);
		lista.add(cliente3);
		lista.add(cliente4);
		
		assertEquals(lista, servicioS.subscribers);
		
	}
	
	@Test(expected = NullClientException.class)
	public void tryingToRemoveNullClientGivesException() throws NullClientException, NonExistingClientException {
		servicioS.removeSubscriber(clienteNulo);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void tryingToRemoveNonExixtingClientGivesException() throws NullClientException, NonExistingClientException {
		servicioS.removeSubscriber(cliente1);
	}
	
	@Test
	public void addAndRemoveSameClientCorrect() throws NullClientException, ExistingClientException, NonExistingClientException {
		//no contiene > añadimos >contiene > no contiene
		assertFalse(servicioS.subscribers.contains(cliente1));
		servicioS.addSubscriber(cliente1);
		assertTrue(servicioS.subscribers.contains(cliente1));
		servicioS.removeSubscriber(cliente1);
		assertFalse(servicioS.subscribers.contains(cliente1));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void removeTwiceSameClientThrowsException() throws NullClientException, ExistingClientException, NonExistingClientException {
		//no contiene > añadimos >contiene > no contiene
		servicioS.addSubscriber(cliente1);
		servicioS.removeSubscriber(cliente1);
		servicioS.removeSubscriber(cliente1);

	}
	
	@Test
	public void removeMultipleClientsCorrectly() throws NullClientException, ExistingClientException, NonExistingClientException {
		lista = new ArrayList<Client>();
		servicioS.addSubscriber(cliente1);
		servicioS.addSubscriber(cliente2);
		servicioS.addSubscriber(cliente3);
		servicioS.addSubscriber(cliente4);
		
		//no hacemos comprobacion de si se han añadido bien, eso pertenece a otro test
		servicioS.removeSubscriber(cliente1);
		servicioS.removeSubscriber(cliente2);

		lista.add(cliente3);
		lista.add(cliente4);
		//deberian estar solo el 3 y el 4
		assertEquals(lista, servicioS.subscribers);
		
	}
	
	@Test
	public void removeAllClientsCorrectly() throws NullClientException, ExistingClientException, NonExistingClientException {
		lista = new ArrayList<Client>();
		servicioS.addSubscriber(cliente1);
		servicioS.addSubscriber(cliente2);
		servicioS.addSubscriber(cliente3);
		servicioS.addSubscriber(cliente4);
		
		//no hacemos comprobacion de si se han añadido bien, eso pertenece a otro test
		servicioS.removeSubscriber(cliente1);
		servicioS.removeSubscriber(cliente2);
		servicioS.removeSubscriber(cliente3);
		servicioS.removeSubscriber(cliente4);
		
		//deberia estar vacia
		assertEquals(lista, servicioS.subscribers);
		
	}
	
	
	//////////////////interaction tests///////////////////////////
	
	@Test
	public void clientReceivesMessageWhenSubscriptIfHasEmail() throws NullClientException, ExistingClientException {
		servicioS.addSubscriber(cliente1);
		when(cliente1.hasEmail()).thenReturn(true);
		servicioS.sendMessage(msg1);
		verify(cliente1,times(1)).receiveMessage(msg1);
	}
	

	@Test
	public void clientNotReceivesMessageWhenSubscriptIfNotHasEmail() throws NullClientException, ExistingClientException {
		servicioS.addSubscriber(cliente1);
		when(cliente1.hasEmail()).thenReturn(false);
		servicioS.sendMessage(msg1);
		verify(cliente1,never()).receiveMessage(msg1);
	}
	
	@Test
	public void multipleClientReceiveMessageWhenSubscriptIfHaveEmail() throws NullClientException, ExistingClientException {
		servicioS.addSubscriber(cliente1);
		servicioS.addSubscriber(cliente2);
		servicioS.addSubscriber(cliente3);
		servicioS.addSubscriber(cliente4);
		when(cliente1.hasEmail()).thenReturn(true);
		when(cliente2.hasEmail()).thenReturn(true);
		when(cliente3.hasEmail()).thenReturn(true);
		when(cliente4.hasEmail()).thenReturn(true);
		servicioS.sendMessage(msg1);
		verify(cliente1,times(1)).receiveMessage(msg1);
		verify(cliente2,times(1)).receiveMessage(msg1);
		verify(cliente3,times(1)).receiveMessage(msg1);
		verify(cliente4,times(1)).receiveMessage(msg1);
	}
	
	@Test
	public void clientNotRecevesMsgWhenDesSubscript() throws NullClientException, ExistingClientException, NonExistingClientException {
		//recibe el priero porque esta suscrito, el segundo no, se desubscribe
		servicioS.addSubscriber(cliente1);
		when(cliente1.hasEmail()).thenReturn(true);
		servicioS.sendMessage(msg1);
		verify(cliente1,times(1)).receiveMessage(msg1);
		
		servicioS.removeSubscriber(cliente1);
		servicioS.sendMessage(msg2);
		verify(cliente1,never()).receiveMessage(msg2);

	}
	
}

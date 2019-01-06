package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SubscriptionServiceTest {
 	private SubscriptionService subscription;
 	private Client cliente1;
 	private Client cliente2;
 	private Client cliente3;
 	private Message mens1;
 	private Message mens2;
 	 
 	@Before
 	public void setUp(){
 		subscription = new SubscriptionService();
 		cliente1 = Mockito.mock(Client.class);
		cliente2 = Mockito.mock(Client.class);
		cliente3 = Mockito.mock(Client.class);
		mens1 = Mockito.mock(Message.class);
		mens2 = Mockito.mock(Message.class);
 	}
 	
 	@Test (expected=NullClientException.class)
 	public void ClientNullSubscription() throws NullClientException, ExistingClientException {
 		subscription.addSubscriber(null);
 	}
 	
 	@Test
 	public void ClientSubscription() throws NullClientException, ExistingClientException {
 		subscription.addSubscriber(cliente1);
 	}
 	
 	@Test(expected=ExistingClientException.class)
 	public void SameClientSubscription() throws NullClientException, ExistingClientException {
 		subscription.addSubscriber(cliente1);
 		subscription.addSubscriber(cliente1);
 	}
 	
 	@Test
 	public void SaveDiferentClientSubscription() throws NullClientException, ExistingClientException {
 		subscription.addSubscriber(cliente1);
 		subscription.addSubscriber(cliente2);
 		assertTrue(subscription.subscribers.contains(cliente1));
 		assertTrue(subscription.subscribers.contains(cliente2));
 	}
 	
 	@Test (expected=NullClientException.class)
 	public void RemoveClientNullSubscription() throws NullClientException, NonExistingClientException {
 		subscription.removeSubscriber(null);
 	}
 	
 	@Test (expected=NonExistingClientException.class)
 	public void RemoveNonExistingClient() throws NullClientException, NonExistingClientException {
 		subscription.removeSubscriber(cliente1);
 	}

 	@Test
 	public void RemoveExistingClient() throws NullClientException, NonExistingClientException, ExistingClientException {
 		subscription.addSubscriber(cliente1);
 		subscription.removeSubscriber(cliente1);
 		assertTrue(subscription.subscribers.isEmpty());
 	}
 	
 	@Test (expected=NonExistingClientException.class)
 	public void RemoveExistingClientTwice() throws NullClientException, NonExistingClientException, ExistingClientException {
 		subscription.addSubscriber(cliente1);
 		subscription.removeSubscriber(cliente1);
 		subscription.removeSubscriber(cliente1);
 	}
 	
 	@Test
 	public void AddRemoveExistingClients() throws NullClientException, NonExistingClientException, ExistingClientException {
 		subscription.addSubscriber(cliente1);
 		subscription.addSubscriber(cliente2);
 		subscription.addSubscriber(cliente3);
 		subscription.removeSubscriber(cliente1);
 		subscription.removeSubscriber(cliente2);
 		assertTrue(subscription.subscribers.size() == 1);
 	}
 	
 	@Test
 	public void AddRemoveAllExistingClients() throws NullClientException, NonExistingClientException, ExistingClientException {
 		subscription.addSubscriber(cliente1);
 		subscription.addSubscriber(cliente2);
 		subscription.removeSubscriber(cliente1);
 		subscription.removeSubscriber(cliente2);
 		assertTrue(subscription.subscribers.isEmpty());
 	}
 	
 	@Test
 	public void SendMessageWithEmail() throws NullClientException, ExistingClientException {
 		Mockito.when(cliente1.hasEmail()).thenReturn(true);
 		subscription.addSubscriber(cliente1);
 		subscription.sendMessage(mens1);
 		Mockito.verify(cliente1).receiveMessage(mens1); 
 	}
 	
 	@Test
 	public void NoSendMessageWithOutEmail() throws NullClientException, ExistingClientException {
 		Mockito.when(cliente1.hasEmail()).thenReturn(false);
 		subscription.addSubscriber(cliente1);
 		subscription.sendMessage(mens1);
 		Mockito.verify(cliente1, Mockito.never()).receiveMessage(mens1); 
 	}
 	
 	@Test
 	public void SendMessageToSubscritionWithEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
 		// parte 1, Varios  Client suscritos reciben mensajes
 		Mockito.when(cliente1.hasEmail()).thenReturn(true);
 		Mockito.when(cliente2.hasEmail()).thenReturn(true);
 		subscription.addSubscriber(cliente1);
 		subscription.addSubscriber(cliente2);
 		subscription.sendMessage(mens1);
 		Mockito.verify(cliente1).receiveMessage(mens1); 
 		Mockito.verify(cliente2).receiveMessage(mens1);
 		
 		// parte 2, Al des-suscribir un Client, éste no recibe mensajes
 		subscription.removeSubscriber(cliente2);
 		subscription.sendMessage(mens2);
 		Mockito.verify(cliente2, Mockito.never()).receiveMessage(mens2); 
 	}
}


/*
     State tests
        //No se puede añadir un Client null a la lista subscribers.
        //Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
        //No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.
        //Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
        //No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
        //No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
        //Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
        //No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
        //Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
        //Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
    Interaction tests
        //Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
        //Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
        //Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
        //Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
 */
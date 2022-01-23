package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.*;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@RunWith(MockitoJUnitRunner.class)

public class SubscriptionServiceTest {
	
	//STATE TESTS//
	
	@InjectMocks
	SubscriptionService subscriptionService;

	@Mock
	Client client;

	@Mock
	Message message;
	
	//No se puede añadir un Client null a la lista subscribers
	@Test
	@DisplayName("No se puede añadir un Client null a la lista subscribers")
    public void addClientNull() throws NullClientException, ExistingClientException {
		subscriptionService = new SubscriptionService();
		assertThrows(NullClientException.class, () -> subscriptionService.addSubscriber(null));
    }
	
	//Al añadir un Client mediante addSubscriber(), este Client se almacena en la lista subscribers.
	@Test
	@DisplayName("Al añadir un Client mediante addSubscriber(), este Client se almacena en la lista subscribers")
	public void addClientInSubscribers() throws NullClientException, ExistingClientException {
		subscriptionService = new SubscriptionService();
	    client = mock(Client.class);
	    subscriptionService.addSubscriber(client);
	    assertTrue(subscriptionService.subscribers.contains(client));
	}
	
	//No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.
	 @Test
	 @DisplayName("No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers")
	 public void addSameClientTwice() throws NullClientException, ExistingClientException {
		 subscriptionService = new SubscriptionService();
	     client = mock(Client.class);
	     subscriptionService.addSubscriber(client);
	     assertThrows(ExistingClientException.class, () -> subscriptionService.addSubscriber(client));
	 }

	//Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.
	 @Test
	 @DisplayName("Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers")
	 public void addMultipleClient() throws NullClientException, ExistingClientException {
		 subscriptionService = new SubscriptionService();
	     Client client1 = mock(Client.class);
	     Client client2 = mock(Client.class);
	     Client client3 = mock(Client.class);
	     subscriptionService.addSubscriber(client1);
	     subscriptionService.addSubscriber(client2);
	     subscriptionService.addSubscriber(client3);
	     
	     assertTrue(subscriptionService.subscribers.contains(client1) 
	    		 && subscriptionService.subscribers.contains(client2)
	    		 &&subscriptionService.subscribers.contains(client3)
	    		 );
	 }
	 
	//No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	 @Test
	 @DisplayName("No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers")
	 public void removeClientNull() throws NullClientException, NonExistingClientException {
		 subscriptionService = new SubscriptionService();
		 assertThrows(NullClientException.class, () -> subscriptionService.removeSubscriber(null));
	 }
	 
	//No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	 @Test
	 @DisplayName("No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers")
	 public void removeClientNotSubscribed() throws NullClientException, NonExistingClientException {
		subscriptionService = new SubscriptionService();
	    client = mock(Client.class);
	    assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client));
	 }
	 
	//Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
	 @Test
	 @DisplayName("Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers")
	 public void removeClientSuscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService = new SubscriptionService();
	    client = mock(Client.class);
	    subscriptionService.addSubscriber(client);
	    subscriptionService.removeSubscriber(client);
	    assertEquals(0, subscriptionService.subscribers.size());
	 }
	 
	//No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	 @Test
	 @DisplayName("No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers")
	 public void removeClientTwice() throws NullClientException, ExistingClientException, NonExistingClientException {
		 subscriptionService = new SubscriptionService();
		 client = mock(Client.class);
		 subscriptionService.addSubscriber(client);
		 subscriptionService.removeSubscriber(client);
		 assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client));
	 }
	 
	//Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.
	 @Test
	 @DisplayName("Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers")
	 public void removeMultipleClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		subscriptionService = new SubscriptionService();
	 	Client client1 = mock(Client.class);
	 	Client client2 = mock(Client.class);
	    Client client3 = mock(Client.class);
		subscriptionService.addSubscriber(client1);
		subscriptionService.addSubscriber(client2);
		subscriptionService.addSubscriber(client3);
		subscriptionService.removeSubscriber(client1);
		subscriptionService.removeSubscriber(client2);
		assertEquals(1, subscriptionService.subscribers.size());
	 }
	 
	//Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.
	 @Test
	 @DisplayName("Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers")
	 public void removeAllClient() throws NullClientException, ExistingClientException, NonExistingClientException {
		 subscriptionService = new SubscriptionService();
		 Client client1 = mock(Client.class);
		 Client client2 = mock(Client.class);
		 Client client3 = mock(Client.class);
		    
		 subscriptionService.addSubscriber(client1);
		 subscriptionService.addSubscriber(client2);
		 subscriptionService.addSubscriber(client3);
		 assertEquals(3, subscriptionService.subscribers.size());
		
		 subscriptionService.removeSubscriber(client1);
		 subscriptionService.removeSubscriber(client2);
		 subscriptionService.removeSubscriber(client3);
		 assertEquals(0, subscriptionService.subscribers.size());
	 }
	 
	 //Interaction tests//

	 //Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).
	  @Test
	  @DisplayName("Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true)")
	  public void receiveMessageWithEmail() throws NullClientException, ExistingClientException {
		  subscriptionService = new SubscriptionService();
	      client = mock(Client.class);
	      message = mock(Message.class);
	      
	      when(client.hasEmail()).thenReturn(true);
	      //Mockito.when(client.hasEmail()).thenReturn(true);
	      
	      subscriptionService.addSubscriber(client);
	      subscriptionService.sendMessage(message);

	      verify(client, times(1)).receiveMessage(message);
	  }

	 //Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).
	  @Test
	  @DisplayName("Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false)")
	  public void receiveMessageNoEmail() throws NullClientException, ExistingClientException {
		  subscriptionService = new SubscriptionService();
		  client = mock(Client.class);
	      message = mock(Message.class);
	      
		  when(client.hasEmail()).thenReturn(false);
		  
		  subscriptionService.addSubscriber(client);
		  subscriptionService.sendMessage(message);
		  
		  verify(client,times(0)).receiveMessage(any());
	  }
	  
	 //Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).
	  @Test
	  @DisplayName("Varios Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true)")
	  public void receiveMessageMultipleClientWithEmail() throws NullClientException, ExistingClientException {
		  subscriptionService = new SubscriptionService();
		  Client client1 = mock(Client.class);
		  Client client2 = mock(Client.class);
		  Client client3 = mock(Client.class);
		  message = mock(Message.class);
		  
		  when(client1.hasEmail()).thenReturn(true);
		  when(client2.hasEmail()).thenReturn(true);
		  when(client3.hasEmail()).thenReturn(true);

		  subscriptionService.addSubscriber(client1);
		  subscriptionService.addSubscriber(client2);
		  subscriptionService.addSubscriber(client3);
		  
		  subscriptionService.sendMessage(message);
		  
		  verify(client1, times(1)).receiveMessage(message);
		  verify(client2, times(1)).receiveMessage(message);
		  verify(client3, times(1)).receiveMessage(message);
	  }

	 //Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
	  @Test
	  @DisplayName("Al des-suscribir un Client éste no recibe mensajes (método receiveMessage())")
	  public void receiveMessageNotSubscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
		  subscriptionService = new SubscriptionService();
		  client = mock(Client.class);
		  message = mock(Message.class);

		  subscriptionService.addSubscriber(client);
		  subscriptionService.removeSubscriber(client);
		  
		  subscriptionService.sendMessage(message);

		  verify(client, times(0)).receiveMessage(message);
	  }
}

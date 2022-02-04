package es.grise.upm.profundizacion.mocking_ejercicio_1;

//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;



public class SubscriptionServiceTest {
	
	
	SubscriptionService servicio;
	
	
	
	@Test(expected = NullClientException.class)
	//@DisplayName("No se puede añadir un Client null a la lista subscribers")
    public void addSubscribernulltest() throws NullClientException, ExistingClientException{
       servicio.addSubscriber(null);
    }
	
	@Test
    public void addSubscribertest() throws NullClientException, ExistingClientException{
		Client cliente = mock(Client.class);
       servicio.addSubscriber(cliente);
       assertTrue(servicio.subscribers.contains(cliente));
    }
	
	@Test(expected = ExistingClientException.class)
    public void addsameSubscribertwicetest() throws NullClientException, ExistingClientException{
		Client cliente = mock(Client.class);
        servicio.addSubscriber(cliente);
        servicio.addSubscriber(cliente);
       
    }
	
	@Test
    public void addSeveralSubscriberstest() throws NullClientException, ExistingClientException{
		Client cliente = mock(Client.class);
		Client cliente2 = mock(Client.class);
        servicio.addSubscriber(cliente);
        servicio.addSubscriber(cliente2);
        assertTrue(servicio.subscribers.contains(cliente));
        assertTrue(servicio.subscribers.contains(cliente2));
    }
	
	@Test(expected = NullClientException.class)
    public void removeSubscribernulltest() throws NullClientException, ExistingClientException, NonExistingClientException{
		
       servicio.removeSubscriber(null);
       
    }

	@Test(expected = NonExistingClientException.class)
    public void removeSubscribernotlisttest() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client cliente = mock(Client.class);
        servicio.removeSubscriber(cliente);
       
    }
	
	@Test
    public void removeSubscribertest() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client cliente = mock(Client.class);
		servicio.addSubscriber(cliente);
        servicio.removeSubscriber(cliente);
        assertFalse(servicio.subscribers.contains(cliente));
       
    }
	
	@Test(expected = NonExistingClientException.class)
    public void removeSubscribertwicetest() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client cliente = mock(Client.class);
		servicio.addSubscriber(cliente);
        servicio.removeSubscriber(cliente);
        servicio.removeSubscriber(cliente);
        
    }
	
	@Test
    public void removeSeveralSubscriberstest() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client cliente = mock(Client.class);
		Client cliente2 = mock(Client.class);
		Client cliente3 = mock(Client.class);
		servicio.addSubscriber(cliente);
		servicio.addSubscriber(cliente2);
		servicio.addSubscriber(cliente3);
        servicio.removeSubscriber(cliente);
        servicio.removeSubscriber(cliente2);
        assertFalse(servicio.subscribers.contains(cliente));
        assertFalse(servicio.subscribers.contains(cliente2));
        
    }
	
	@Test
    public void removeAllSubscriberstest() throws NullClientException, ExistingClientException, NonExistingClientException{
		Client cliente = mock(Client.class);
		Client cliente2 = mock(Client.class);
	
		servicio.addSubscriber(cliente);
		servicio.addSubscriber(cliente2);
        servicio.removeSubscriber(cliente);
        servicio.removeSubscriber(cliente2);
        assertTrue(servicio.subscribers.isEmpty());
        
        
    }
	
	@Test
    public void receiveMessagetest() throws NullClientException, ExistingClientException, NonExistingClientException{
		
		Client cliente = mock(Client.class);
	    Message mensaje = mock(Message.class);
	    
	    when(cliente.hasEmail()).thenReturn(true);
		servicio.addSubscriber(cliente);
		servicio.sendMessage(mensaje);
		verify(cliente).receiveMessage(mensaje);
        
        
    }
	
	@Test
    public void noreceiveMessagetest() throws NullClientException, ExistingClientException, NonExistingClientException{
		
		Client cliente = mock(Client.class);
	    Message mensaje = mock(Message.class);
	    
	    when(cliente.hasEmail()).thenReturn(false);
		servicio.addSubscriber(cliente);
		servicio.sendMessage(mensaje);
		verify(cliente).receiveMessage(mensaje);
        
        
    }
	
	@Test
    public void receiveMessageSeveralClientstest() throws NullClientException, ExistingClientException, NonExistingClientException{
		
		Client cliente = mock(Client.class);
		Client cliente1 = mock(Client.class);
		Client cliente2 = mock(Client.class);
	    Message mensaje = mock(Message.class);
	    
	    when(cliente.hasEmail()).thenReturn(true);
	    when(cliente1.hasEmail()).thenReturn(true);
	    when(cliente2.hasEmail()).thenReturn(true);
		servicio.addSubscriber(cliente);
		servicio.addSubscriber(cliente1);
		servicio.addSubscriber(cliente2);
		servicio.sendMessage(mensaje);
		
		verify(cliente).receiveMessage(mensaje);
		verify(cliente1).receiveMessage(mensaje);
		verify(cliente2).receiveMessage(mensaje);
    }
	
	@Test
    public void receiveMessageClientRemovedtest() throws NullClientException, ExistingClientException, NonExistingClientException{
		
		Client cliente = mock(Client.class);
	    Message mensaje = mock(Message.class);
	    
	    //when(cliente.hasEmail()).thenReturn(true);
		servicio.addSubscriber(cliente);
		servicio.removeSubscriber(cliente);
		servicio.sendMessage(mensaje);
		verify(cliente).receiveMessage(mensaje);
        
        
    }
	
}

package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.*;

public class SubscriptionServiceTest {

    // STATE TESTS

    @Test
    // No se puede añadir un Client null a la lista subscribers
    public void testAddSubscriberNull() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        assertThrows(NullClientException.class, () -> subscriptionService.addSubscriber(null));
    }

    @Test
    // Al añadir un Clientmediante addSubscriber(), este Client se almacena en la lista subscribers
    public void testAddSubscriber() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client);
        assertTrue(subscriptionService.subscribers.contains(client));
    }

    @Test
    // No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers
    public void testAddSubscriberExisting() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client);
        assertThrows(ExistingClientException.class, () -> subscriptionService.addSubscriber(client));
    }

    @Test
    // Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers
    public void testAddSubscribers() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        assertTrue(subscriptionService.subscribers.contains(client1));
        assertTrue(subscriptionService.subscribers.contains(client2));
    }

    @Test
    // No se puede eliminar usando removeSubscriber() un Client null de la lista subscribers
    public void testRemoveSubscriberNull() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        assertThrows(NullClientException.class, () -> subscriptionService.removeSubscriber(null));
    }

    @Test
    // No se puede eliminar usando removeSubscriber() un Client que no está almacenado en la lista subscribers
    public void testRemoveSubscriberNonExisting() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client = Mockito.mock(Client.class);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client));
    }

    @Test
    // Se puede eliminar correctamente usando removeSubscriber() un Client almacenado en la lista subscribers
    public void testRemoveSubscriber() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        assertTrue(!subscriptionService.subscribers.contains(client));
    }
	
    @Test
    // No se puede eliminar usando removeSubscriber() dos veces el mismo Client de la lista subscribers
    public void testRemoveSubscriberTwice() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client));
    }

    @Test
    // Se pueden eliminar correctamente usando removeSubscriber() varios Client almacenados en la lista subscribers
    public void testRemoveSubscribers() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Client client3 = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        assertTrue(!subscriptionService.subscribers.contains(client1));
        assertTrue(!subscriptionService.subscribers.contains(client2));
    }

    @Test
    // Se pueden eliminar correctamente usando removeSubscriber() todos los Client almacenados en la lista subscribers
    public void testRemoveAllSubscribers() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        assertTrue(subscriptionService.subscribers.isEmpty());
    }

    // INTERACTION TESTS

    @Test
    // Un Client suscrito recibe mensajes si tiene email
    public void testClientReceivesMessages() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client = Mockito.mock(Client.class);
        Message mensaje = Mockito.mock(Message.class);
        subscriptionService.addSubscriber(client);
        when(client.hasEmail()).thenReturn(true);
        if(client.hasEmail())
            subscriptionService.sendMessage(mensaje);
        Mockito.verify(client, Mockito.times(1)).receiveMessage(mensaje);
    }

    @Test
    // Un Client suscrito no recibe mensajes si no tiene email
    public void testClientDoesNotReceiveMessages() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client = Mockito.mock(Client.class);
        Message mensaje = Mockito.mock(Message.class);
        subscriptionService.addSubscriber(client);
        when(client.hasEmail()).thenReturn(false);
        if(!client.hasEmail())
            subscriptionService.sendMessage(mensaje);
        Mockito.verify(client, Mockito.times(0)).receiveMessage(mensaje);
    }

    @Test
    // Varios  Client suscritos reciben mensajes si tienen email
    public void testClientsReceiveMessages() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Message mensaje = Mockito.mock(Message.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        when(client1.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(true);
        if(client1.hasEmail() & client2.hasEmail())
            subscriptionService.sendMessage(mensaje);
        Mockito.verify(client1, Mockito.times(1)).receiveMessage(mensaje);
        Mockito.verify(client2, Mockito.times(1)).receiveMessage(mensaje);
    }

    @Test
    // Al des-suscribir un Client éste no recibe mensajes 
    public void testClientDoesNotReceiveMessagesAfterUnsubscribe() throws Exception {
        SubscriptionService subscriptionService = new SubscriptionService();
        Client client = Mockito.mock(Client.class);
        Message mensaje = Mockito.mock(Message.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        when(client.hasEmail()).thenReturn(true);
        if(client.hasEmail())
            subscriptionService.sendMessage(mensaje);
        Mockito.verify(client, Mockito.times(0)).receiveMessage(mensaje);
    }
}

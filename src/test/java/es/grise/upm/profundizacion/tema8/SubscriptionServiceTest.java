package es.grise.upm.profundizacion.tema8;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubscriptionServiceTest {
    private SubscriptionService subscriptionService;

    @Before
    public void setUp() {
        subscriptionService = new SubscriptionService();
    }

    // No se puede añadir un Client null a la lista subscribers
    @Test(expected = NullClientException.class)
    public void notAbleToAddNullClient() throws NullClientException, ExistingClientException {

        Client client = null;

        subscriptionService.addSubscriber(client);
    }

    // Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers
    @Test
    public void clientSaveInSuscribersList_OK() throws NullClientException, ExistingClientException {

        Client client = mock(Client.class);

        subscriptionService.addSubscriber(client);

        assertEquals(1, subscriptionService.subscribers.size());
        assertTrue(subscriptionService.subscribers.contains(client));
    }

    // No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers
    @Test(expected = ExistingClientException.class)
    public void notAbleToAddClientTwice() throws NullClientException, ExistingClientException {

        Client client = mock(Client.class);

        subscriptionService.addSubscriber(client);
        subscriptionService.addSubscriber(client);
    }

    // Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers
    @Test
    public void multipleClientsSaveInSuscribersList_OK() throws NullClientException, ExistingClientException {

        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);

        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);

        assertTrue(subscriptionService.subscribers.contains(client1));
        assertTrue(subscriptionService.subscribers.contains(client2));
        assertTrue(subscriptionService.subscribers.contains(client3));
        assertEquals(3, subscriptionService.subscribers.size());
    }

    // No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers
    @Test(expected = NullClientException.class)
    public void notAbleToRemoveNullClient() throws NullClientException, NonExistingClientException {

        Client client = null;

        subscriptionService.removeSubscriber(client);
    }

    // No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers
    @Test(expected = NonExistingClientException.class)
    public void notAbleToRemoveNoSuscribedClient() throws NullClientException, NonExistingClientException {

        Client client = mock(Client.class);

        subscriptionService.removeSubscriber(client);
    }

    // Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers
    @Test
    public void ableToRemoveExistingClient_OK() throws NullClientException, NonExistingClientException, ExistingClientException {

        Client client = mock(Client.class);

        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);

        assertFalse(subscriptionService.subscribers.contains(client));
        assertEquals(0, subscriptionService.subscribers.size());
    }

    // No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers
    @Test(expected = NonExistingClientException.class)
    public void notAbleToRemoveClientTwice() throws NullClientException, NonExistingClientException, ExistingClientException {

        Client client = mock(Client.class);

        subscriptionService.addSubscriber(client);

        subscriptionService.removeSubscriber(client);
        subscriptionService.removeSubscriber(client);
    }

    // Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers
    @Test
    public void ableToRemoveSeveralClients_OK() throws NullClientException, NonExistingClientException, ExistingClientException {

        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);

        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);

        assertFalse(subscriptionService.subscribers.contains(client1));
        assertFalse(subscriptionService.subscribers.contains(client2));
        assertTrue(subscriptionService.subscribers.contains(client3));
        assertEquals(1, subscriptionService.subscribers.size());
    }

    // Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers
    @Test
    public void shouldRemoveAllClientsCorrectly() throws NullClientException, NonExistingClientException, ExistingClientException {

        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);

        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        subscriptionService.removeSubscriber(client3);

        assertFalse(subscriptionService.subscribers.contains(client1));
        assertFalse(subscriptionService.subscribers.contains(client2));
        assertFalse(subscriptionService.subscribers.contains(client3));
        assertEquals(0, subscriptionService.subscribers.size());
    }

    // Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true)
    @Test
    public void subscribedClientWithEmailReceivesMessages() throws NullClientException, ExistingClientException {

        Client client = mock(Client.class);
        Message message = mock(Message.class);

        when(client.hasEmail()).thenReturn(true);

        subscriptionService.addSubscriber(client);
        subscriptionService.sendMessage(message);

        verify(client).hasEmail();
        verify(client).receiveMessage(message);
    }

    // Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false)
    @Test
    public void subscribedClientWithoutEmailDoesNotReceiveMessages() throws NullClientException, ExistingClientException {

        Client client = mock(Client.class);
        Message message = mock(Message.class);

        subscriptionService.addSubscriber(client);
        subscriptionService.sendMessage(message);

        verify(client).hasEmail();
        verify(client, never()).receiveMessage(message);
    }

    // Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true)
    @Test
    public void allSubscribedClientsWithEmailDoReceiveMessages() throws NullClientException, ExistingClientException {

        Client client1  = mock(Client.class);
        Client client2  = mock(Client.class);
        Client client3  = mock(Client.class);
        Message message = mock(Message.class);

        when(client1.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(true);
        when(client3.hasEmail()).thenReturn(false);

        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.sendMessage(message);

        verify(client1).hasEmail();
        verify(client2).hasEmail();
        verify(client3).hasEmail();
        verify(client1).receiveMessage(message);
        verify(client2).receiveMessage(message);
        verify(client3, never()).receiveMessage(message);
    }

    // Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage())
    @Test
    public void unsubscribedClientWithEmailShouldNotReceiveMessages() throws NullClientException, NonExistingClientException, ExistingClientException {

        Client client   = mock(Client.class);
        Message message = mock(Message.class);

        when(client.hasEmail()).thenReturn(true);

        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        subscriptionService.sendMessage(message);

        verify(client, never()).hasEmail();
        verify(client, never()).receiveMessage(message);
    }
}

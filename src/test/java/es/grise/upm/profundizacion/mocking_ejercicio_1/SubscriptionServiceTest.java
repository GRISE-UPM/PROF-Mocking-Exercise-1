package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
public class SubscriptionServiceTest {

    Client client;
    Client client2;
    Client client3;
    Message msg;
    SubscriptionService service;

    @BeforeEach
    public void setUp() {
        client = mock(Client.class);
        client2 = mock(Client.class);
        client3 = mock(Client.class);
        msg = mock(Message.class);
        service = new SubscriptionService();
    }

    /* STATE TESTS */

    /**
     * No se puede añadir un Client null a la lista subscribers.
     */
    @Test
    public void addNullClientTest() {
        assertThrows(NullClientException.class, () -> service.addSubscriber(null));
    }

    /**
     * Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
     */
    @Test
    public void addClientStoresItTest() throws NullClientException, ExistingClientException {
        service.addSubscriber(client);
        assertEquals(true, service.subscribers.contains(client));
    }

    /**
     * No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers.
     * Al hacerlo, se lanza la excepción ExistingClientException.
     */
    @Test
    public void addClientTwiceTest() throws NullClientException, ExistingClientException {
        service.addSubscriber(client);
        assertThrows(ExistingClientException.class, () -> service.addSubscriber(client));
    }

    /**
     * Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.
     */
    @Test
    public void addDifferentClientsTest() throws NullClientException, ExistingClientException {
        service.addSubscriber(client);
        service.addSubscriber(client2);

        assertEquals(true, service.subscribers.contains(client));
        assertEquals(true, service.subscribers.contains(client2));
    }

    /**
     * No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers.
     * Al hacerlo, se lanza la excepción NullClientException.
     */
    @Test
    public void removeNullClientTest() {
        assertThrows(NullClientException.class, () -> service.removeSubscriber(null));
    }

    /**
     * No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en la lista subscribers.
     * Al hacerlo, se lanza la excepción NonExistingClientException.
     */
    @Test
    public void removeNonExistingClientTest() {
        assertThrows(NonExistingClientException.class, () -> service.removeSubscriber(client));
    }

    /**
     * Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en la lista subscribers
     */
    @Test
    public void removeOkClientTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        service.addSubscriber(client);
        service.removeSubscriber(client);

        assertEquals(false, service.subscribers.contains(client));
    }

    /**
     * No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers.
     * Al hacerlo, se lanza la excepción NonExistingClientException.
     */
    @Test
    public void removeClientTwiceTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        service.addSubscriber(client);
        service.removeSubscriber(client);

        assertThrows(NonExistingClientException.class, () -> service.removeSubscriber(client));
    }

    /**
     * Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en la lista subscribers.
     */
    @Test
    public void removeDifferentClientsTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        service.addSubscriber(client);
        service.addSubscriber(client2);
        service.addSubscriber(client3);

        service.removeSubscriber(client);
        service.removeSubscriber(client2);

        assertEquals(false, service.subscribers.contains(client));
        assertEquals(false, service.subscribers.contains(client2));
        assertEquals(true, service.subscribers.contains(client3));
    }

    /**
     * Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en la lista subscribers.
     */
    @Test
    public void removeaAllClientsTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        service.addSubscriber(client);
        service.addSubscriber(client2);

        service.removeSubscriber(client);
        service.removeSubscriber(client2);

        assertEquals(false, service.subscribers.contains(client));
        assertEquals(false, service.subscribers.contains(client2));
        assertEquals(true, service.subscribers.isEmpty());
    }

    /* INTERACTION TESTS */

    /**
     * Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
     */
    @Test
    public void clientReceivesMessageTest() throws NullClientException, ExistingClientException {
        when(client.hasEmail()).thenReturn(true);

        service.addSubscriber(client);
        service.sendMessage(msg);

        verify(client).receiveMessage(msg);
    }

    /**
     * Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
     */
    @Test
    public void clientDoesNotReceiveMessageTest() throws NullClientException, ExistingClientException {
        when(client.hasEmail()).thenReturn(false);

        service.addSubscriber(client);
        service.sendMessage(msg);

        verify(client).hasEmail();
        verifyNoMoreInteractions(client);
    }

    /**
     * Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
     */
    @Test
    public void differentClientsReceiveMessageTest() throws NullClientException, ExistingClientException {
        when(client.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(true);
        when(client3.hasEmail()).thenReturn(true);

        service.addSubscriber(client);
        service.addSubscriber(client2);
        service.addSubscriber(client3);
        service.sendMessage(msg);

        verify(client).receiveMessage(msg);
        verify(client2).receiveMessage(msg);
        verify(client3).receiveMessage(msg);
    }

    /**
     * Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
     */
    @Test
    public void unsubscribedClientDoesNotReceiveMessageTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        when(client.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(true);

        service.addSubscriber(client);
        service.addSubscriber(client2);
        service.removeSubscriber(client);
        service.sendMessage(msg);

        verifyNoMoreInteractions(client);
        verify(client2).receiveMessage(msg);
    }

}

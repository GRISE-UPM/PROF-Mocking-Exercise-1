package es.grise.upm.profundizacion.mocking_ejercicio_1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
    SubscriptionService subService;
    Client client1, client2, client3;
    Message message;

    @BeforeEach
    public void setUp() {
        subService = new SubscriptionService();
    }

    //    State tests

    // No se puede añadir un Client null a la lista subscribers.
    @Test
    public void addSubscriberNullTest() throws NullClientException, ExistingClientException {
        assertThrows(NullClientException.class, () -> subService.addSubscriber(null));
    }

    // Al añadir un Clientmediante addSubscriber(), éste Client se almacena en la lista subscribers.
    @Test
    public void addSubscriber1Test() throws NullClientException, ExistingClientException {
        client1 = mock(Client.class);
        subService.addSubscriber(client1);
        assertTrue(subService.subscribers.contains(client1));
    }

    // No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers
    // Al hacerlo, se lanza la excepción ExistingClientException.
    @Test
    public void addSubscriber2Test() throws NullClientException, ExistingClientException {
        client1 = mock(Client.class);
        subService.addSubscriber(client1);
        assertThrows(ExistingClientException.class, () -> subService.addSubscriber(client1));
    }

    // Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.
    @Test
    public void addSubscriber3Test() throws NullClientException, ExistingClientException {
        client1 = mock(Client.class);
        client2 = mock(Client.class);
        subService.addSubscriber(client1);
        subService.addSubscriber(client2);
        assertTrue(subService.subscribers.contains(client1));
        assertTrue(subService.subscribers.contains(client2));
    }

    // No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
    @Test
    public void removeSubscriberNullTest() throws NullClientException, ExistingClientException {
        client1 = mock(Client.class);
        subService.addSubscriber(client1);
        assertThrows(NullClientException.class, () -> subService.removeSubscriber(null));
    }

    // No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
    @Test
    public void removeNonExistingClientExceptionTest() throws NullClientException, NonExistingClientException {
        client1 = mock(Client.class);
        assertFalse(subService.subscribers.contains(client1));
        assertThrows(NonExistingClientException.class, () -> subService.removeSubscriber(client1));
    }

    // Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
    @Test
    public void removeClientTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        client1 = mock(Client.class);
        subService.addSubscriber(client1);
        subService.removeSubscriber(client1);
        assertFalse(subService.subscribers.contains(client1));
    }

    // No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
    @Test
    public void removeClient2Test() throws NullClientException, ExistingClientException, NonExistingClientException {
        client1 = mock(Client.class);
        subService.addSubscriber(client1);
        subService.removeSubscriber(client1);
        assertThrows(NonExistingClientException.class, () -> subService.removeSubscriber(client1));
    }

    // Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.
    @Test
    public void removeClient3Test() throws NullClientException, ExistingClientException, NonExistingClientException {
        client1 = mock(Client.class);
        client2 = mock(Client.class);
        client3 = mock(Client.class);
        subService.addSubscriber(client1);
        subService.addSubscriber(client2);
        subService.addSubscriber(client3);
        subService.removeSubscriber(client1);
        subService.removeSubscriber(client3);
        assertFalse(subService.subscribers.contains(client1));
        assertFalse(subService.subscribers.contains(client3));
    }

    // Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.
    @Test
    public void removeAllClientTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        client1 = mock(Client.class);
        client2 = mock(Client.class);
        client3 = mock(Client.class);
        subService.addSubscriber(client1);
        subService.addSubscriber(client2);
        subService.addSubscriber(client3);
        subService.removeSubscriber(client1);
        subService.removeSubscriber(client2);
        subService.removeSubscriber(client3);
        assertTrue(subService.subscribers.isEmpty());
    }


    //    Interaction tests

    // Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).
    @Test
    public void receiveMessage1Test() throws NullClientException, ExistingClientException {
        client1 = mock(Client.class);
        message = mock(Message.class);
        when(client1.hasEmail()).thenReturn(true);
        subService.addSubscriber(client1);
        subService.sendMessage(message);
        verify(client1).hasEmail();
        verify(client1).receiveMessage(message);
    }

    // Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).
    @Test
    public void receiveMessage2Test() throws NullClientException, ExistingClientException {
        client1 = mock(Client.class);
        when(client1.hasEmail()).thenReturn(false);
        subService.addSubscriber(client1);
        message = mock(Message.class);
        subService.sendMessage(message);
        assertFalse(client1.hasEmail());
        verify(client1, never()).receiveMessage(any());
    }

    // Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).
    @Test
    public void receiveMessage3Test() throws NullClientException, ExistingClientException {
        client1 = mock(Client.class);
        client2 = mock(Client.class);
        client3 = mock(Client.class);
        message = mock(Message.class);
        when(client1.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(true);
        when(client3.hasEmail()).thenReturn(false);
        subService.addSubscriber(client1);
        subService.addSubscriber(client2);
        subService.addSubscriber(client3);
        subService.sendMessage(message);
        verify(client1).hasEmail();
        verify(client2).hasEmail();
        assertFalse(client3.hasEmail());
        verify(client1).receiveMessage(message);
        verify(client2).receiveMessage(message);
        verify(client3, never()).receiveMessage(any());
    }

    // Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
    @Test
    public void dontReceiveMessageTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        client1 = mock(Client.class);
        when(client1.hasEmail()).thenReturn(true);
        subService.addSubscriber(client1);
        subService.removeSubscriber(client1);
        message = mock(Message.class);
        subService.sendMessage(message);
        verify(client1, never()).receiveMessage(any());
    }

}

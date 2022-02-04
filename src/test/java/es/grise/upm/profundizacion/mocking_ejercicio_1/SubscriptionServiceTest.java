package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceTest {

    SubscriptionService subscriptionService = new SubscriptionService();

//    No se puede añadir un Client null a la lista subscribers.
    @Test(expected = NullClientException.class)
    public void whenANullClientIsGiven_thenThrowException() throws NullClientException, ExistingClientException {
        subscriptionService.addSubscriber(null);
    }

//    Al añadir un Clientmediante addSubscriber(), éste Client se almacena en la lista subscribers.
    @Test
    public void whenAValidClientIsGiven_thenAddToSuscribers() throws NullClientException, ExistingClientException {
        Client client = mock(Client.class);
        subscriptionService.addSubscriber(client);
        assertTrue(subscriptionService.subscribers.contains(client));
       // when(subscriptionService.addSubscriber()).thenReturn();

    }

//    No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers
//    Al hacerlo, se lanza la excepción ExistingClientException.

    @Test(expected = ExistingClientException.class)
    public void whenADuplicateClientIsGiven_thenThrowException() throws NullClientException, ExistingClientException {
        Client client = mock(Client.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.addSubscriber(client);

    }
//
//    Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.
    @Test
    public void whenSomeValidClientIsGiven_thenAddToSuscribers() throws NullClientException, ExistingClientException {
        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        assertTrue(subscriptionService.subscribers.contains(client1));
        assertTrue(subscriptionService.subscribers.contains(client2));
        assertTrue(subscriptionService.subscribers.contains(client3));

    }

//
//    No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.

    @Test(expected = NullClientException.class)
    public void whenANullClientIsRemoved_thenThrowException() throws NullClientException, NonExistingClientException {
        subscriptionService.removeSubscriber(null);
    }

//    No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.

    @Test(expected = NonExistingClientException.class)
    public void whenANonContentClientInSubscribersIsRemoved_thenThrowException() throws NullClientException, NonExistingClientException {
        Client client4 = mock(Client.class);
        subscriptionService.removeSubscriber(client4);
    }

//    Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
    @Test
    public void whenAValidClientIsGiven_thenRemoveSuscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = mock(Client.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        assertFalse(subscriptionService.subscribers.contains(client));

    }

//    No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.

    @Test(expected = NonExistingClientException.class)
    public void whenAClientInSubscribersIsRemovedTwice_thenThrowException() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client = mock(Client.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        subscriptionService.removeSubscriber(client);
    }

//    Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.

    @Test
    public void whenSomeValidClientIsGiven_thenRemoveSuscribers() throws NullClientException, ExistingClientException, NonExistingClientException {
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

    }

//    Interaction tests
//
//    Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).

    @Test
    public void whenAClientIsSubscribedAndHasEmail_ReceiveMessage() throws NullClientException, ExistingClientException {
        Client client = Mockito.mock(Client.class);
        when(client.hasEmail()).thenReturn(true);
        Message message = Mockito.mock(Message.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.sendMessage(message);
        verify(client, times(1)).receiveMessage(message);

    }

//    Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).

    @Test
    public void whenAClientIsSubscribedAndNotEmail_ReceiveMessage() throws NullClientException, ExistingClientException {
        Client client = Mockito.mock(Client.class);
        when(client.hasEmail()).thenReturn(false);
        Message message = Mockito.mock(Message.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.sendMessage(message);
        verify(client, times(0)).receiveMessage(message);

    }

//    Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).

    @Test
    public void whenClientsIsSubscribedAndHasEmail_ReceiveMessage() throws NullClientException, ExistingClientException {
        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);

        when(client1.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(true);
        when(client3.hasEmail()).thenReturn(true);

        Message message = Mockito.mock(Message.class);

        subscriptionService.sendMessage(message);
        verify(client1, times(1)).receiveMessage(message);
        verify(client2, times(1)).receiveMessage(message);
        verify(client3, times(1)).receiveMessage(message);

    }

//    Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).

    @Test
    public void whenAClientIsNotSubscribed_NotReceiveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        when(client.hasEmail()).thenReturn(true);
        Message message = Mockito.mock(Message.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        subscriptionService.sendMessage(message);
        verify(client, times(0)).receiveMessage(message);

    }



}

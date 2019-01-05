package es.grise.upm.profundizacion.tema8;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {


    //Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
    @Test
    public void addClient() throws ExistingClientException, NullClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        s.addSubscriber(c);
        assertEquals(s.subscribers.size(),1);
    }


    //No se puede añadir un Client null a la lista subscribers.
    @Test(expected = NullClientException.class)
    public void addNullSub() throws ExistingClientException, NullClientException {
        SubscriptionService s = new SubscriptionService();
        s.addSubscriber(null);

    }

    //No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers.
    //Al hacerlo, se lanza la excepción ExistingClientException.
    @Test(expected = ExistingClientException.class)
    public void addSameClientTwice() throws ExistingClientException, NullClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        s.addSubscriber(c);
        s.addSubscriber(c);
    }



    //Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
    @Test
    public void addTwoClients() throws ExistingClientException, NullClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Client c2 = mock(Client.class);
        s.addSubscriber(c);
        s.addSubscriber(c2);
        assertEquals(s.subscribers.size(),2);
    }
    //No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers.
    //Al hacerlo, se lanza la excepción NullClientException.
    @Test(expected = NullClientException.class)
    public void removeNullClient() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService s = new SubscriptionService();
        s.removeSubscriber(null);
    }


    //No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers.
    //Al hacerlo, se lanza la excepción NonExistingClientException.
    @Test(expected = NonExistingClientException.class)
    public void removeNonExistingClient() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Client c2 = mock(Client.class);
        s.addSubscriber(c2);
        s.removeSubscriber(c);
    }



    //Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
    @Test
    public void removeOneClient() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Client c2 = mock(Client.class);
        s.addSubscriber(c);
        s.addSubscriber(c2);
        s.removeSubscriber(c);
        assertEquals(s.subscribers.size(),1);
    }
    //No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers.
    //Al hacerlo, se lanza la excepción NonExistingClientException.
    @Test(expected = NonExistingClientException.class)
    public void deleteSameClientTwice() throws NonExistingClientException, NullClientException, ExistingClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        s.addSubscriber(c);
        s.removeSubscriber(c);
        s.removeSubscriber(c);
    }


    //Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
    @Test
    public void removeTwoClients() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Client c2 = mock(Client.class);
        Client c3 = mock(Client.class);
        s.addSubscriber(c);
        s.addSubscriber(c2);
        s.addSubscriber(c3);
        s.removeSubscriber(c);
        s.removeSubscriber(c2);
        assertEquals(s.subscribers.size(),1);
    }
    //Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
    @Test
    public void removeAllClients() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Client c2 = mock(Client.class);
        Client c3 = mock(Client.class);
        s.addSubscriber(c);
        s.removeSubscriber(c);
        assertEquals(s.subscribers.size(),0);
    }


    //Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
    @Test
    public void oneClientEmailTrue() throws ExistingClientException, NullClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Message msg = mock(Message.class);

        doReturn(true).when(c).hasEmail();
        doNothing().when(c).receiveMessage(msg);
        s.addSubscriber(c);
        s.sendMessage(msg);
        verify(c,times(1))
                .receiveMessage(msg);
    }


    //Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
    @Test
    public void oneClientEmailFalse() throws ExistingClientException, NullClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Message msg = mock(Message.class);

        doReturn(false).when(c).hasEmail();
        doNothing().when(c).receiveMessage(msg);
        s.addSubscriber(c);
        s.sendMessage(msg);
        verify(c,times(0))
                .receiveMessage(msg);
    }



    //Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
    @Test
    public void twoClientEmailTrue() throws ExistingClientException, NullClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Client c2 = mock(Client.class);
        Message msg = mock(Message.class);

        doReturn(true).when(c).hasEmail();
        doReturn(true).when(c2).hasEmail();
        doNothing().when(c).receiveMessage(msg);
        doNothing().when(c2).receiveMessage(msg);
        s.addSubscriber(c);
        s.addSubscriber(c2);
        s.sendMessage(msg);
        verify(c,times(1))
                .receiveMessage(msg);
        verify(c2,times(1))
                .receiveMessage(msg);
    }




    //Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
    @Test
    public void oneClientUnsusbcribe() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService s = new SubscriptionService();
        Client c = mock(Client.class);
        Message msg = mock(Message.class);

        doReturn(true).when(c).hasEmail();
        doNothing().when(c).receiveMessage(msg);
        s.addSubscriber(c);
        s.removeSubscriber(c);
        s.sendMessage(msg);
        verify(c,times(0))
                .receiveMessage(msg);
    }

}

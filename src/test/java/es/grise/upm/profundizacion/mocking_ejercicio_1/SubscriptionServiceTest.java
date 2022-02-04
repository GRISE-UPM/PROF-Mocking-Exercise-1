package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

    private SubscriptionService subServ;

    @Before
    public void subscriptionService(){
        subServ = new SubscriptionService();
    }

    @Test(expected = NullClientException.class)
    public void clientNull() throws ExistingClientException, NullClientException {
       subServ.addSubscriber(null);
    }

    @Test
    public void subscriberWellAdded() throws ExistingClientException, NullClientException {
        Client c = mock(Client.class);
        subServ.addSubscriber(c);
        assertTrue(subServ.subscribers.contains(c));
    }

    @Test(expected = ExistingClientException.class)
    public void existingClientException() throws ExistingClientException, NullClientException {
        Client c = mock(Client.class);
        subServ.addSubscriber(c);
        subServ.addSubscriber(c);
    }
    @Test
    public void addMultilpeSubscribers() throws ExistingClientException, NullClientException {
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        subServ.addSubscriber(c1);
        subServ.addSubscriber(c2);
        boolean result = subServ.subscribers.contains(c1) && subServ.subscribers.contains(c2);
        assertTrue(result);
    }

    //o se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException
    @Test(expected = NullClientException.class)
    public void notNullRemovable() throws NonExistingClientException, NullClientException {
        subServ.removeSubscriber(null);
    }

    @Test(expected = NonExistingClientException.class)
    public void removeNonExistingClient() throws NonExistingClientException, NullClientException {
        Client c = mock(Client.class);
        subServ.removeSubscriber(c);
    }

    @Test
    public void removeClientCorrectly() throws ExistingClientException, NullClientException, NonExistingClientException {
        Client c = mock(Client.class);
        subServ.addSubscriber(c);
        subServ.removeSubscriber(c);
    }

    @Test(expected = NonExistingClientException.class)
    public void removeClientTwoTimes() throws ExistingClientException, NullClientException, NonExistingClientException {
        Client c = mock(Client.class);
        subServ.addSubscriber(c);
        subServ.removeSubscriber(c);
        subServ.removeSubscriber(c);
    }

    @Test
    public void removeMultipleClients() throws ExistingClientException, NullClientException, NonExistingClientException {
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        subServ.addSubscriber(c1);
        subServ.addSubscriber(c2);
        subServ.removeSubscriber(c1);
        subServ.removeSubscriber(c2);
    }

    @Test
    public void clientReceivesMessages() throws ExistingClientException, NullClientException, NonExistingClientException {
        Client c1 = mock(Client.class);
        Message msg = mock(Message.class);
        when(c1.hasEmail()).thenReturn(true);
        subServ.addSubscriber(c1);
        subServ.sendMessage(msg);
        verify(c1).receiveMessage(any());
    }

    @Test
    public void notReceivedMessage() throws ExistingClientException, NullClientException {
        Client c1 = mock(Client.class);
        Message msg = mock(Message.class);
        when(c1.hasEmail()).thenReturn(false);
        subServ.addSubscriber(c1);
        subServ.sendMessage(msg);
        verify(c1,never()).receiveMessage(any());
    }

    @Test
    public void multipleClientsMessages() throws ExistingClientException, NullClientException {
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        Message msg1 = mock(Message.class);
        when(c1.hasEmail()).thenReturn(true);
        when(c2.hasEmail()).thenReturn(true);
        subServ.addSubscriber(c1);
        subServ.addSubscriber(c2);
        subServ.sendMessage(msg1);
        verify(c1).receiveMessage(any());
        verify(c2).receiveMessage(any());
    }

    @Test
    public void clientDesuscribed() throws ExistingClientException, NullClientException, NonExistingClientException {
        Client c1 = mock(Client.class);
        Message msg = mock(Message.class);
        when(c1.hasEmail()).thenReturn(true);
        subServ.addSubscriber(c1);
        subServ.removeSubscriber(c1);
        subServ.sendMessage(msg);
        verify(c1,never()).receiveMessage(any());
    }
}

package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class SubscriptionServiceTest {

    @Test (expected = NullClientException.class)
    public void errorClienteNull() throws ExistingClientException, NullClientException {
        SubscriptionService ss = new SubscriptionService();
        ss.addSubscriber(null);
    }

    @Test
    public void seAñadeCliente() throws ExistingClientException, NullClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        ss.addSubscriber(mockClient);
        assertTrue(ss.subscribers.contains(mockClient));
    }

    @Test (expected = ExistingClientException.class)
    public void dosClientesIguales() throws ExistingClientException, NullClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        ss.addSubscriber(mockClient);
        ss.addSubscriber(mockClient);



    }


    @Test
    public void añadirVariosClient() throws ExistingClientException, NullClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient1 = mock(Client.class);
        Client mockClient2 = mock(Client.class);
        ss.addSubscriber(mockClient1);
        ss.addSubscriber(mockClient2);
        assertTrue(ss.subscribers.contains(mockClient1) && ss.subscribers.contains(mockClient2));

    }

    @Test (expected = NullClientException.class)
    public void quitarClienteNull() throws NullClientException, NonExistingClientException {
        SubscriptionService ss = new SubscriptionService();
        ss.removeSubscriber(null);

    }

    @Test (expected = NonExistingClientException.class)
    public void quitarClienteNoExistente() throws NullClientException, NonExistingClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        ss.removeSubscriber(mockClient);
    }

    @Test
    public void eliminaClienteCorrectamente() throws NullClientException, NonExistingClientException, ExistingClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        ss.addSubscriber(mockClient);
        ss.removeSubscriber(mockClient);
        assertEquals(0,ss.subscribers.size());
    }

    @Test (expected = NonExistingClientException.class)
    public void eliminaDosVecesMismoCliente() throws NullClientException, NonExistingClientException, ExistingClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        ss.addSubscriber(mockClient);
        ss.removeSubscriber(mockClient);
        ss.removeSubscriber(mockClient);
    }

    @Test
    public void eliminaVariosClientes() throws NullClientException, NonExistingClientException, ExistingClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient1 = mock(Client.class);
        Client mockClient2 = mock(Client.class);
        Client mockClient3 = mock(Client.class);
        Client mockClient4 = mock(Client.class);
        ss.addSubscriber(mockClient1);
        ss.addSubscriber(mockClient2);
        ss.addSubscriber(mockClient3);
        ss.addSubscriber(mockClient4);
        ss.removeSubscriber(mockClient1);
        ss.removeSubscriber(mockClient2);
        assertTrue(ss.subscribers.contains(mockClient3)
                && ss.subscribers.contains(mockClient4) && !ss.subscribers.contains(mockClient1)
                && !ss.subscribers.contains(mockClient2));

    }

    @Test
    public void eliminaTodos() throws NullClientException, NonExistingClientException, ExistingClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient1 = mock(Client.class);
        Client mockClient2 = mock(Client.class);
        Client mockClient3 = mock(Client.class);
        Client mockClient4 = mock(Client.class);
        ss.addSubscriber(mockClient1);
        ss.addSubscriber(mockClient2);
        ss.addSubscriber(mockClient3);
        ss.addSubscriber(mockClient4);
        ss.removeSubscriber(mockClient1);
        ss.removeSubscriber(mockClient2);
        ss.removeSubscriber(mockClient3);
        ss.removeSubscriber(mockClient4);
        assertEquals(0,ss.subscribers.size());

    }


    //TODO: Tests de Interaccion

    @Test
    public void recibeMensajesConEmail() throws ExistingClientException, NullClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        //when(listMock.add(anyString())).thenReturn(false);
        Mockito.when(mockClient.hasEmail()).thenReturn(true);
        ss.addSubscriber(mockClient);
        Message msg = mock(Message.class);
        ss.sendMessage(msg);
        //verify(mockedList).size();
        verify(mockClient).receiveMessage(msg);
    }

    @Test
    public void noRecibeMensajesSinEmail() throws ExistingClientException, NullClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        //when(listMock.add(anyString())).thenReturn(false);
        Mockito.when(mockClient.hasEmail()).thenReturn(false);
        ss.addSubscriber(mockClient);
        Message msg = mock(Message.class);
        ss.sendMessage(msg);
        //verify(mockedList).size();
        verify(mockClient,times(0)).receiveMessage(any());
    }

    @Test
    public void variosRecibenMensajesConEmail() throws ExistingClientException, NullClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        Client mockClient1 = mock(Client.class);
        //when(listMock.add(anyString())).thenReturn(false);
        Mockito.when(mockClient.hasEmail()).thenReturn(true);
        Mockito.when(mockClient1.hasEmail()).thenReturn(true);
        ss.addSubscriber(mockClient);
        ss.addSubscriber(mockClient1);
        Message msg = mock(Message.class);
        ss.sendMessage(msg);
        //verify(mockedList).size();
        verify(mockClient).receiveMessage(any());
        verify(mockClient1).receiveMessage(any());
    }


    @Test
    public void siUnsubscribedNoRecibeMensaje() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService ss = new SubscriptionService();
        Client mockClient = mock(Client.class);
        Client mockClient1 = mock(Client.class);
        Mockito.when(mockClient.hasEmail()).thenReturn(true);
        Mockito.when(mockClient1.hasEmail()).thenReturn(false);
        ss.addSubscriber(mockClient);
        ss.addSubscriber(mockClient1);
        Message msg = mock(Message.class);
        ss.sendMessage(msg);
        ss.removeSubscriber(mockClient);
        ss.sendMessage(msg);
        verify(mockClient, times(1)).receiveMessage(msg);
    }



	
}

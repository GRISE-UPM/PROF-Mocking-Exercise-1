package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceTest {

    protected SubscriptionService service;

    @Before
    public void beginTest() {
        service = new SubscriptionService();
    }

    //No se puede añadir un cliente null

    @Test(expected = NullClientException.class)
    public void nullClientTest() throws NullClientException {
        ExistingClientException ex = null;
        try {
            service.addSubscriber(null);
        } catch (ExistingClientException e) {
            ex = e;
        }
    }

    //Añadir client y almacenar en lista
    @Test
    public void addClientTest() throws NullClientException, ExistingClientException {
        Client c = mock(Client.class);
        service.addSubscriber(c);
        assertTrue(service.subscribers.contains(c));

    }

    //No se puede añadir mismo cliente dos veces
    @Test(expected = ExistingClientException.class)
    public void addSameClientTest() throws NullClientException, ExistingClientException {
        Client c = mock(Client.class);
        service.addSubscriber(c);
        service.addSubscriber(c);
    }

    //Añadir varios clientes
    @Test
    public void addSomeClientsTest() throws NullClientException, ExistingClientException {
        Client a = mock(Client.class);
        Client b = mock(Client.class);
        Client c = mock(Client.class);
        Client d = mock(Client.class);
        service.addSubscriber(a);
        service.addSubscriber(b);
        service.addSubscriber(c);
        service.addSubscriber(d);
        assertEquals(4, service.subscribers.size());
    }

    //No se puede borrar cliente null
    @Test(expected = NullClientException.class)
    public void deleteNullClientTest() throws NullClientException, NonExistingClientException {
        Client c = mock(Client.class);
        service.removeSubscriber(null);
    }

    //No se puede borrar un cliente que no está en la lista
    @Test(expected = NonExistingClientException.class)
    public void deleteNonExistingClientTest() throws NullClientException, NonExistingClientException {
        Client c = mock(Client.class);
        service.removeSubscriber(c);
    }

    //Eliminar cliente almacenado
    @Test
    public void deleteExistingClientTest() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client c = mock(Client.class);
        service.addSubscriber(c);
        service.removeSubscriber(c);
        assertEquals(0, service.subscribers.size());
    }

    //No eliminar un cliente ya eliminado
    @Test(expected = NonExistingClientException.class)
    public void deleteExistingClientTwiceTest() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client c = mock(Client.class);
        service.addSubscriber(c);
        service.removeSubscriber(c);
        service.removeSubscriber(c);
    }

    //Eliminar algunos clientes
    @Test
    public void deleteSomeExistingClientTest() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client a = mock(Client.class);
        Client b = mock(Client.class);
        Client c = mock(Client.class);
        service.addSubscriber(a);
        service.addSubscriber(b);
        service.addSubscriber(c);
        service.removeSubscriber(a);
        service.removeSubscriber(c);
        assertEquals(1, service.subscribers.size());
    }

    //Elimiar todos los cliente
    @Test
    public void deleteAllExistingClientTest() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client a = mock(Client.class);
        Client b = mock(Client.class);
        Client c = mock(Client.class);
        service.addSubscriber(a);
        service.addSubscriber(b);
        service.addSubscriber(c);
        service.removeSubscriber(a);
        service.removeSubscriber(b);
        service.removeSubscriber(c);
        assertEquals(0, service.subscribers.size());
    }

    //INTERACTION TESTS

    //Cliente suscrito recibe mensaje si tiene email
    @Test
    public void ClientEmailTest() throws NullClientException, ExistingClientException {
        Client c = mock(Client.class);
        service.addSubscriber(c);
        when(c.hasEmail()).thenReturn(true);
        service.sendMessage(mock(Message.class));
        verify(c).receiveMessage(any());
    }

    //Cliente suscrito no recibe mensaje si no tiene email
    @Test
    public void ClientNoEmailTest() throws NullClientException, ExistingClientException {
        Client c = mock(Client.class);
        service.addSubscriber(c);
        when(c.hasEmail()).thenReturn(false);
        service.sendMessage(mock(Message.class));
        verify(c, never()).receiveMessage(any());
    }

    //Varios Clientes suscritos reciben varios mensajes si tiene email
    @Test
    public void SomeClientsSomeEmailsTest() throws NullClientException, ExistingClientException {
        Client a = mock(Client.class);
        Client b = mock(Client.class);
        service.addSubscriber(a);
        service.addSubscriber(b);
        when(a.hasEmail()).thenReturn(true);
        when(b.hasEmail()).thenReturn(true);
        service.sendMessage(mock(Message.class));
        verify(a).receiveMessage(any());
        verify(b).receiveMessage(any());
    }

    //Si cliente se desuscribe no recibe message
    @Test
    public void ClientUnsuscribedNoEmailTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client c = mock(Client.class);
        service.addSubscriber(c);
        when(c.hasEmail()).thenReturn(false);
        service.sendMessage(mock(Message.class));
        service.removeSubscriber(c);
        verify(c, never()).receiveMessage(any());
    }

	
}

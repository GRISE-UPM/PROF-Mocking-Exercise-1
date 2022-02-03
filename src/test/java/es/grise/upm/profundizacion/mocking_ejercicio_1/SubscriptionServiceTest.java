package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import es.grise.upm.profundizacion.mocking_ejercicio_1.SubscriptionService;

public class SubscriptionServiceTest {
	
    SubscriptionService service;

    @Before
    public void inicio(){
    service = new SubscriptionService();
    }
    @Test(expected = NullClientException.class)
    public void nullclient() throws NullClientException, ExistingClientException{
       service.addSubscriber(null);
    }

    @Test
    public void clientealmacenado() throws NullClientException, ExistingClientException{
        Client c = mock(Client.class);
        service.addSubscriber(c);
        assertTrue(service.subscribers.contains(c));
    }

    @Test(expected = ExistingClientException.class)
    public void clienteanadidodosveces() throws NullClientException, ExistingClientException{
        Client c = mock(Client.class);
        service.addSubscriber(c);
        service.addSubscriber(c);
    }

    @Test
    public void variosclientesanadidos() throws NullClientException, ExistingClientException{
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        service.addSubscriber(c1);
        service.addSubscriber(c2);
        assertTrue(service.subscribers.contains(c1) && service.subscribers.contains(c2));
    }

    @Test(expected = NullClientException.class)
    public void borrarnullclient() throws NonExistingClientException, NullClientException{
       service.removeSubscriber(null);
    }

    @Test(expected = NonExistingClientException.class)
    public void borrarclientenoexistente() throws NonExistingClientException, NullClientException{
        Client c = mock(Client.class);
        service.removeSubscriber(c);
    }

    @Test
    public void borrarclienteexistente() throws NonExistingClientException, NullClientException, ExistingClientException{
        Client c = mock(Client.class);
        service.addSubscriber(c);
        service.removeSubscriber(c);
        assertTrue(!service.subscribers.contains(c));
    }

    @Test(expected = NonExistingClientException.class)
    public void borrarclientedosveces() throws NonExistingClientException, NullClientException, ExistingClientException{
        Client c = mock(Client.class);
        service.addSubscriber(c);
        service.removeSubscriber(c);
        service.removeSubscriber(c);
    }

    @Test
    public void borrarvariosclientes() throws NonExistingClientException, NullClientException, ExistingClientException{
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        Client c3 = mock(Client.class);
        service.addSubscriber(c1);
        service.addSubscriber(c2);
        service.addSubscriber(c3);
        service.removeSubscriber(c1);
        service.removeSubscriber(c2);
        assertTrue(!service.subscribers.contains(c1) && !service.subscribers.contains(c2));
    }

    @Test
    public void borrartodosclientes() throws NonExistingClientException, NullClientException, ExistingClientException{
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        Client c3 = mock(Client.class);
        service.addSubscriber(c1);
        service.addSubscriber(c2);
        service.addSubscriber(c3);
        service.removeSubscriber(c1);
        service.removeSubscriber(c2);
        service.removeSubscriber(c2);
        assertTrue(service.subscribers.isEmpty());
    }

    //Test de Interacción

    /*
    Un ``Client`` suscrito recibe mensajes (método ``receiveMessage()`` 
    si tiene email (método ``hasEmail() == true``).
    */
    @Test
    public void clienterecibemensajes() throws NullClientException, ExistingClientException{
        Client c = mock(Client.class);
        Message m = mock(Message.class);
        when(c.hasEmail()).thenReturn(true);
        service.addSubscriber(c);
        service.sendMessage(m);
        verify(c).receiveMessage(m);
    }

    /*
    Un ``Client`` suscrito no recibe mensajes (método ``receiveMessage()``
    si no tiene email (método ``hasEmail() == false``).
    */
    @Test
    public void clientenorecibemensajes() throws NullClientException, ExistingClientException{
        Client c = mock(Client.class);
        Message m = mock(Message.class);
        when(c.hasEmail()).thenReturn(false);
        service.addSubscriber(c);
        service.sendMessage(m);
        verify(c).receiveMessage(m);
    }

    /*
    Varios  ``Client`` suscritos reciben mensajes (método ``receiveMessage()`` 
    si tienen email (método ``hasEmail() == true``).
    */
    @Test
    public void variosclientesrecibenmensajes() throws NullClientException, ExistingClientException{
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        Message m = mock(Message.class);
        service.addSubscriber(c1);
        service.addSubscriber(c2);
        when(c1.hasEmail()).thenReturn(true);
        when(c2.hasEmail()).thenReturn(true);
        service.sendMessage(m);
        verify(c1).receiveMessage(m);
        verify(c2).receiveMessage(m);
    }

    /*
    Al des-suscribir un ``Client`` 
    éste no recibe mensajes (método ``receiveMessage()``).
    */
    @Test
    public void clientenorecibemensajesaldesubscribir() throws NullClientException, ExistingClientException, NonExistingClientException{
        Client c = mock(Client.class);
        Message m = mock(Message.class);
        when(c.hasEmail()).thenReturn(true);
        service.addSubscriber(c);
        service.removeSubscriber(c);
        service.sendMessage(m);
        verify(c).receiveMessage(m);
    }





}

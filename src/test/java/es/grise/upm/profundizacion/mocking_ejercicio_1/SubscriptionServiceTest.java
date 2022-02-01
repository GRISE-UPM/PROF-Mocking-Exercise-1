package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubscriptionServiceTest {

    private SubscriptionService ss;

    public SubscriptionServiceTest() {
        ss = new SubscriptionService();
    }

    @Test(expected = NullClientException.class)
    public void addNullSubscriber() throws ExistingClientException, NullClientException {
        ss.addSubscriber(null);
    }

    @Test
    public void addSubscriber() throws ExistingClientException, NullClientException {
        Client c = mock(Client.class);
        ss.addSubscriber(c);
        assertTrue("The client should be contained", ss.subscribers.contains(c));
    }

    @Test(expected = ExistingClientException.class)
    public void notTwiceSameClient() throws NullClientException, ExistingClientException {
        Client c = mock(Client.class);
        ss.addSubscriber(c);
        ss.addSubscriber(c);
    }

    @Test
    public void addMultipleSubscribers() throws ExistingClientException, NullClientException {
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        Client c3 = mock(Client.class);
        Client c4 = mock(Client.class);
        ss.addSubscriber(c1);
        ss.addSubscriber(c2);
        ss.addSubscriber(c3);
        ss.addSubscriber(c4);
        assertTrue("All clients should be contained", ss.subscribers.contains(c1) && ss.subscribers.contains(c2) && ss.subscribers.contains(c3) && ss.subscribers.contains(c4));
    }

    @Test(expected = NullClientException.class)
    public void removeNullClient() throws NullClientException, NonExistingClientException {
        ss.removeSubscriber(null);
    }

    @Test(expected = NonExistingClientException.class)
    public void removeNonSubscribedClient() throws NullClientException, NonExistingClientException, ExistingClientException, ExistingClientException, ExistingClientException {
        ss.removeSubscriber(mock(Client.class));
    }

    @Test
    public void removeSubscribedClient() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client c = mock(Client.class);
        ss.addSubscriber(c);
        ss.removeSubscriber(c);
        assertFalse("The client should be removed", ss.subscribers.contains(c));
    }

    @Test(expected = NonExistingClientException.class)
    public void removeTwiceSubscribedClient() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client c = mock(Client.class);
        ss.addSubscriber(c);
        ss.removeSubscriber(c);
        ss.removeSubscriber(c);
    }

    @Test
    public void multipleAllSubscribers() throws ExistingClientException, NullClientException, NonExistingClientException {
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        Client c3 = mock(Client.class);
        Client c4 = mock(Client.class);
        ss.addSubscriber(c1);
        ss.addSubscriber(c2);
        ss.addSubscriber(c3);
        ss.addSubscriber(c4);
        ss.removeSubscriber(c1);
        ss.removeSubscriber(c3);
        assertFalse("Clients 1 and 3 should NOT be contained", ss.subscribers.contains(c1) || ss.subscribers.contains(c3));
    }

    @Test
    public void removeAllSubscribers() throws ExistingClientException, NullClientException, NonExistingClientException {
        Client c1 = mock(Client.class);
        Client c2 = mock(Client.class);
        Client c3 = mock(Client.class);
        Client c4 = mock(Client.class);
        ss.addSubscriber(c1);
        ss.addSubscriber(c2);
        ss.addSubscriber(c3);
        ss.addSubscriber(c4);
        ss.removeSubscriber(c1);
        ss.removeSubscriber(c2);
        ss.removeSubscriber(c3);
        ss.removeSubscriber(c4);
        assertFalse("All clients should NOT be contained", ss.subscribers.contains(c1) || ss.subscribers.contains(c2) || ss.subscribers.contains(c3) || ss.subscribers.contains(c4));
    }

    @Test
    public void reciveMessage() throws NullClientException, ExistingClientException {
        Client c = mock(Client.class);
        when(c.hasEmail()).thenReturn(true);
        ss.addSubscriber(c);
        ss.sendMessage(mock(Message.class));
        verify(c).receiveMessage(any());
    }

    @Test
    public void doesNotReciveMessage() throws NullClientException, ExistingClientException {
        Client c = mock(Client.class);
        when(c.hasEmail()).thenReturn(false);
        ss.addSubscriber(c);
        ss.sendMessage(mock(Message.class));
        verify(c, never()).receiveMessage(any());
    }

    @Test
    public void multipleReciveMessage() throws NullClientException, ExistingClientException {
        Client c = mock(Client.class);
        Client c2 = mock(Client.class);
        Client c3 = mock(Client.class);
        when(c.hasEmail()).thenReturn(true);
        when(c2.hasEmail()).thenReturn(false);
        when(c3.hasEmail()).thenReturn(true);
        ss.addSubscriber(c);
        ss.addSubscriber(c2);
        ss.addSubscriber(c3);
        ss.sendMessage(mock(Message.class));

        verify(c).receiveMessage(any());
        verify(c2, never()).receiveMessage(any());
        verify(c3).receiveMessage(any());

    }

    @Test
    public void cancelReciveMessage() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client c = mock(Client.class);
        when(c.hasEmail()).thenReturn(true);
        ss.addSubscriber(c);
        ss.removeSubscriber(c);
        ss.sendMessage(mock(Message.class));
        verify(c, never()).receiveMessage(any());
    }

}

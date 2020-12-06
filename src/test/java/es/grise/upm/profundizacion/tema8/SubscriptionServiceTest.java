package es.grise.upm.profundizacion.tema8;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
    private SubscriptionService toTest;
    private Client client;
    private Client anotherClient;
    private Message message;

    @Before
    public void setup() {
        this.toTest = new SubscriptionService();
        this.client = mock(Client.class);
        this.anotherClient = mock(Client.class);
        this.message = mock(Message.class);
    }

    @Test(expected = NullClientException.class)
    public void testAddSubscriber_whenSubscriberIsNull_throwsNullClientException()
            throws Exception {
        toTest.addSubscriber(null);
    }

    @Test
    public void testAddSubscriber_happyPath()
            throws Exception {
        toTest.addSubscriber(client);
        assertTrue(toTest.subscribers.contains(client));
    }

    @Test(expected = ExistingClientException.class)
    public void testAddSubscriber_whenAddTwiceSameSubscriber_throwsExistingClientException()
            throws Exception {
        toTest.addSubscriber(client);
        toTest.addSubscriber(client);
    }

    @Test
    public void testAddSubscriber_whenAddMultiplesSubscribers_addMultipleSubscribers()
            throws Exception {
        toTest.addSubscriber(client);
        toTest.addSubscriber(anotherClient);
        assertTrue(toTest.subscribers.contains(client));
        assertTrue(toTest.subscribers.contains(anotherClient));
    }

    @Test(expected = NullClientException.class)
    public void testRemoveSubscriber_whenSubscriberIsNull_throwsNullClientException()
            throws Exception {
        toTest.removeSubscriber(null);
    }

    @Test(expected = NonExistingClientException.class)
    public void testRemoveSubscriber_whenSubscriberIsNotAdded_throwsNonExistingClientException()
            throws Exception {
        toTest.removeSubscriber(client);
    }

    @Test
    public void testRemoveSubscriber_happyPath()
            throws Exception {
        toTest.addSubscriber(client);
        toTest.removeSubscriber(client);
        assertFalse(toTest.subscribers.contains(client));
    }

    @Test(expected = NonExistingClientException.class)
    public void testRemoveSubscriber_whenSubscriberTwice_throwsNonExistingClientException()
            throws Exception {
        toTest.addSubscriber(client);
        toTest.removeSubscriber(client);
        toTest.removeSubscriber(client);
    }

    @Test
    public void testRemoveSubscriber_whenRemoveMultipleSubscribers_removesMultipleSubscribers()
            throws Exception {
        toTest.addSubscriber(client);
        toTest.addSubscriber(anotherClient);
        toTest.removeSubscriber(client);
        toTest.removeSubscriber(anotherClient);
        assertFalse(toTest.subscribers.contains(client));
        assertFalse(toTest.subscribers.contains(anotherClient));
    }

    @Test
    public void testRemoveSubscriber_whenRemoveAllSubscribers_removesAllSubscribers()
            throws Exception {
        toTest.addSubscriber(client);
        toTest.addSubscriber(anotherClient);
        toTest.removeSubscriber(client);
        toTest.removeSubscriber(anotherClient);
        assertTrue(toTest.subscribers.isEmpty());
    }

    @Test
    public void testSendMessage_whenSubscriberHasEmail_receivesMessage()
            throws Exception {
        toTest.addSubscriber(client);
        when(client.hasEmail()).thenReturn(true);

        toTest.sendMessage(message);

        verify(client).receiveMessage(message);
    }

    @Test
    public void testSendMessage_whenSubscriberHasNotEmail_receivesMessage()
            throws Exception {
        toTest.addSubscriber(client);
        when(client.hasEmail()).thenReturn(false);

        toTest.sendMessage(message);

        verify(client, never()).receiveMessage(message);
    }

    @Test
    public void testSendMessage_whenMultiplesSubscribersHaveEmail_multipleSubscribersReceiveMessages()
            throws Exception {
        toTest.addSubscriber(client);
        toTest.addSubscriber(anotherClient);
        when(client.hasEmail()).thenReturn(true);
        when(anotherClient.hasEmail()).thenReturn(true);

        toTest.sendMessage(message);

        verify(client).receiveMessage(message);
        verify(anotherClient).receiveMessage(message);
    }

    @Test
    public void testSendMessage_whenSubscriberHasUnsubscribed_doesNotReceiveMessage()
            throws Exception {
        toTest.addSubscriber(client);
        when(client.hasEmail()).thenReturn(true);
        toTest.removeSubscriber(client);

        toTest.sendMessage(message);

        verify(client, never()).receiveMessage(message);
    }
}

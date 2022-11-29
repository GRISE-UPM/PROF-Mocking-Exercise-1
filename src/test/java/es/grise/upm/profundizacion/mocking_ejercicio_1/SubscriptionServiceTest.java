package es.grise.upm.profundizacion.mocking_ejercicio_1;


import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

    SubscriptionService subscriptionService = new SubscriptionService();

    //state tests
    @Test(expected = NullClientException.class)
    public void addSubscriberNullTest() throws NullClientException, ExistingClientException {
        subscriptionService.addSubscriber(null);
    }

    @Test
    public void addSubscriberTest() throws NullClientException, ExistingClientException {
        Client client = mock(Client.class);
        subscriptionService.addSubscriber(client);
        assertEquals(1, subscriptionService.subscribers.size());
    }

    @Test(expected = ExistingClientException.class)
    public void addSubscriberTestDuplicate() throws NullClientException, ExistingClientException {
        Client client1 = mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client1);
    }

    @Test
    public void addManySubscriberTest() throws NullClientException, ExistingClientException {
        for(int i = 0; i<10; i++) {
            Client client = mock(Client.class);
            subscriptionService.addSubscriber(client);
        }
        assertEquals(10, subscriptionService.subscribers.size());
    }

    @Test(expected = NullClientException.class)
    public void removeSubscriberNullTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        subscriptionService.removeSubscriber(null);
    }

    @Test(expected = NonExistingClientException.class)
    public void removeSubscriberNonExistingTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = mock(Client.class);
        subscriptionService.removeSubscriber(client);
    }

    @Test
    public void removeSubscriberTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = mock(Client.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        assertEquals(0, subscriptionService.subscribers.size());
    }

    @Test(expected = NonExistingClientException.class)
    public void removeSubscriberTestDuplicate() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client1 = mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client1);
    }

    @Test
    public void removeManySubscribersTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        subscriptionService.removeSubscriber(client3);
        assertEquals(0, subscriptionService.subscribers.size());
    }

    @Test
    public void removeAllSubscribersTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        for(int i=0; i<10; i++) {
            Client client1 = mock(Client.class);
            subscriptionService.addSubscriber(client1);
        }
        while(subscriptionService.subscribers.iterator().hasNext()) {
            subscriptionService.removeSubscriber(subscriptionService.subscribers.iterator().next());
        }
        assertEquals(0, subscriptionService.subscribers.size());
    }

    //interaction tests
    @Test
    public void receiveMessageTestWithEmail() throws NullClientException, ExistingClientException {
        Client client = mock(Client.class);
        Message message = mock(Message.class);
        when(client.hasEmail()).thenReturn(true);
        subscriptionService.addSubscriber(client);
        subscriptionService.sendMessage(message);
        verify(client, times(1)).receiveMessage(message);
    }

    @Test
    public void receiveMessageTestWithNoEmail() throws NullClientException, ExistingClientException {
        Client client = mock(Client.class);
        Message message = mock(Message.class);
        when(client.hasEmail()).thenReturn(false);
        subscriptionService.addSubscriber(client);
        subscriptionService.sendMessage(message);
        verify(client, never()).receiveMessage(message);
    }

    @Test
    public void receiveMultipleMessageTestWithEmail() throws NullClientException, ExistingClientException {
        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);
        Message message = mock(Message.class);
        when(client1.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(true);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.sendMessage(message);
        verify(client1, times(1)).receiveMessage(message);
        verify(client2, times(1)).receiveMessage(message);
    }

    @Test
    public void receiveMessageTestNonSubcribed() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = mock(Client.class);
        Message message = mock(Message.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        subscriptionService.sendMessage(message);
        verify(client, never()).receiveMessage(message);
    }




	
}

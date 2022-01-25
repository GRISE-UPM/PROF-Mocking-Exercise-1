package es.grise.upm.profundizacion.mocking_ejercicio_1;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

    private SubscriptionService subscriptionService;


    @Test
    public void NotClientNull(){
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        assertThrows(NullClientException.class, () -> subscriptionService.addSubscriber(null));

    }

    @Test
    public void AddSubscriber() throws NullClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Client client = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client);
        assertTrue(subscriptionService.subscribers.contains(client));
    }

    @Test
    public void NotAddTheSameClient() throws NullClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Client client = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client);
        assertThrows(ExistingClientException.class, () -> subscriptionService.addSubscriber(client));
    }

    @Test
    public void AddSomeSubscriber() throws NullClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        assertTrue(subscriptionService.subscribers.contains(client1));
        assertTrue(subscriptionService.subscribers.contains(client2));
    }

    @Test
    public void RemovingNullClient() {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));

        assertThrows(NullClientException.class, () -> subscriptionService.removeSubscriber(null));
    }

    @Test
    public void RemovingClientNotIsInTheList() {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Client client = Mockito.mock(Client.class);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client));
    }

    @Test
    public void RemovingClientIsInTheList() throws NullClientException, ExistingClientException, NonExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Client client = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        assertFalse(subscriptionService.subscribers.contains(client));
    }

    @Test
    public void RemovingTheSameClientTwoTimes() throws NullClientException, NonExistingClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Client client = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(client);
        subscriptionService.removeSubscriber(client);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client));
    }

    @Test
    public void RemovingTwoClients() throws NullClientException, NonExistingClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        // Clients
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Client client3 = Mockito.mock(Client.class);
        Client client4 = Mockito.mock(Client.class);
        // Add Clients to the list
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.addSubscriber(client4);
        // Remove two clients
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        // Checking the subscribers list
        assertFalse(subscriptionService.subscribers.contains(client1));
        assertFalse(subscriptionService.subscribers.contains(client2));
        assertTrue(subscriptionService.subscribers.contains(client3));
        assertTrue(subscriptionService.subscribers.contains(client4));
    }
    @Test
    public void RemovingAllClients() throws NullClientException, NonExistingClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        // Clients
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Client client3 = Mockito.mock(Client.class);
        Client client4 = Mockito.mock(Client.class);
        // Add Clients to the list
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.addSubscriber(client4);
        // Remove all clients
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        subscriptionService.removeSubscriber(client3);
        subscriptionService.removeSubscriber(client4);
        // Checking the subscribers list
        assertFalse(subscriptionService.subscribers.contains(client1));
        assertFalse(subscriptionService.subscribers.contains(client2));
        assertFalse(subscriptionService.subscribers.contains(client3));
        assertFalse(subscriptionService.subscribers.contains(client4));
    }

    @Test
    public void clientHasEmailReceiveMessage() throws NullClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Message message = Mockito.mock(Message.class);
        Client client = Mockito.mock(Client.class);
        when(client.hasEmail()).thenReturn(true);
        doNothing().when(client).receiveMessage(isA(Message.class));
        subscriptionService.addSubscriber(client);
        subscriptionService.sendMessage(message);
        verify(client, times(1)).receiveMessage(message);
    }

    @Test
    public void clientNoHasEmailNoReceiveMessage() throws NullClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Message message = Mockito.mock(Message.class);
        Client client = Mockito.mock(Client.class);
        when(client.hasEmail()).thenReturn(false);
        subscriptionService.addSubscriber(client);
        subscriptionService.sendMessage(message);
        verify(client, times(0)).receiveMessage(message);
    }

    @Test
    public void clientsHasEmailReceiveMessage() throws NullClientException, ExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Message message = Mockito.mock(Message.class);
        Client client1 = Mockito.mock(Client.class);
        when(client1.hasEmail()).thenReturn(true);
        Client client2 = Mockito.mock(Client.class);
        when(client2.hasEmail()).thenReturn(true);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.sendMessage(message);
        verify(client1, times(1)).receiveMessage(message);
        verify(client2, times(1)).receiveMessage(message);
    }

    @Test
    public void clientsHasEmailButUnsubscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
        subscriptionService = Mockito.mock(SubscriptionService.class, withSettings()
                .useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        Message message = Mockito.mock(Message.class);
        Client client1 = Mockito.mock(Client.class);
        when(client1.hasEmail()).thenReturn(true);
        Client client2 = Mockito.mock(Client.class);
        when(client2.hasEmail()).thenReturn(true);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.removeSubscriber(client2);
        subscriptionService.sendMessage(message);
        verify(client1, times(1)).receiveMessage(message);
        verify(client2, times(0)).receiveMessage(message);
    }


}

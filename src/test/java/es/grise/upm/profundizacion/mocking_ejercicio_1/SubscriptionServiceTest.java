package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class SubscriptionServiceTest {

    SubscriptionService subService;


    @BeforeEach
    public void setUp() {
        subService = new SubscriptionService();
    }

    @Test
    public void testAddSubscriberNull() throws NullClientException, ExistingClientException {
        assertThrows(NullClientException.class, ()-> subService.addSubscriber(null));
    }

    @Test
    public void testAddSubscriberContainsClient() throws NullClientException, ExistingClientException {
        Client clientM = mock(Client.class);
        subService.addSubscriber(clientM);
        assertTrue(subService.subscribers.contains(clientM));
    }
    
    @Test 
    public void testDuplicateSubscriberException() throws NullClientException, ExistingClientException {
        Client clientM = mock(Client.class);
        subService.addSubscriber(clientM);
        assertThrows(ExistingClientException.class, ()-> subService.addSubscriber(clientM));
        
    }

    @Test
    public void testAddSeveralSubscriberContainsClients() throws NullClientException, ExistingClientException {
        Client clientM = mock(Client.class);
        subService.addSubscriber(clientM);
        Client clientM2 = mock(Client.class);
        subService.addSubscriber(clientM2);
        assertTrue(subService.subscribers.containsAll(List.of(clientM,clientM2)));
    }


    @Test
    public void testRemoveSubscriberNull() throws NullClientException, ExistingClientException {
        Client clientM = mock(Client.class);
        subService.addSubscriber(clientM);
        assertThrows(NullClientException.class, ()-> subService.removeSubscriber(null));
    }
    @Test
    public void testRemoveSubscriberNonExistingException() throws NullClientException, ExistingClientException {
        Client clientM = mock(Client.class);
        assertThrows(NonExistingClientException.class, ()-> subService.removeSubscriber(clientM));
    }
    @Test
    public void testRemoveSubscriberClient() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client clientM = mock(Client.class);
        subService.addSubscriber(clientM);
        Client clientM2 = mock(Client.class);
        subService.addSubscriber(clientM2);
        assertTrue(subService.subscribers.containsAll(List.of(clientM,clientM2)));
        subService.removeSubscriber(clientM);
        assertTrue(!subService.subscribers.contains(clientM));
    }
    @Test
    public void testRemoveSubscriberClientNonExistingClientException() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client clientM = mock(Client.class);
        subService.addSubscriber(clientM);
        Client clientM2 = mock(Client.class);
        subService.addSubscriber(clientM2);
        assertTrue(subService.subscribers.containsAll(List.of(clientM,clientM2)));
        subService.removeSubscriber(clientM);
        assertTrue(!subService.subscribers.contains(clientM));
        assertThrows(NonExistingClientException.class, ()-> subService.removeSubscriber(clientM));

    }

    @Test
    public void testRemoveSeveralSubscriberClients() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client clientM = mock(Client.class);
        subService.addSubscriber(clientM);
        Client clientM2 = mock(Client.class);
        subService.addSubscriber(clientM2);
        assertTrue(subService.subscribers.containsAll(List.of(clientM,clientM2)));
        subService.removeSubscriber(clientM);
        assertTrue(!subService.subscribers.contains(clientM));
        subService.removeSubscriber(clientM2);
        assertTrue(!subService.subscribers.contains(clientM2));

    }
    @Test
    public void testRemoveAllSubscriberClients() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client clientM = mock(Client.class);
        subService.addSubscriber(clientM);
        Client clientM2 = mock(Client.class);
        subService.addSubscriber(clientM2);
        assertTrue(subService.subscribers.containsAll(List.of(clientM,clientM2)));
        subService.removeSubscriber(clientM);
        assertTrue(!subService.subscribers.contains(clientM));
        subService.removeSubscriber(clientM2);
        assertTrue(!subService.subscribers.contains(clientM2));
    }


    // INTERACTION TESTS

    @Test
    public void testSendMessage() throws NullClientException, ExistingClientException {
        Client client = mock(Client.class);
        Mockito.when(client.hasEmail()).thenReturn(true);
        Message message = mock(Message.class);
        subService.addSubscriber(client);
        subService.sendMessage(message);
        verify(client, times(1)).receiveMessage(message);
    }

    @Test
    public void testDontReceiveMessageNoEmail()
            throws NullClientException, ExistingClientException {

        Client client = mock(Client.class);
        Mockito.when(client.hasEmail()).thenReturn(false);
        Message message = mock(Message.class);
        subService.addSubscriber(client);
        subService.sendMessage(message);
        verify(client, times(0)).receiveMessage(message);
    }

    @Test
    public void testSendMessageMultipleClients() throws NullClientException, ExistingClientException {
        Client client = mock(Client.class);
        Client client2 = mock(Client.class);
        Mockito.when(client.hasEmail()).thenReturn(true);
        Mockito.when(client2.hasEmail()).thenReturn(true);
        Message message = mock(Message.class);
        subService.addSubscriber(client);
        subService.addSubscriber(client2);
        subService.sendMessage(message);
        verify(client, times(1)).receiveMessage(message);
        verify(client2, times(1)).receiveMessage(message);
    }

    @Test
    public void testSendMessageButNoSubscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = mock(Client.class);
        Mockito.when(client.hasEmail()).thenReturn(true);
        Message message = mock(Message.class);
        subService.addSubscriber(client);
        subService.sendMessage(message);
        verify(client, times(1)).receiveMessage(message);
        subService.removeSubscriber(client);
        subService.sendMessage(message);
        verify(client, times(1)).receiveMessage(message);

    }

}

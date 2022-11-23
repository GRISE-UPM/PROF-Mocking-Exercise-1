package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mockito;


public class SubscriptionServiceTest {

    @InjectMocks
    SubscriptionService testedClass = new SubscriptionService();
	@Test 
    public void testClientInsertion() throws NullClientException, ExistingClientException {
        testedClass.addSubscriber(Mockito.mock(Client.class));
        Assertions.assertEquals(testedClass.subscribers.stream().findAny().isPresent(), true);
    }

    @Test
    public void testClientDuplicated() throws NullClientException, ExistingClientException {
        Client client = Mockito.mock(Client.class);
        testedClass.addSubscriber(client);
        Assertions.assertThrows(ExistingClientException.class, ()-> testedClass.addSubscriber(client));
    }

    @Test
    public void testClientNulled() {
        Assertions.assertThrows(NullClientException.class, ()-> testedClass.addSubscriber(null));
    }

    @Test
    public void testClientNulledRemoval() {
        Assertions.assertThrows(NullClientException.class, ()-> testedClass.removeSubscriber(null));
    }

    @Test
    public void testClientRemoveClient() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        testedClass.addSubscriber(client);
        testedClass.removeSubscriber(client);
        Assertions.assertEquals(testedClass.subscribers.stream().findAny().isPresent(), false);
    }
    @Test
    public void testClientRemoveFailed() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        Assertions.assertThrows(NonExistingClientException.class, ()-> testedClass.removeSubscriber(client));
    }
    @Test
    public void testClientDuplicatedRemoval() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        testedClass.addSubscriber(client);
        testedClass.removeSubscriber(client);
        Assertions.assertThrows(NonExistingClientException.class, ()->  testedClass.removeSubscriber(client));
    }
    @Test
    public void testClientRemoveMultipleClients() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Client client3 = Mockito.mock(Client.class);
        testedClass.addSubscriber(client1);
        testedClass.addSubscriber(client2);
        testedClass.addSubscriber(client3);
        testedClass.removeSubscriber(client1);
        testedClass.removeSubscriber(client2);
        Assertions.assertEquals(testedClass.subscribers.stream().findAny().get(), client3);
        testedClass.removeSubscriber(client3);
        Assertions.assertEquals(testedClass.subscribers.stream().findAny().isPresent(), false);
    }

    @Test
    public void testMessageSending() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        Message message = Mockito.mock(Message.class);
        testedClass.addSubscriber(client);
        Mockito.when(client.hasEmail()).thenReturn(true);
        testedClass.sendMessage(message);
        Mockito.verify(client, Mockito.times(1)).receiveMessage(message);
    }

    @Test
    public void testMessageSendingNotSubscribed() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(client.hasEmail()).thenReturn(true);
        testedClass.sendMessage(message);
        Mockito.verify(client, Mockito.times(0)).receiveMessage(message);
    }

    @Test
    public void testMessageSendingMultiple() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Message message = Mockito.mock(Message.class);
        testedClass.addSubscriber(client);
        testedClass.addSubscriber(client2);
        Mockito.when(client.hasEmail()).thenReturn(true);
        Mockito.when(client2.hasEmail()).thenReturn(true);
        testedClass.sendMessage(message);
        Mockito.verify(client, Mockito.times(1)).receiveMessage(message);
        Mockito.verify(client2, Mockito.times(1)).receiveMessage(message);
    }

    @Test
    public void testMessageSendingMulti2() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Message message = Mockito.mock(Message.class);
        testedClass.addSubscriber(client);
        testedClass.addSubscriber(client2);
        Mockito.when(client.hasEmail()).thenReturn(true);
        Mockito.when(client2.hasEmail()).thenReturn(true);
        testedClass.removeSubscriber(client2);
        testedClass.sendMessage(message);
        Mockito.verify(client, Mockito.times(1)).receiveMessage(message);
        Mockito.verify(client2, Mockito.times(0)).receiveMessage(message);
    }

    }

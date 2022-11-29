package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mockito;


public class SubscriptionServiceTest {

    @InjectMocks
    SubscriptionService ss = new SubscriptionService();

    @Test (expected = NullClientException.class)
    public void ClientNullTest() throws NullClientException, ExistingClientException{
        ss.addSubscriber(null);
    }
    @Test
    public void ClientInSubscribersTest() throws NullClientException, ExistingClientException {
        ss.addSubscriber(Mockito.mock(Client.class));
        Assertions.assertFalse(ss.subscribers.isEmpty());
    }

    @Test (expected = ExistingClientException.class)
    public void DuplicatedClientTest() throws NullClientException, ExistingClientException {
        Client client = Mockito.mock(Client.class);
        ss.addSubscriber(client);
        ss.addSubscriber(client);
    }

    @Test
    public void MultipleClientInSubscribersTest() throws NullClientException, ExistingClientException {
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Client client3 = Mockito.mock(Client.class);
        ss.addSubscriber(client1);
        ss.addSubscriber(client2);
        ss.addSubscriber(client3);
        Assertions.assertTrue(ss.subscribers.contains(client1));
        Assertions.assertTrue(ss.subscribers.contains(client2));
        Assertions.assertTrue(ss.subscribers.contains(client3));
    }


    @Test (expected = NullClientException.class)
    public void RemoveClientNullTest() throws NonExistingClientException, NullClientException {
        ss.removeSubscriber(null);
    }

    @Test (expected = NonExistingClientException.class)
    public void RemoveClientNonExistingTest() throws NonExistingClientException, NullClientException {
        Client client = Mockito.mock(Client.class);
        ss.removeSubscriber(client);
    }

    @Test
    public void RemoveClientInSubscribersTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        ss.addSubscriber(client);
        Assertions.assertTrue(ss.subscribers.contains(client));
        ss.removeSubscriber(client);
        Assertions.assertTrue(ss.subscribers.isEmpty());
    }

    @Test(expected = NonExistingClientException.class)
    public void DuplicateRemoveClientInSubscribersTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        ss.addSubscriber(client);
        ss.removeSubscriber(client);
        ss.removeSubscriber(client);
    }

    @Test
    public void MultipleRemoveClientInSubscribersTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Client client3 = Mockito.mock(Client.class);
        ss.addSubscriber(client1);
        ss.addSubscriber(client2);
        ss.addSubscriber(client3);
        Assertions.assertTrue(ss.subscribers.contains(client1));
        Assertions.assertTrue(ss.subscribers.contains(client2));
        Assertions.assertTrue(ss.subscribers.contains(client3));
        ss.removeSubscriber(client1);
        ss.removeSubscriber(client2);
        Assertions.assertFalse(ss.subscribers.contains(client1));
        Assertions.assertFalse(ss.subscribers.contains(client2));
        Assertions.assertTrue(ss.subscribers.contains(client3));
    }
    @Test
    public void AllRemoveClientInSubscribersTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Client client3 = Mockito.mock(Client.class);
        ss.addSubscriber(client1);
        ss.addSubscriber(client2);
        ss.addSubscriber(client3);
        Assertions.assertTrue(ss.subscribers.contains(client1));
        Assertions.assertTrue(ss.subscribers.contains(client2));
        Assertions.assertTrue(ss.subscribers.contains(client3));
        ss.removeSubscriber(client1);
        ss.removeSubscriber(client2);
        ss.removeSubscriber(client3);
        Assertions.assertFalse(ss.subscribers.contains(client1));
        Assertions.assertFalse(ss.subscribers.contains(client2));
        Assertions.assertFalse(ss.subscribers.contains(client3));
    }

    @Test
    public void receiveMessageTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        ss.addSubscriber(client);
        Mockito.when(client.hasEmail()).thenReturn(true);
        Message message = Mockito.mock(Message.class);
        ss.sendMessage(message);
        Mockito.verify(client, Mockito.times(1)).receiveMessage(message);
    }

    @Test
    public void noMailReceiveMessageTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client = Mockito.mock(Client.class);
        ss.addSubscriber(client);
        Mockito.when(client.hasEmail()).thenReturn(false);
        Message message = Mockito.mock(Message.class);
        ss.sendMessage(message);
        Mockito.verify(client, Mockito.times(0)).receiveMessage(message);
    }

    @Test
    public void MultipleMailReceiveMessageTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client1 = Mockito.mock(Client.class);
        Client client2 = Mockito.mock(Client.class);
        Client client3 = Mockito.mock(Client.class);
        ss.addSubscriber(client1);
        ss.addSubscriber(client2);
        ss.addSubscriber(client3);
        Mockito.when(client1.hasEmail()).thenReturn(true);
        Mockito.when(client2.hasEmail()).thenReturn(true);
        Mockito.when(client3.hasEmail()).thenReturn(true);
        Message message = Mockito.mock(Message.class);
        ss.sendMessage(message);
        Mockito.verify(client1, Mockito.times(1)).receiveMessage(message);
        Mockito.verify(client2, Mockito.times(1)).receiveMessage(message);
        Mockito.verify(client3, Mockito.times(1)).receiveMessage(message);
    }

    @Test
    public void unsuscribeReceiveMessageTest() throws NullClientException, ExistingClientException, NonExistingClientException {
        Client client1 = Mockito.mock(Client.class);
        ss.addSubscriber(client1);
        Mockito.when(client1.hasEmail()).thenReturn(true);
        Message message = Mockito.mock(Message.class);
        ss.sendMessage(message);
        Mockito.verify(client1, Mockito.times(1)).receiveMessage(message);
        ss.removeSubscriber(client1);
        Message message2 = Mockito.mock(Message.class);
        ss.sendMessage(message2);
        Mockito.verify(client1, Mockito.times(0)).receiveMessage(message2);
    }

}

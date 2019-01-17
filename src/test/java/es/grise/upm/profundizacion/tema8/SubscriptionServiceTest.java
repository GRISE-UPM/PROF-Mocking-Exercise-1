package es.grise.upm.profundizacion.tema8;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

    @Test(expected = NullClientException.class)
    public void test_null_client_lauches_null_exception() throws ExistingClientException, NullClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        //Act
        service.addSubscriber(null);
    }

    @Test
    public void test_add_client_is_storaged_in_subscribers() throws ExistingClientException, NullClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient = mock(Client.class);

        //Act
        service.addSubscriber(mockClient);

        //Assert
        assertEquals(service.subscribers.size(), 1);
        assertEquals(service.subscribers.iterator().next(), mockClient);
    }

    @Test(expected = ExistingClientException.class)
    public void test_same_client_lauches_existing_exception() throws ExistingClientException, NullClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient = mock(Client.class);

        //Act
        service.addSubscriber(mockClient);
        service.addSubscriber(mockClient);
    }

    @Test
    public void test_add_some_clients_are_storaged_in_subscribers() throws ExistingClientException, NullClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient1 = mock(Client.class);
        Client mockClient2 = mock(Client.class);
        Client mockClient3 = mock(Client.class);

        //Act
        service.addSubscriber(mockClient1);
        service.addSubscriber(mockClient2);
        service.addSubscriber(mockClient3);

        Iterator<Client> iterator = service.subscribers.iterator();
        //Assert
        assertEquals(service.subscribers.size(), 3);
        assertEquals(iterator.next(), mockClient1);
        assertEquals(iterator.next(), mockClient2);
        assertEquals(iterator.next(), mockClient3);
    }

    @Test(expected = NullClientException.class)
    public void test_remove_null_client_lauches_null_exception() throws ExistingClientException, NullClientException, NonExistingClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        //Act
        service.removeSubscriber(null);
    }

    @Test(expected = NonExistingClientException.class)
    public void test_remove_client_not_storaged_lauches_non_existing_client_exception() throws ExistingClientException, NullClientException, NonExistingClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        //Act
        Client mockClient = mock(Client.class);

        service.removeSubscriber(mockClient);
    }

    @Test
    public void test_remove_client_storaged_removes_from_subscribers() throws ExistingClientException, NullClientException, NonExistingClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient = mock(Client.class);

        //Act
        service.addSubscriber(mockClient);
        service.removeSubscriber(mockClient);

        //Assert
        assertEquals(service.subscribers.size(), 0);

    }

    @Test(expected = NonExistingClientException.class)
    public void test_remove_client_twice_lauches_non_existing_client_exception() throws ExistingClientException, NullClientException, NonExistingClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        //Act
        Client mockClient = mock(Client.class);

        service.removeSubscriber(mockClient);
        service.removeSubscriber(mockClient);
    }

    @Test
    public void test_remove_some_clients_removes_then_from_subscribers() throws ExistingClientException, NullClientException, NonExistingClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        //Act
        Client mockClient1 = mock(Client.class);
        Client mockClient2 = mock(Client.class);
        Client mockClient3 = mock(Client.class);

        service.addSubscriber(mockClient1);
        service.addSubscriber(mockClient2);
        service.addSubscriber(mockClient3);

        service.removeSubscriber(mockClient1);
        service.removeSubscriber(mockClient2);

        assertEquals(service.subscribers.size(), 1);
        assertEquals(service.subscribers.iterator().next(), mockClient3);
    }

    @Test
    public void test_remove_all_clients_removes_then_from_subscribers() throws ExistingClientException, NullClientException, NonExistingClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient1 = mock(Client.class);
        Client mockClient2 = mock(Client.class);
        Client mockClient3 = mock(Client.class);

        //Act
        service.addSubscriber(mockClient1);
        service.addSubscriber(mockClient2);
        service.addSubscriber(mockClient3);

        service.removeSubscriber(mockClient1);
        service.removeSubscriber(mockClient2);
        service.removeSubscriber(mockClient3);

        //Assert
        assertEquals(service.subscribers.size(), 0);
    }

    @Test
    public void test_client_with_email_receives_message() throws ExistingClientException, NullClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient = mock(Client.class);
        Message mockMessage = mock(Message.class);
        when(mockClient.hasEmail()).thenReturn(true);
        //Act
        service.addSubscriber(mockClient);
        service.sendMessage(mockMessage);
        //Assert
        verify(mockClient, times(1)).receiveMessage(mockMessage);

    }

    @Test
    public void test_client_with_no_email_no_receives_message() throws ExistingClientException, NullClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient = mock(Client.class);
        Message mockMessage = mock(Message.class);
        when(mockClient.hasEmail()).thenReturn(false);
        //Act
        service.addSubscriber(mockClient);
        service.sendMessage(mockMessage);
        //Assert
        verify(mockClient, times(0)).receiveMessage(mockMessage);
    }

    @Test
    public void test_some_clients_with_email_receive_message() throws ExistingClientException, NullClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient1 = mock(Client.class);
        Client mockClient2 = mock(Client.class);
        Message mockMessage = mock(Message.class);
        when(mockClient1.hasEmail()).thenReturn(true);
        when(mockClient2.hasEmail()).thenReturn(true);
        //Act
        service.addSubscriber(mockClient1);
        service.addSubscriber(mockClient2);
        service.sendMessage(mockMessage);
        //Assert
        verify(mockClient1, times(1)).receiveMessage(mockMessage);
        verify(mockClient2, times(1)).receiveMessage(mockMessage);
    }

    @Test
    public void test_some_clients_with_email_then_unsubscribe_no_receive_message() throws ExistingClientException, NullClientException {
        //Arrange
        SubscriptionService service = new SubscriptionService();
        Client mockClient1 = mock(Client.class);
        Client mockClient2 = mock(Client.class);
        Message mockMessage = mock(Message.class);
        when(mockClient1.hasEmail()).thenReturn(true);
        when(mockClient2.hasEmail()).thenReturn(true);
        //Act
        service.addSubscriber(mockClient1);
        service.addSubscriber(mockClient2);
        service.sendMessage(mockMessage);
        when(mockClient2.hasEmail()).thenReturn(false);
        service.sendMessage(mockMessage);
        //Assert
        verify(mockClient1, times(2)).receiveMessage(mockMessage);
        verify(mockClient2, times(1)).receiveMessage(mockMessage);
    }
}

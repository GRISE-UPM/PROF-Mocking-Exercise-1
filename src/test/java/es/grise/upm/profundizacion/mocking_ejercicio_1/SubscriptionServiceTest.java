package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class SubscriptionServiceTest {

    SubscriptionService ss;
    Client mockedClient;

    @BeforeEach
    public void setUp() {
        ss = new SubscriptionService();
        mockedClient = mock(Client.class);
    }

    /**
     * STATE TESTS
     */

    @Test
    public void nullClientTest() {
        assertThrows(NullClientException.class, () -> ss.addSubscriber(null));
    }

    @Test
    public void addClientTest() {
        try {
            ss.addSubscriber(mockedClient);
        } catch (Exception e) {
            // Something wrong
            fail("We could not add the client to the subscribers list");
        }
        assertTrue(ss.subscribers.contains(mockedClient));
    }

    @Test
    public void addClientTwiceTest() {
        try {
            ss.addSubscriber(mockedClient);
            assertThrows(ExistingClientException.class, () -> ss.addSubscriber(mockedClient));
        } catch (NullClientException e) {
            fail("Null Client Exception");
        } catch (ExistingClientException e) {
            assertTrue(true);
        }
    }

    @Test
    public void addSeveralClientTest() {
        try {
            Client mockedClient2 = mock(Client.class);
            Client mockedClient3 = mock(Client.class);
            ss.addSubscriber(mockedClient);
            ss.addSubscriber(mockedClient2);
            ss.addSubscriber(mockedClient3);
            assertTrue(ss.subscribers.contains(mockedClient) && ss.subscribers.contains(mockedClient2)
                    && ss.subscribers.contains(mockedClient3) && ss.subscribers.size() == 3);
        } catch (Exception e) {
            fail("We could not add the client to the subscribers list");
        }
    }

    @Test
    public void removeNullClientTest() {
        try {

            assertThrows(NullClientException.class, () -> ss.removeSubscriber(null));
        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    @Test
    public void removeNonExistingClientTest() {
        try {
            assertThrows(NonExistingClientException.class, () -> ss.removeSubscriber(mockedClient));
        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    @Test
    public void removeExistingClientTest() {
        try {
            ss.addSubscriber(mockedClient);
            ss.removeSubscriber(mockedClient);
            assertTrue(!ss.subscribers.contains(mockedClient) && ss.subscribers.size() == 0);
        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    @Test
    public void removeExistingClientTwiceTest() {
        try {
            ss.addSubscriber(mockedClient);
            ss.removeSubscriber(mockedClient);
            assertThrows(NonExistingClientException.class, () -> ss.removeSubscriber(mockedClient));
        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    @Test
    public void removeSeveralClientsTest() {
        try {
            Client mockedClient2 = mock(Client.class);
            Client mockedClient3 = mock(Client.class);

            ss.addSubscriber(mockedClient);
            ss.addSubscriber(mockedClient2);
            ss.addSubscriber(mockedClient3);

            ss.removeSubscriber(mockedClient);
            ss.removeSubscriber(mockedClient2);

            assertTrue(!ss.subscribers.contains(mockedClient) && !ss.subscribers.contains(mockedClient2)
                    && ss.subscribers.contains(mockedClient3) && ss.subscribers.size() == 1);
        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    @Test
    public void removeAllClientsTest() {
        try {
            Client mockedClient2 = mock(Client.class);
            Client mockedClient3 = mock(Client.class);

            ss.addSubscriber(mockedClient);
            ss.addSubscriber(mockedClient2);
            ss.addSubscriber(mockedClient3);

            ss.removeSubscriber(mockedClient);
            ss.removeSubscriber(mockedClient2);
            ss.removeSubscriber(mockedClient3);

            assertTrue(!ss.subscribers.contains(mockedClient) && !ss.subscribers.contains(mockedClient2)
                    && !ss.subscribers.contains(mockedClient3) && ss.subscribers.size() == 0);
        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    /**
     * INTERACTION TEST
     */

    @Test
    public void receiveMessageTest() {
        try {
            Message message = mock(Message.class);

            ss.addSubscriber(mockedClient);

            when(mockedClient.hasEmail()).thenReturn(true);

            ss.sendMessage(message);
            verify(mockedClient).receiveMessage(message);
        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    @Test
    public void notReceiveMessageTest() {
        try {
            Message message = mock(Message.class);

            ss.addSubscriber(mockedClient);

            when(mockedClient.hasEmail()).thenReturn(false);

            ss.sendMessage(message);

            // Verify that this method is not invoked
            verify(mockedClient,times(0)).receiveMessage(message);
        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    @Test
    public void someReceiveMessagesTest() {
        try {
            Message message = mock(Message.class);
            Client mockedClient2 = mock(Client.class);
            Client mockedClient3 = mock(Client.class);

            ss.addSubscriber(mockedClient);
            ss.addSubscriber(mockedClient2);
            ss.addSubscriber(mockedClient3);

            when(mockedClient.hasEmail()).thenReturn(false);
            when(mockedClient2.hasEmail()).thenReturn(true);
            when(mockedClient3.hasEmail()).thenReturn(true);


            ss.sendMessage(message);

            verify(mockedClient,times(0)).receiveMessage(message);
            verify(mockedClient2).receiveMessage(message);
            verify(mockedClient3).receiveMessage(message);

        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

    @Test
    public void unsuscribeAndNotReceiveMessageTest() {
        try {
            Message message = mock(Message.class);

            ss.addSubscriber(mockedClient);
            ss.removeSubscriber(mockedClient);

            when(mockedClient.hasEmail()).thenReturn(false);
            ss.sendMessage(message);

            verify(mockedClient,times(0)).receiveMessage(message);

        } catch (Exception e) {
            fail("Something unexpected happened");
        }
    }

}

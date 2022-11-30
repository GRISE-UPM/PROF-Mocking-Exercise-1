package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Vector;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubscriptionServiceTest {

    SubscriptionService subscriptionService;

    private final int MAX_CLIENT_NUMBER = 2467;


    @Before
    public void init(){
        subscriptionService = Mockito.spy(new SubscriptionService());
    }

    @Test
    public void cannotAddNullClient(){
        try {
            subscriptionService.addSubscriber(null);
            Assert.fail("Exception was not thrown on null client!");
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof NullClientException);
        }
    }

    @Test
    public void testAddClient() {
        try {
            Client client = Mockito.mock(Client.class);
            subscriptionService.addSubscriber(client);
            Assert.assertTrue(subscriptionService.subscribers.contains(client));
        }
        catch (Exception e) {
            Assert.fail("Unexpected exception thrown!");
        }
    }


    @Test
    public void testAddDuplicateClient() {
        try {
            Client client = Mockito.mock(Client.class);
            subscriptionService.addSubscriber(client);
            subscriptionService.addSubscriber(client);

            Assert.fail("Duplicate client exception not thrown!");
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof ExistingClientException);
        }
    }

    @Test
    public void testAddMultipleClients() {
        try {
            Vector<Client> clients = new Vector<Client>(MAX_CLIENT_NUMBER);
            for(int i = 0; i<MAX_CLIENT_NUMBER; i++) {
                clients.add(Mockito.mock(Client.class));
            }

            for(Client client:clients){
                subscriptionService.addSubscriber(client);
                Assert.assertTrue(subscriptionService.subscribers.contains(client));
            }

            Assert.assertEquals(MAX_CLIENT_NUMBER, subscriptionService.subscribers.size());

        }
        catch (Exception e) {
            Assert.fail("Unexpected exception thrown!");
        }
    }

    @Test
    public void testCannotRemoveNullClient(){
        try {
            subscriptionService.removeSubscriber(null);
            Assert.fail("Exception was not thrown on null client!");
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof NullClientException);
        }
    }

    @Test
    public void testCannotRemoveNonExistingClient() {
        try {
            Client client = Mockito.mock(Client.class);
            subscriptionService.removeSubscriber(client);
            Assert.fail("Exception was not thrown removing non existing client!");
        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof NonExistingClientException);
        }
    }

    @Test
    public void testCanRemoveExistingClient() {
        try {
            Client client = Mockito.mock(Client.class);
            subscriptionService.addSubscriber(client);
            Assert.assertEquals(1, subscriptionService.subscribers.size());
            subscriptionService.removeSubscriber(client);
            Assert.assertEquals(0, subscriptionService.subscribers.size());
        }
        catch (Exception e) {
            Assert.fail("Unexpected exception removing an existing client!");
        }
    }
    @Test
    public void testCanNotRemoveExistingClientTwice() {
        // Largely a copy of being able to remove a client but not a non existing one
        try {
            Client client = Mockito.mock(Client.class);
            subscriptionService.addSubscriber(client);
            Assert.assertEquals(1, subscriptionService.subscribers.size());
            subscriptionService.removeSubscriber(client);
            Assert.assertEquals(0, subscriptionService.subscribers.size());
            subscriptionService.removeSubscriber(client);
            Assert.fail("Able to remove the same client twice!");


        }
        catch (Exception e) {
            Assert.assertTrue(e instanceof NonExistingClientException);
        }
    }


    @Test
    public void testRemoveMultipleClients() {
        try {
            Vector<Client> clients = new Vector<Client>(MAX_CLIENT_NUMBER);
            for(int i = 0; i<MAX_CLIENT_NUMBER; i++) {
                clients.add(Mockito.mock(Client.class));
            }

            for(Client client:clients){
                subscriptionService.addSubscriber(client);
                Assert.assertTrue(subscriptionService.subscribers.contains(client));
            }

            Assert.assertEquals(MAX_CLIENT_NUMBER, subscriptionService.subscribers.size());


            for(Client client:clients){
                subscriptionService.removeSubscriber(client);
                Assert.assertFalse(subscriptionService.subscribers.contains(client));
            }

            Assert.assertTrue(subscriptionService.subscribers.isEmpty());
        }
        catch (Exception e) {
            Assert.fail("Unexpected exception thrown!");
        }
    }


    @Test
    public void testRemoveAllClients() {
        try {
            Vector<Client> clients = new Vector<Client>(MAX_CLIENT_NUMBER);
            for(int i = 0; i<MAX_CLIENT_NUMBER; i++) {
                clients.add(Mockito.mock(Client.class));
            }

            for(Client client:clients){
                subscriptionService.addSubscriber(client);
                Assert.assertTrue(subscriptionService.subscribers.contains(client));
            }

            Assert.assertEquals(MAX_CLIENT_NUMBER, subscriptionService.subscribers.size());


            for(Client client: subscriptionService.subscribers.toArray(new Client[0])){
                subscriptionService.removeSubscriber(client);
                Assert.assertFalse(subscriptionService.subscribers.contains(client));
            }

            Assert.assertTrue(subscriptionService.subscribers.isEmpty());
        }
        catch (Exception e) {
            Assert.fail("Unexpected exception thrown!");
        }
    }

    // Int


    @Test
    public void testClientMessage() {
        try {
            Client client = Mockito.mock(Client.class);
            Message message = Mockito.mock(Message.class);
            when(client.hasEmail()).thenReturn(true);
            subscriptionService.addSubscriber(client);
            Assert.assertTrue(subscriptionService.subscribers.contains(client));
            subscriptionService.sendMessage(message);

            verify(client, times(1)).receiveMessage(message);

        }
        catch (Exception e) {
            Assert.fail("Unexpected exception thrown!");
        }
    }
    @Test
    public void testClientMessageNoEmail() {
        try {
            Client client = Mockito.mock(Client.class);
            Message message = Mockito.mock(Message.class);
            when(client.hasEmail()).thenReturn(false);
            subscriptionService.addSubscriber(client);
            Assert.assertTrue(subscriptionService.subscribers.contains(client));
            subscriptionService.sendMessage(message);

            verify(client, times(0)).receiveMessage(message);

        }
        catch (Exception e) {
            Assert.fail("Unexpected exception thrown!");
        }
    }

    @Test
    public void testEmailMultipleClients() {
        try {

            Vector<Client> clients = new Vector<Client>(MAX_CLIENT_NUMBER);
            Message message = Mockito.mock(Message.class);

            for(int i = 0; i<MAX_CLIENT_NUMBER; i++) {
                Client client = Mockito.mock(Client.class);
                when(client.hasEmail()).thenReturn(true);
                clients.add(client);
            }

            for(Client client:clients){
                subscriptionService.addSubscriber(client);
                Assert.assertTrue(subscriptionService.subscribers.contains(client));
            }

            Assert.assertEquals(MAX_CLIENT_NUMBER, subscriptionService.subscribers.size());

            subscriptionService.sendMessage(message);
            for(Client client:subscriptionService.subscribers) {
                verify(client, times(1)).receiveMessage(message);
            }

        }
        catch (Exception e) {
            Assert.fail("Unexpected exception thrown!");
        }
    }


    @Test
    public void testDontMessageUnsubscribedClient() {
        try {
            Client client = Mockito.mock(Client.class);
            Message message = Mockito.mock(Message.class);
            when(client.hasEmail()).thenReturn(true);
            subscriptionService.addSubscriber(client);
            Assert.assertTrue(subscriptionService.subscribers.contains(client));
            subscriptionService.removeSubscriber(client);
            subscriptionService.sendMessage(message);

            verify(client, times(0)).receiveMessage(message);

        }
        catch (Exception e) {
            Assert.fail("Unexpected exception thrown!");
        }
    }
}

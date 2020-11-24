package es.grise.upm.profundizacion.tema8;

import org.junit.jupiter.api.*;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
    SubscriptionService subscriptionService;
    ArrayList<Client> subscribers;

    @BeforeEach
    public void setUp() {
        this.subscribers = mock(ArrayList.class);
        this.subscriptionService = new SubscriptionService();

        Whitebox.setInternalState(this.subscriptionService, "subscribers", this.subscribers);
    }

    @Test
    public void addNullClientTest() {
        assertThrows(NullClientException.class, () -> this.subscriptionService.addSubscriber(null));
    }

    @Test
    public void addClientTest() throws ExistingClientException, NullClientException {
        Client client = mock(Client.class);

        this.subscriptionService.addSubscriber(client);

        verify(this.subscribers).contains(client);
        verify(this.subscribers).add(client);
    }

    @Test
    public void addDuplicatedClientTest() {
        Client client = mock(Client.class);
        when(this.subscribers.contains(client)).thenReturn(true);

        assertThrows(ExistingClientException.class, () -> this.subscriptionService.addSubscriber(client));
    }

    @Test
    public void addMultipleClientTest() throws ExistingClientException, NullClientException {
        int inserts = 10;

        for (int i = 0; i < inserts; i++) {
            Client client = mock(Client.class);
            when(this.subscribers.contains(client)).thenReturn(false);
            this.subscriptionService.addSubscriber(client);
        }

        verify(this.subscribers, times(inserts)).contains(any());
        verify(this.subscribers, times(inserts)).add(any());
    }

    @Test
    public void removeNullClientTest() {
        assertThrows(NullClientException.class, () -> this.subscriptionService.removeSubscriber(null));
    }

    @Test
    public void removeMissingClientTest() {
        Client client = mock(Client.class);
        when(this.subscribers.contains(client)).thenReturn(false);

        assertThrows(NonExistingClientException.class, () -> this.subscriptionService.removeSubscriber(client));
    }

    @Test
    public void removeClientTest() throws NonExistingClientException, NullClientException {
        Client client = mock(Client.class);
        when(this.subscribers.contains(client)).thenReturn(true);

        this.subscriptionService.removeSubscriber(client);

        verify(this.subscribers).contains(client);
        verify(this.subscribers).remove(client);
    }

    @Test
    public void removeClientTwiceTest() throws NonExistingClientException, NullClientException {
        Client client = mock(Client.class);
        when(this.subscribers.contains(client)).thenReturn(true).thenReturn(false);

        this.subscriptionService.removeSubscriber(client);
        assertThrows(NonExistingClientException.class, () -> this.subscriptionService.removeSubscriber(client));

        verify(this.subscribers, times(2)).contains(client);
        verify(this.subscribers).remove(client);
    }


    @Test
    public void removeSomeClientsTest() throws ExistingClientException, NonExistingClientException, NullClientException {
        int inserts = 10;
        Client[] clients = new Client[inserts];

        for (int i = 0; i < inserts; i++) {
            Client client = mock(Client.class);
            when(this.subscribers.contains(client)).thenReturn(false);
            this.subscriptionService.addSubscriber(client);
            clients[i] = client;
        }


        for (int i = 0; i < inserts / 2; i++) {
            Client client = clients[i];
            when(this.subscribers.contains(client)).thenReturn(true);
            this.subscriptionService.removeSubscriber(client);
        }

        verify(this.subscribers, times(inserts + inserts / 2)).contains(any());
        verify(this.subscribers, times(inserts / 2)).remove(any());
    }

    @Test
    public void removeAllClientsTest() throws ExistingClientException, NonExistingClientException, NullClientException {
        int inserts = 10;
        Client[] clients = new Client[inserts];

        for (int i = 0; i < inserts; i++) {
            Client client = mock(Client.class);
            when(this.subscribers.contains(client)).thenReturn(false);
            this.subscriptionService.addSubscriber(client);
            clients[i] = client;
        }

        for (Client c : clients) {
            when(this.subscribers.contains(c)).thenReturn(true);
            this.subscriptionService.removeSubscriber(c);
        }

        verify(this.subscribers, times(inserts * 2)).contains(any());
        verify(this.subscribers, times(inserts)).remove(any());
    }

    @Test
    public void clientReceiveMessageTest() {
        Client client = mock(Client.class);
        Iterator<Client> iterator = mock(Iterator.class);

        when(this.subscribers.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(client);

        when(client.hasEmail()).thenReturn(true);

        Message message = mock(Message.class);
        this.subscriptionService.sendMessage(message);

        verify(client).receiveMessage(message);
        verify(client, times(1)).hasEmail();
    }

    @Test
    public void clientReceiveNoMessageTest() {
        Client client = mock(Client.class);
        Iterator<Client> iterator = mock(Iterator.class);

        when(this.subscribers.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(client);

        when(client.hasEmail()).thenReturn(false);

        Message message = mock(Message.class);
        this.subscriptionService.sendMessage(message);

        verify(client, times(0)).receiveMessage(message);
        verify(client, times(1)).hasEmail();
    }

    @Test
    public void clientsReceiveMessageTest() {
        Client client = mock(Client.class);
        Iterator<Client> iterator = mock(Iterator.class);

        when(this.subscribers.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn(client);

        when(client.hasEmail()).thenReturn(true, false, true);

        Message message = mock(Message.class);
        this.subscriptionService.sendMessage(message);

        verify(client, times(2)).receiveMessage(message);
        verify(client, times(3)).hasEmail();
    }

    @Test
    public void removeClientsReceiveNoMessageTest() {
        Client client = mock(Client.class);
        Iterator<Client> iterator = mock(Iterator.class);

        when(this.subscribers.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(false);
        when(iterator.next()).thenReturn(client);

        when(client.hasEmail()).thenReturn(true);

        Message message = mock(Message.class);
        this.subscriptionService.sendMessage(message);

        verify(client, times(0)).receiveMessage(message);
        verify(client, times(0)).hasEmail();
    }
}

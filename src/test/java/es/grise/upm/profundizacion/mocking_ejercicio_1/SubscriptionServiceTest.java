package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionServiceTest {

  private SubscriptionService subscriptionService;

  @Before
  public void setup() {
    subscriptionService = new SubscriptionService();
  }

  @Test(expected = NullClientException.class)
  public void addNullClient_throwsNullClientException() throws NullClientException, ExistingClientException {
    subscriptionService.addSubscriber(null);
  }

  @Test
  public void addClient_clientInSubscribers() throws NullClientException, ExistingClientException {
    Client client = mock(Client.class);
    subscriptionService.addSubscriber(client);
    assertTrue(subscriptionService.subscribers.contains(client));
  }

  @Test(expected = ExistingClientException.class)
  public void addExistingClient_throwsExistingClientException() throws NullClientException, ExistingClientException {
    Client client = mock(Client.class);
    subscriptionService.addSubscriber(client);
    subscriptionService.addSubscriber(client);
  }

  @Test
  public void addSeveralClients_clientsInSubscribers() throws NullClientException, ExistingClientException {
    Client client1 = mock(Client.class);
    Client client2 = mock(Client.class);
    Client client3 = mock(Client.class);
    subscriptionService.addSubscriber(client1);
    subscriptionService.addSubscriber(client2);
    subscriptionService.addSubscriber(client3);
    assertEquals(3, subscriptionService.subscribers.size());
  }

  @Test(expected = NullClientException.class)
  public void removeNullSubscriber_throwsNullClientException() throws NullClientException, NonExistingClientException {
    subscriptionService.removeSubscriber(null);
  }

  @Test(expected = NonExistingClientException.class)
  public void removeClientNotSubscribed_throwsNonExistingClientException() throws NullClientException, NonExistingClientException {
    Client client = mock(Client.class);
    subscriptionService.removeSubscriber(client);
  }

  @Test
  public void removeClientCorrectly() throws NullClientException, ExistingClientException, NonExistingClientException {
    Client client = mock(Client.class);
    subscriptionService.addSubscriber(client);
    subscriptionService.removeSubscriber(client);
    assertEquals(0, subscriptionService.subscribers.size());
  }

  @Test(expected = NonExistingClientException.class)
  public void removeSubscriberTwice_throwsNonExistingClientException() throws NullClientException, ExistingClientException, NonExistingClientException {
    Client client = mock(Client.class);
    subscriptionService.addSubscriber(client);
    subscriptionService.removeSubscriber(client);
    subscriptionService.removeSubscriber(client);
  }

  @Test
  public void removeSeveralClientsCorrectly() throws NullClientException, ExistingClientException, NonExistingClientException {
    Client client1 = mock(Client.class);
    Client client2 = mock(Client.class);
    Client client3 = mock(Client.class);
    subscriptionService.addSubscriber(client1);
    subscriptionService.addSubscriber(client2);
    subscriptionService.addSubscriber(client3);

    subscriptionService.removeSubscriber(client1);
    subscriptionService.removeSubscriber(client3);
    assertEquals(1, subscriptionService.subscribers.size());
  }

  @Test
  public void removeAllClientsCorrectly() throws NullClientException, ExistingClientException, NonExistingClientException {
    Client client1 = mock(Client.class);
    Client client2 = mock(Client.class);
    Client client3 = mock(Client.class);
    subscriptionService.addSubscriber(client1);
    subscriptionService.addSubscriber(client2);
    subscriptionService.addSubscriber(client3);

    subscriptionService.removeSubscriber(client1);
    subscriptionService.removeSubscriber(client2);
    subscriptionService.removeSubscriber(client3);
    assertTrue(subscriptionService.subscribers.isEmpty());
  }
	
  @Test
  public void subscriberReceivesMessagesIfTheyHaveEmail() throws NullClientException, ExistingClientException {
    Client client = mock(Client.class);
    when(client.hasEmail()).thenReturn(true);
    subscriptionService.addSubscriber(client);
    subscriptionService.sendMessage(mock(Message.class));
    verify(client).receiveMessage(any());
  }

  @Test
  public void subscriberDoesNotReceiveMessagesIfTheyDoNotHaveEmail() throws NullClientException, ExistingClientException {
    Client client = mock(Client.class);
    when(client.hasEmail()).thenReturn(false);
    subscriptionService.addSubscriber(client);
    subscriptionService.sendMessage(mock(Message.class));
    verify(client, never()).receiveMessage(any());
  }

  @Test
  public void severalSubscribersReceivesMessagesIfTheyHaveEmail() throws NullClientException, ExistingClientException {
    Client client1 = mock(Client.class);
    Client client2 = mock(Client.class);
    Client client3 = mock(Client.class);
    when(client1.hasEmail()).thenReturn(true);
    when(client2.hasEmail()).thenReturn(false);
    when(client3.hasEmail()).thenReturn(true);
    subscriptionService.addSubscriber(client1);
    subscriptionService.addSubscriber(client2);
    subscriptionService.addSubscriber(client3);
    subscriptionService.sendMessage(mock(Message.class));
    verify(client1).receiveMessage(any());
    verify(client2, never()).receiveMessage(any());
    verify(client3).receiveMessage(any());
  }

  @Test
  public void unsubscribedClientDoesNotReceiveMessages() throws NullClientException, ExistingClientException, NonExistingClientException {
    Client client = mock(Client.class);
    subscriptionService.addSubscriber(client);
    subscriptionService.removeSubscriber(client);
    subscriptionService.sendMessage(mock(Message.class));
    verify(client, never()).receiveMessage(any());
  }
}

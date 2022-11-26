package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.*;

public class SubscriptionServiceTest {

  Client client, client2, client3;
  SubscriptionService sub;

  @BeforeEach
  public void setUp() {
      sub = new SubscriptionService();
      client = mock(Client.class);
      client2 = mock(Client.class);
      client3 = mock(Client.class);
  }

	@Test       
  public void testAddNullClientToListTest(){
    assertThrows(NullClientException.class, ()->{
      sub.addSubscriber(null);
    });
  }

  @Test
  public void addClientToListTest() throws NullClientException, ExistingClientException{
    sub.addSubscriber(client);
    assertTrue(sub.subscribers.contains(client));
  }

  @Test
  public void addClientTwiceToListTest() throws NullClientException{
    assertThrows(ExistingClientException.class, ()->{
      sub.addSubscriber(client);
      sub.addSubscriber(client);
    });
  }

  @Test
  public void addMultipleClientsToListTest() throws NullClientException, ExistingClientException{
    sub.addSubscriber(client);
    sub.addSubscriber(client2);
    sub.addSubscriber(client3);
    assertTrue(sub.subscribers.contains(client) && sub.subscribers.contains(client2) && sub.subscribers.contains(client3));
  }

  @Test
  public void cannotRemoveNullClientFromListTest() throws NonExistingClientException{
    sub.subscribers.add(null);
    assertThrows(NullClientException.class, ()->{
      sub.removeSubscriber(null);
    });
  }

  @Test
  public void cannotRemoveNotExistingClientFromListTest() throws NullClientException{
    sub.subscribers.add(client);
    assertThrows(NonExistingClientException.class, ()->{
      sub.removeSubscriber(client2);
    });
  }

  @Test
  public void removeClientFromListTest() throws NonExistingClientException, NullClientException{
    sub.subscribers.add(client);
    sub.removeSubscriber(client);
    assertFalse(sub.subscribers.contains(client));
  }

  @Test
  public void cannotRemoveClientTwiceFromListTest() throws NullClientException{
    sub.subscribers.add(client);
    assertThrows(NonExistingClientException.class, ()->{
      sub.removeSubscriber(client);
      sub.removeSubscriber(client);
    });
  }

  @Test
  public void removeMultipleClientsFromListTest() throws NonExistingClientException, NullClientException{
    sub.subscribers.add(client);
    sub.subscribers.add(client2);
    sub.subscribers.add(client3);
    sub.removeSubscriber(client);
    sub.removeSubscriber(client2);
    assertTrue(!sub.subscribers.contains(client) && !sub.subscribers.contains(client2) && sub.subscribers.contains(client3));
  }

  @Test
  public void removeAllClientsFromListTest() throws NonExistingClientException, NullClientException{
    sub.subscribers.add(client);
    sub.subscribers.add(client2);
    sub.subscribers.add(client3);
    sub.removeSubscriber(client);
    sub.removeSubscriber(client2);
    sub.removeSubscriber(client3);
    assertTrue(sub.subscribers.isEmpty());
  }

  @Test
  public void subscribedClientWithEmailReceivesMsgTest(){
    Message m = mock(Message.class);
    when(client.hasEmail()).thenReturn(true);
    sub.subscribers.add(client);
    sub.sendMessage(m);
    verify(client).receiveMessage(m);
  }

  @Test
  public void subscribedClientWithoutEmailDoesNotReceivesMsgTest(){
    Message m = mock(Message.class);
    when(client.hasEmail()).thenReturn(false);
    sub.subscribers.add(client);
    sub.sendMessage(m);
    verify(client, times(0)).receiveMessage(m);
  }

  @Test
  public void MultipleSubscribedClientsWithEmailReceivesMsgTest(){
    Message m = mock(Message.class);
    when(client.hasEmail()).thenReturn(true);
    when(client2.hasEmail()).thenReturn(true);
    when(client3.hasEmail()).thenReturn(false);
    sub.subscribers.add(client);
    sub.subscribers.add(client2);
    sub.subscribers.add(client3);
    sub.sendMessage(m);
    verify(client).receiveMessage(m);
    verify(client2).receiveMessage(m);
    verify(client3, times(0)).receiveMessage(m);
  }

  @Test
  public void unsubscribedClientWithEmailDoesNotReceivesMsgTest() throws NullClientException, NonExistingClientException{
    Message m = mock(Message.class);
    when(client.hasEmail()).thenReturn(false);
    sub.subscribers.add(client);
    sub.removeSubscriber(client);
    sub.sendMessage(m);
    verify(client, times(0)).receiveMessage(m);
  }
}

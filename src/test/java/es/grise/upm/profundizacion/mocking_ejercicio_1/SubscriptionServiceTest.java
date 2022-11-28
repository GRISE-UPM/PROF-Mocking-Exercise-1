package es.grise.upm.profundizacion.mocking_ejercicio_1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SubscriptionServiceTest {

  SubscriptionService subscriptionService;
  Client client1;
  Client client2;
  Message message;

  @BeforeAll
  public static void beforeAll() {
    System.out.println("Empiezan tests");
  }

  @BeforeEach
  public void beforeEach() {
    client1 = mock(Client.class);
    client2 = mock(Client.class);
    message = mock(Message.class);
    subscriptionService = new SubscriptionService();
    System.out.println("Empieza test");
  }

  @AfterEach
  public void afterEach() {
    System.out.println("Test terminado");
  }

  @AfterAll
  public static void afterAll() {
    System.out.println("Terminan tests");
  }

  // STATE TESTS

  @Test
  public void addNullClientTest() {
    assertThrows(
      NullClientException.class,
      () -> subscriptionService.addSubscriber(null)
    );
  }

  @Test
  public void addNewClientTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    assertEquals(true, subscriptionService.subscribers.contains(client1));
  }

  @Test
  public void addSameClientTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    assertThrows(
      ExistingClientException.class,
      () -> subscriptionService.addSubscriber(client1)
    );
  }

  @Test
  public void addMultipleClientsTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    subscriptionService.addSubscriber(client2);
    assertEquals(true, subscriptionService.subscribers.contains(client1));
    assertEquals(true, subscriptionService.subscribers.contains(client2));
  }

  @Test
  public void removeNullClientTest() {
    assertThrows(
      NullClientException.class,
      () -> subscriptionService.removeSubscriber(null)
    );
  }

  @Test
  public void removeNotStoredClientTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    assertThrows(
      NonExistingClientException.class,
      () -> subscriptionService.removeSubscriber(client2)
    );
  }

  @Test
  public void removeStoredClientTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    subscriptionService.removeSubscriber(client1);
    assertEquals(false, subscriptionService.subscribers.contains(client1));
  }

  @Test
  public void removeStoredClientTwiceTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    subscriptionService.removeSubscriber(client1);
    assertThrows(
      NonExistingClientException.class,
      () -> subscriptionService.removeSubscriber(client1)
    );
  }

  @Test
  public void removeMultipleStoredClientsTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    subscriptionService.addSubscriber(client2);
    subscriptionService.removeSubscriber(client1);
    subscriptionService.removeSubscriber(client2);
    assertEquals(false, subscriptionService.subscribers.contains(client1));
    assertEquals(false, subscriptionService.subscribers.contains(client2));
  }

  // INTERACTION TESTS

  @Test
  public void clientReceiveMessageTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    when(client1.hasEmail()).thenReturn(true);
    subscriptionService.sendMessage(message);
    verify(client1).receiveMessage(message);
  }

  @Test
  public void clientWithoutEmailDoesntReceiveMessageTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    when(client1.hasEmail()).thenReturn(false);
    subscriptionService.sendMessage(message);
    verify(client1, times(0)).receiveMessage(message);
  }

  @Test
  public void multipleClientsReceiveMessageTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    subscriptionService.addSubscriber(client2);
    when(client1.hasEmail()).thenReturn(true);
    when(client2.hasEmail()).thenReturn(true);
    subscriptionService.sendMessage(message);
    verify(client1).receiveMessage(message);
    verify(client2).receiveMessage(message);
  }

  @Test
  public void unsubscribedClientDoesntReceiveMessageTest() throws Exception {
    subscriptionService.addSubscriber(client1);
    subscriptionService.removeSubscriber(client1);
    when(client1.hasEmail()).thenReturn(true);
    subscriptionService.sendMessage(message);
    verify(client1, times(0)).receiveMessage(message);
  }
}

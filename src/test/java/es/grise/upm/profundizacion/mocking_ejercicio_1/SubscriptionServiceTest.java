package es.grise.upm.profundizacion.mocking_ejercicio_1;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Create the mocks based on the @Mock annotation -> https://www.vogella.com/tutorials/Mockito/article.html
@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

//  https://stackoverflow.com/questions/16467685/difference-between-mock-and-injectmocks
    @InjectMocks
    SubscriptionService subscriptionService;

    @Mock
    Client clientMock;

    @Mock
    Message messageMock;

    @BeforeAll
    static public void beforeAll(){
        System.out.println("Starting Ex8 Mockito tests");
    }

    @BeforeEach
    public void init(TestInfo testInfo){
        subscriptionService = new SubscriptionService();
        System.out.println("Start... " + testInfo.getDisplayName());
    }

    // State tests
    @DisplayName("Test1: No se puede añadir un Client null a la lista subscribers.")
    @Test
    public void addNullClientToSubsList(){
        assertThrows(NullClientException.class, () -> subscriptionService.addSubscriber(null));
    }

    @DisplayName("Test2: Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.")
    @Test
    public void addClientToSubsList() throws NullClientException, ExistingClientException {
        clientMock = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(clientMock);
        assertTrue(subscriptionService.subscribers.contains(clientMock));
    }

    @DisplayName("Test3: No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.")
    @Test
    public void addClientTwice() throws NullClientException, ExistingClientException {
        clientMock = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(clientMock);
        assertThrows(ExistingClientException.class, () -> subscriptionService.addSubscriber(clientMock));
    }

    @DisplayName("Test4: Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.")
    @Test
    public void addClientsToSubList() throws NullClientException, ExistingClientException {
        Client clientMock1 = Mockito.mock(Client.class);
        Client clientMock2 = Mockito.mock(Client.class);
        Client clientMock3 = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(clientMock1);
        subscriptionService.addSubscriber(clientMock2);
        subscriptionService.addSubscriber(clientMock3);

        assertTrue(subscriptionService.subscribers.contains(clientMock1) && subscriptionService.subscribers.contains(clientMock2) && subscriptionService.subscribers.contains(clientMock3));
        assertEquals(3, subscriptionService.subscribers.size());

    }

    @DisplayName("Test5: No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.")
    @Test
    public void removeNullClient() {
        assertThrows(NullClientException.class, () -> subscriptionService.removeSubscriber(null));
    }

    @DisplayName("Test6: No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
    @Test
    public void removeNotExistingClient() {
        clientMock = Mockito.mock(Client.class);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(clientMock));
    }

    @DisplayName("Test7: Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.")
    @Test
    public void removeClientInSubscriberList() throws NullClientException, ExistingClientException, NonExistingClientException {
        clientMock = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(clientMock);
        assertTrue(subscriptionService.subscribers.contains(clientMock));
        subscriptionService.removeSubscriber(clientMock);
        assertFalse(subscriptionService.subscribers.contains(clientMock));
        assertEquals(0, subscriptionService.subscribers.size());
    }

    @DisplayName("Test8: No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
    @Test
    public void removeClientFromListTwice() throws NullClientException, ExistingClientException, NonExistingClientException {
        clientMock = Mockito.mock(Client.class);
        subscriptionService.addSubscriber(clientMock);
        assertTrue(subscriptionService.subscribers.contains(clientMock));
        subscriptionService.removeSubscriber(clientMock);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(clientMock));
    }

    @DisplayName("Test9: Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.")
    @Test
    public void removeMultipleClientsFromSubsList() throws NullClientException, ExistingClientException, NonExistingClientException {
    // Create 5 clients
    Client clientMock1 = Mockito.mock(Client.class);
    Client clientMock2 = Mockito.mock(Client.class);
    Client clientMock3 = Mockito.mock(Client.class);
    Client clientMock4 = Mockito.mock(Client.class);
    Client clientMock5 = Mockito.mock(Client.class);
    // Add all clients to the list
    subscriptionService.addSubscriber(clientMock1);
    subscriptionService.addSubscriber(clientMock2);
    subscriptionService.addSubscriber(clientMock3);
    subscriptionService.addSubscriber(clientMock4);
    subscriptionService.addSubscriber(clientMock5);
    assertEquals(5, subscriptionService.subscribers.size());
        // Remove 3 clients from the list
    subscriptionService.removeSubscriber(clientMock1);
    subscriptionService.removeSubscriber(clientMock3);
    subscriptionService.removeSubscriber(clientMock5);
    // Check clients in list
    assertTrue(subscriptionService.subscribers.contains(clientMock2));
    assertTrue(subscriptionService.subscribers.contains(clientMock4));
    assertFalse(subscriptionService.subscribers.contains(clientMock1));
    assertFalse(subscriptionService.subscribers.contains(clientMock3));
    assertFalse(subscriptionService.subscribers.contains(clientMock5));
    // Check list size
    assertEquals(2, subscriptionService.subscribers.size());
    }

    @DisplayName("Test10: Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.")
    @Test
    public void removeAllClientsFromSubsList() throws NullClientException, ExistingClientException, NonExistingClientException {
        // Create 5 clients
        Client clientMock1 = Mockito.mock(Client.class);
        Client clientMock2 = Mockito.mock(Client.class);
        Client clientMock3 = Mockito.mock(Client.class);
        Client clientMock4 = Mockito.mock(Client.class);
        Client clientMock5 = Mockito.mock(Client.class);
        // Add all clients to the list
        subscriptionService.addSubscriber(clientMock1);
        subscriptionService.addSubscriber(clientMock2);
        subscriptionService.addSubscriber(clientMock3);
        subscriptionService.addSubscriber(clientMock4);
        subscriptionService.addSubscriber(clientMock5);
        assertEquals(5, subscriptionService.subscribers.size());
        // Remove 3 clients from the list
        subscriptionService.removeSubscriber(clientMock1);
        subscriptionService.removeSubscriber(clientMock2);
        subscriptionService.removeSubscriber(clientMock3);
        subscriptionService.removeSubscriber(clientMock4);
        subscriptionService.removeSubscriber(clientMock5);
        // Check clients in list
        assertFalse(subscriptionService.subscribers.contains(clientMock1));
        assertFalse(subscriptionService.subscribers.contains(clientMock2));
        assertFalse(subscriptionService.subscribers.contains(clientMock3));
        assertFalse(subscriptionService.subscribers.contains(clientMock4));
        assertFalse(subscriptionService.subscribers.contains(clientMock5));
        // Check list size
        assertEquals(0, subscriptionService.subscribers.size());
    }

    //    Interaction Tests
    @DisplayName("Test11: Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).")
    @Test
    public void clientReceiveMessagesWithEmail() throws NullClientException, ExistingClientException {
        clientMock = Mockito.mock(Client.class);
        messageMock = Mockito.mock(Message.class);
        when(clientMock.hasEmail()).thenReturn(true);
        subscriptionService.addSubscriber(clientMock);
        subscriptionService.sendMessage(messageMock);
        verify(clientMock, times(1)).receiveMessage(messageMock);
    }

    @DisplayName("Test12: Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).")
    @Test
    public void clientReceiveMessagesWithNoEmail() throws NullClientException, ExistingClientException {
        clientMock = Mockito.mock(Client.class);
        messageMock = Mockito.mock(Message.class);
        when(clientMock.hasEmail()).thenReturn(false);
        subscriptionService.addSubscriber(clientMock);
        subscriptionService.sendMessage(messageMock);
        verify(clientMock, times(0)).receiveMessage(messageMock);
    }

    @DisplayName("Test13: Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).")
    @Test
    public void multipleClientsMessagesWithEmail() throws NullClientException, ExistingClientException {
        messageMock = Mockito.mock(Message.class);
        // Create clients
        clientMock = Mockito.mock(Client.class);
        Client clientMock2 = Mockito.mock(Client.class);
        Client clientMock3 = Mockito.mock(Client.class);
        // Set method hasEmail method response for each client
        when(clientMock.hasEmail()).thenReturn(true);
        when(clientMock2.hasEmail()).thenReturn(true);
        when(clientMock3.hasEmail()).thenReturn(false);
        // Add clients to subscription list
        subscriptionService.addSubscriber(clientMock);
        subscriptionService.addSubscriber(clientMock2);
        subscriptionService.addSubscriber(clientMock3);
        // Send message
        subscriptionService.sendMessage(messageMock);
        // Verify that only clients with hasEmail() == true receive the message
        verify(clientMock, times(1)).receiveMessage(messageMock);
        verify(clientMock2, times(1)).receiveMessage(messageMock);
        verify(clientMock3, times(0)).receiveMessage(messageMock);
    }

    @DisplayName("Test14: Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).")
    @Test
    public void multipleClientsMessagesWithNoEmail() throws NullClientException, ExistingClientException, NonExistingClientException {
        messageMock = Mockito.mock(Message.class);
        // Create clients
        clientMock = Mockito.mock(Client.class);
        Client clientMock2 = Mockito.mock(Client.class);
        Client clientMock3 = Mockito.mock(Client.class);
        // Set method hasEmail method response for each client

        // Some clients removed -> https://www.baeldung.com/mockito-unnecessary-stubbing-exception
        // when(clientMock.hasEmail()).thenReturn(true);
        when(clientMock2.hasEmail()).thenReturn(true);
        // when(clientMock3.hasEmail()).thenReturn(true);

        // Add clients to subscription list
        subscriptionService.addSubscriber(clientMock);
        subscriptionService.addSubscriber(clientMock2);
        subscriptionService.addSubscriber(clientMock3);
        // Unsubscribe clientMock and clientMock3
        subscriptionService.removeSubscriber(clientMock);
        subscriptionService.removeSubscriber(clientMock3);
        // Send message
        subscriptionService.sendMessage(messageMock);
        // clientMock2 should receive the message
        verify(clientMock, times(0)).receiveMessage(messageMock);
        verify(clientMock2, times(1)).receiveMessage(messageMock);
        verify(clientMock3, times(0)).receiveMessage(messageMock);
    }


    @AfterEach
    public void tearDown(TestInfo testInfo) {
        System.out.println("Finished... " + testInfo.getDisplayName());
    }

    @AfterAll
    static public void closeSubscriptionServiceTest(){
        System.out.println("Finished Mockito tests");
    }

}

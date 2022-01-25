package es.grise.upm.profundizacion.mocking_ejercicio_1;

import org.junit.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
	@InjectMocks
    SubscriptionService subscriptionService;

    @Before
    public void inicioSubsriptionService() {
        subscriptionService = new SubscriptionService();
    }
    //Primer test: No se puede añadir un Client null a la lista subscribers.
    @Test
    public void test1() throws NullClientException, ExistingClientException {
        assertThrows(NullClientException.class, () -> subscriptionService.addSubscriber(null));
    }
    //Segundo Test: Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
    @Test
    public void test2() throws NullClientException, ExistingClientException {
        Client client1= mock(Client.class);
        subscriptionService.addSubscriber(client1);
        assertEquals(1, subscriptionService.subscribers.size());
    }
    //Tercer test: No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.
    @Test
    public void test3() throws NullClientException, ExistingClientException {
        Client client1= mock(Client.class);
        subscriptionService.addSubscriber(client1);
        assertThrows(ExistingClientException.class, () -> subscriptionService.addSubscriber(client1));
    }
    //CuartoTest: Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.
    @Test
    public void test4() throws NullClientException, ExistingClientException {
        Client client1= mock(Client.class);
        Client client2= mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        assertEquals(2, subscriptionService.subscribers.size());
    }
    //Quinto test: No se puede eliminar usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
    @Test
    public void test5() throws NullClientException, NonExistingClientException, ExistingClientException {
        assertThrows(NullClientException.class, () -> subscriptionService.removeSubscriber(null));
    }
    //Sexto test: No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
    @Test
    public void test6() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client1));
    }
    //Septimo test: Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers.
    @Test
    public void test7() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.removeSubscriber(client1);
        assertEquals(0, subscriptionService.subscribers.size());
    }
    //Octavo Test: No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
    @Test
    public void test8() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.removeSubscriber(client1);
        assertThrows(NonExistingClientException.class, () -> subscriptionService.removeSubscriber(client1));
    }
    //Noveno test: Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.
    @Test
    public void test9() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        Client client2= mock(Client.class);
        Client client3= mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        assertEquals(1, subscriptionService.subscribers.size());
    }
    //Decimo Test: Se pueden eliminar correctamente (usando removeSubscriber() todos los Client almacenados en la lista subscribers.
    @Test
    public void test10() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        Client client2= mock(Client.class);
        Client client3= mock(Client.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        subscriptionService.addSubscriber(client3);
        subscriptionService.removeSubscriber(client1);
        subscriptionService.removeSubscriber(client2);
        subscriptionService.removeSubscriber(client3);
        assertEquals(0, subscriptionService.subscribers.size());
    }
    //Undecimo test: Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).
    @Test
    public void test11() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        Message mensaje = mock(Message.class);
        subscriptionService.addSubscriber(client1);
        when(client1.hasEmail()).thenReturn(true);
        subscriptionService.sendMessage(mensaje);
        verify(client1).receiveMessage(any());
    }
    //Duocedimo test: Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false).
    @Test
    public void test12() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        Message mensaje = mock(Message.class);
        subscriptionService.addSubscriber(client1);
        when(client1.hasEmail()).thenReturn(false);
        subscriptionService.sendMessage(mensaje);
        verify(client1, never()).receiveMessage(any());
    }
    //Decimotercer test: Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).
    @Test
    public void test13() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        Client client2= mock(Client.class);
        Message mensaje1 = mock(Message.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.addSubscriber(client2);
        when(client1.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(false);
        subscriptionService.sendMessage(mensaje1);
        verify(client1).receiveMessage(any());
        verify(client2, never()).receiveMessage(any());
    }
    //Decimocuarto test: Al des-suscribir un Client éste no recibe mensajes (método receiveMessage()).
    @Test
    public void test14() throws NullClientException, NonExistingClientException, ExistingClientException {
        Client client1= mock(Client.class);
        Message mensaje1 = mock(Message.class);
        subscriptionService.addSubscriber(client1);
        subscriptionService.removeSubscriber(client1);
        when(client1.hasEmail()).thenReturn(false);
        subscriptionService.sendMessage(mensaje1);
        verify(client1, never()).receiveMessage(any());
    }




}

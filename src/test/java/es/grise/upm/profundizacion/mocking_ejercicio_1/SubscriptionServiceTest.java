package es.grise.upm.profundizacion.mocking_ejercicio_1;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {

    Client client;
    Message message;
    SubscriptionService subscriptionService;


    @BeforeEach
    public void beforeEach() {
        this.subscriptionService = new SubscriptionService();
    }



    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // State Tests
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    @DisplayName("No se puede añadir un Client null a la lista subscribers.")
    public void noAnhadeClienteNullTest() {
        try {
            this.subscriptionService.addSubscriber(null);
        } catch (NullClientException e) {
            return;
        } catch (ExistingClientException e) {
            Assertions.fail();
        }
        Assertions.fail();
    }

    @Test
    @DisplayName("Al añadir un Clientmediante addSubscriber(), éste Client se almacena en la lista subscribers.")
    public void addSuscriberHappyPath() {
        client = mock(Client.class);

        try {
            this.subscriptionService.addSubscriber(client);
        } catch (Exception e) {
            Assertions.fail();
        }
        Assertions.assertTrue(this.subscriptionService.subscribers.contains(client));

    }


    @Test
    @DisplayName("No se puede añadir dos veces el mismo Client mediante addSubscriber() a la lista subscribers Al hacerlo, se lanza la excepción ExistingClientException.")
    public void anhadirDoble() {
        client = mock(Client.class);

        try {
            this.subscriptionService.addSubscriber(client);
        } catch (Exception e) {
            Assertions.fail();
        }

        try {
            this.subscriptionService.addSubscriber(client);
        } catch (ExistingClientException ignored) {
        } catch (NullClientException n) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Al añadir varios Client mediante addSubscriber(), todos los Clientse almacenan en la lista subscribers.")
    public void addVariosSuscriberHappyPath() {
        Client client1 = mock(Client.class);
        Client client3 = mock(Client.class);
        Client client2 = mock(Client.class);

        try {
            this.subscriptionService.addSubscriber(client1);
            this.subscriptionService.addSubscriber(client2);
            this.subscriptionService.addSubscriber(client3);
        } catch (Exception e) {
            Assertions.fail();
        }

        for (Client c : this.subscriptionService.subscribers) {
            if (!c.equals(client1) && !c.equals(client2) && !c.equals(client3))
                Assertions.fail("No se han almacenado todos los client");
        }

    }


    @Test
    @DisplayName("No se puede eliminar (usando removeSubscriber() un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.")
    public void eliminarClientNull() {
        try {
            this.subscriptionService.removeSubscriber(null);
        } catch (NullClientException ignored) {
        } catch (NonExistingClientException e) {
            Assertions.fail();
        }
    }


    @Test
    @DisplayName("No se puede eliminar (usando removeSubscriber() un Client que no está almacenado en la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
    public void eliminarClientInexistente() {
        client = mock(Client.class);

        try {
            this.subscriptionService.removeSubscriber(client);
        } catch (NullClientException n) {
            Assertions.fail();
        } catch (NonExistingClientException ignored) {
        }
    }


    @Test
    @DisplayName("Se puede eliminar correctamente (usando removeSubscriber() un Client almacenado en la lista subscribers")
    public void eliminarHappyPath() {
        client = mock(Client.class);

        try {
            this.subscriptionService.addSubscriber(client);
        } catch (Exception e) {
            Assertions.fail();
        }

        try {
            this.subscriptionService.removeSubscriber(client);
        } catch (Exception n) {
            Assertions.fail();
        }

        Assertions.assertTrue(this.subscriptionService.subscribers.isEmpty());
    }


    @Test
    @DisplayName("No se puede eliminar (usando removeSubscriber() dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.")
    public void eliminarDosVeces() {
        client = mock(Client.class);

        try {
            this.subscriptionService.addSubscriber(client);
            this.subscriptionService.removeSubscriber(client);
        } catch (Exception e) {
            Assertions.fail();
        }

        try {
            this.subscriptionService.removeSubscriber(client);
        } catch (NonExistingClientException ignored) {
        } catch (NullClientException e) {
            Assertions.fail();
        }

    }


    @Test
    @DisplayName("Se pueden eliminar correctamente (usando removeSubscriber() varios Client almacenados en la lista subscribers.")
    public void eliminarVariasVeces() {
        client = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);

        try {
            this.subscriptionService.addSubscriber(client);
            this.subscriptionService.addSubscriber(client2);
            this.subscriptionService.addSubscriber(client3);
        } catch (Exception e) {
            Assertions.fail();
        }

        try {
            this.subscriptionService.removeSubscriber(client);
            this.subscriptionService.removeSubscriber(client2);
            this.subscriptionService.removeSubscriber(client3);
        } catch (Exception e) {
            Assertions.fail();
        }

    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Interaction Tests
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    @DisplayName("Un Client suscrito recibe mensajes (método receiveMessage() si tiene email (método hasEmail() == true).")
    public void clientRecibeMessage() {
        client = mock(Client.class);
        message = mock(Message.class);
        when(client.hasEmail()).thenReturn(true);

        try {
            this.subscriptionService.addSubscriber(client);
            this.subscriptionService.sendMessage(message);
        } catch (Exception e) {
            Assertions.fail();
        }

        verify(client, times(1)).receiveMessage(message);
    }


    @Test
    @DisplayName("Un Client suscrito no recibe mensajes (método receiveMessage() si no tiene email (método hasEmail() == false)")
    public void clientNoRecibeMessage() {
        client = mock(Client.class);
        message = mock(Message.class);
        when(client.hasEmail()).thenReturn(false);

        try {
            this.subscriptionService.addSubscriber(client);
            this.subscriptionService.sendMessage(message);
        } catch (Exception e) {
            Assertions.fail();
        }

        verify(client, never()).receiveMessage(message);
    }


    @Test
    @DisplayName("Varios  Client suscritos reciben mensajes (método receiveMessage() si tienen email (método hasEmail() == true).")
    public void variosClientRecibeMessage() {
        client = mock(Client.class);
        Client client2 = mock(Client.class);
        Client client3 = mock(Client.class);
        message = mock(Message.class);
        when(client.hasEmail()).thenReturn(true);
        when(client2.hasEmail()).thenReturn(true);
        when(client3.hasEmail()).thenReturn(true);

        try {
            this.subscriptionService.addSubscriber(client);
            this.subscriptionService.addSubscriber(client2);
            this.subscriptionService.addSubscriber(client3);
            this.subscriptionService.sendMessage(message);
        } catch (Exception e) {
            Assertions.fail();
        }

        verify(client, times(1)).receiveMessage(message);
        verify(client2, times(1)).receiveMessage(message);
        verify(client3, times(1)).receiveMessage(message);
    }


    @Test
    @DisplayName("Al des-suscribir un Client éste no recibe mensajes (método receiveMessage())")
    public void mensajeAUnDessuscrito() {
        client = mock(Client.class);
        message = mock(Message.class);
        when(client.hasEmail()).thenReturn(false);

        try {
            this.subscriptionService.addSubscriber(client);
            this.subscriptionService.removeSubscriber(client);
            this.subscriptionService.sendMessage(message);
        } catch (Exception e) {
            Assertions.fail();
        }

        verify(client, never()).receiveMessage(message);

    }


}

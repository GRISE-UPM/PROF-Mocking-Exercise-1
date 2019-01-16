package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class SubscriptionServiceTest {

	
    // STATE TEST:
    //No se puede añadir un Client null a la lista subscribers.
    @Test(expected = NullClientException.class)
    public void añadirClienteNuloTest() throws ExistingClientException, NullClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = null;
        subService.addSubscriber(clienteTEST);}
	
    
	// STATE TEST:
    //Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
    @Test
    public void añadirClienteTest() throws ExistingClientException, NullClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        subService.addSubscriber(clienteTEST);
        assertEquals(subService.subscribers.size(),1);}

    
	// STATE TEST:
    /*	No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers
    	Al hacerlo, se lanza la excepción ExistingClientException.*/
    @Test(expected = ExistingClientException.class)
    public void adiciónDuplicadaTest() throws ExistingClientException, NullClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        subService.addSubscriber(clienteTEST);
        subService.addSubscriber(clienteTEST);}


	// STATE TEST:
    //Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
    @Test
    public void añadirVariosClientesTest() throws ExistingClientException, NullClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTESTA = mock(Client.class);
        Client clienteTESTB = mock(Client.class);
        Client clienteTESTC = mock(Client.class);
        subService.addSubscriber(clienteTESTA);
        subService.addSubscriber(clienteTESTB);
        subService.addSubscriber(clienteTESTC);
        assertEquals(subService.subscribers.size(),3);}
    
    
	// STATE TEST:
    /*	No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers.
    	Al hacerlo, se lanza la excepción NullClientException.*/
    @Test(expected = NullClientException.class)
    public void borradoClienteNuloTest() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = null;
        subService.removeSubscriber(clienteTEST);}

    
	// STATE TEST:
    /*	No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers.
    	Al hacerlo, se lanza la excepción NonExistingClientException.*/
    @Test(expected = NonExistingClientException.class)
    public void borradoClienteInexistenteTest() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        subService.addSubscriber(clienteTEST);
        Client clienteTEST2 = mock(Client.class);
        subService.removeSubscriber(clienteTEST2);}


	// STATE TEST:
    //Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
    @Test
    public void borradoUnicoClienteTest() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        Client clienteTEST2 = mock(Client.class);
        subService.addSubscriber(clienteTEST);
        subService.removeSubscriber(clienteTEST);
        subService.addSubscriber(clienteTEST2);
        assertEquals(subService.subscribers.size(),1);}
    
    
	// STATE TEST:
    //No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers.
    //Al hacerlo, se lanza la excepción NonExistingClientException.
    @Test(expected = NonExistingClientException.class)
    public void borradoDobleClienteTest() throws NonExistingClientException, NullClientException, ExistingClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        subService.addSubscriber(clienteTEST);
        subService.removeSubscriber(clienteTEST);
        subService.removeSubscriber(clienteTEST);}

	// STATE TEST:
    //Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
    @Test
    public void borrarVariosClientesTest() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        Client clienteTEST2 = mock(Client.class);
        Client clienteTEST3 = mock(Client.class);
        Client clienteTEST4 = mock(Client.class);
        subService.addSubscriber(clienteTEST);
        subService.addSubscriber(clienteTEST2);
        subService.addSubscriber(clienteTEST3);
        subService.addSubscriber(clienteTEST4);
        subService.removeSubscriber(clienteTEST2);
        subService.removeSubscriber(clienteTEST3);
        assertEquals(subService.subscribers.size(),2);}
    
	// STATE TEST:
    //Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
    @Test
    public void borrarTodosClientesTest() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        Client clienteTEST2 = mock(Client.class);
        Client clienteTEST3 = mock(Client.class);
        subService.addSubscriber(clienteTEST);
        subService.addSubscriber(clienteTEST2);
        subService.addSubscriber(clienteTEST3);
        subService.removeSubscriber(clienteTEST);
        subService.removeSubscriber(clienteTEST2);
        subService.removeSubscriber(clienteTEST3);
        assertEquals(subService.subscribers.size(),0);
    }

	// INTERACTION TEST:
    //Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
    @Test
    public void clienteRecibeMensajeTest() throws ExistingClientException, NullClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        Message mensaje = mock(Message.class);
        doReturn(true).when(clienteTEST).hasEmail();
        doNothing().when(clienteTEST).receiveMessage(mensaje);
        subService.addSubscriber(clienteTEST);
        subService.sendMessage(mensaje);
        verify(clienteTEST,times(1)).receiveMessage(mensaje);}

    
	// INTERACTION TEST:
    //Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
    @Test
    public void clienteSinEmailTest() throws ExistingClientException, NullClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        Message mensaje = mock(Message.class);
        doReturn(false).when(clienteTEST).hasEmail();
        doNothing().when(clienteTEST).receiveMessage(mensaje);
        subService.addSubscriber(clienteTEST);
        subService.sendMessage(mensaje);
        verify(clienteTEST,times(0)).receiveMessage(mensaje);}


	// INTERACTION TEST:
    //Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
    @Test
    public void variosClientesConEmailTest() throws ExistingClientException, NullClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        Client clienteTEST2 = mock(Client.class);
        Message mensaje = mock(Message.class);
        doReturn(true).when(clienteTEST).hasEmail();
        doReturn(true).when(clienteTEST2).hasEmail();
        doNothing().when(clienteTEST).receiveMessage(mensaje);
        doNothing().when(clienteTEST2).receiveMessage(mensaje);
        subService.addSubscriber(clienteTEST);
        subService.addSubscriber(clienteTEST2);
        subService.sendMessage(mensaje);
        verify(clienteTEST,times(1))
                .receiveMessage(mensaje);
        verify(clienteTEST2,times(1)).receiveMessage(mensaje);}



	// INTERACTION TEST:
    //Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
    @Test
    public void mensajesClienteNoSuscritoTest() throws ExistingClientException, NullClientException, NonExistingClientException {
        SubscriptionService subService = new SubscriptionService();
        Client clienteTEST = mock(Client.class);
        Message msg = mock(Message.class);

        doReturn(true).when(clienteTEST).hasEmail();
        doNothing().when(clienteTEST).receiveMessage(msg);
        subService.addSubscriber(clienteTEST);
        subService.removeSubscriber(clienteTEST);
        subService.sendMessage(msg);
        verify(clienteTEST,times(0)).receiveMessage(msg);}
    
}

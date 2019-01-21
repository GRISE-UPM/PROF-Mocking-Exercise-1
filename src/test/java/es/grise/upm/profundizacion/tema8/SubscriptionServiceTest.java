package es.grise.upm.profundizacion.tema8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

public class SubscriptionServiceTest {
	
	private SubscriptionService serviceToTest = new SubscriptionService();
	private Client client1=mock(Client.class);
	private Client client2=mock(Client.class);
	Collection <Client> subscribers;


	/**
	 * Test 1: No se puede añadir un Client null a la lista subscribers
	 */
	@Test(expected = NullClientException.class)
	public void test1() throws ExistingClientException, NullClientException{
		serviceToTest.addSubscriber(null);
	}
	
	/**
	 * Test 2: Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	 */
	@Test
	public void test2() throws ExistingClientException, NullClientException {
		serviceToTest.addSubscriber(client1);
		subscribers = serviceToTest.subscribers;
		assert(subscribers.contains(client1));
	}

	/**
	 * Test 3:No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. 
	 * Al hacerlo, se lanza la excepción ExistingClientException.
	 */
	@Test(expected = ExistingClientException.class)
	public void test3() throws ExistingClientException, NullClientException {
		serviceToTest.addSubscriber(client1);
		serviceToTest.addSubscriber(client1);
		assertTrue("Debería haber fallado.",true);
	}
	
	
	/**
	 * Test 4: Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	 */
	@Test
	public void test4() throws ExistingClientException, NullClientException {
		serviceToTest.addSubscriber(client1);
		serviceToTest.addSubscriber(client2);
		subscribers = serviceToTest.subscribers;
		assert(subscribers.contains(client1) && subscribers.contains(client2));
	}
	
	
	
	/**
	 * Test 5:No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NullClientException.
	 * @throws NonExistingClientException 
	 * 
	 */
	@Test(expected = NullClientException.class)
	public void test5() throws ExistingClientException, NullClientException, NonExistingClientException {
		serviceToTest.removeSubscriber(null);
		assertTrue("Debería haber fallado.",false);
	}
	
	/**
	 * Test 6:No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	@Test(expected = NonExistingClientException.class)
	public void test6() throws ExistingClientException, NullClientException, NonExistingClientException {
		serviceToTest.removeSubscriber(client1);
		assertTrue("Debería haber fallado.",false);
	}
	
	
	/**
	 * Test 7: Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	 */
	@Test
	public void test7() throws ExistingClientException, NullClientException,NonExistingClientException {
		serviceToTest.addSubscriber(client1);
		serviceToTest.removeSubscriber(client1);
	}
	

	/**
	 * Test 8:No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers.
	 *  Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	@Test(expected = NonExistingClientException.class)
	public void test8() throws ExistingClientException, NullClientException,NonExistingClientException {
		serviceToTest.addSubscriber(client1);
		serviceToTest.removeSubscriber(client1);
		serviceToTest.removeSubscriber(client1);
	}
	
	
	/**
	 * Test 9:Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
	 * Test 10: Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.

	 */
	@Test
	public void test9() throws ExistingClientException, NullClientException,NonExistingClientException {
		serviceToTest.addSubscriber(client1);
		serviceToTest.addSubscriber(client2);
		serviceToTest.removeSubscriber(client1);
		serviceToTest.removeSubscriber(client2);
		subscribers = serviceToTest.subscribers;
		assertTrue("No debería fallar.",subscribers.isEmpty());

		
	}
	
	
	/**
	 * Interaction test
	 * Test 1:Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).

	 */
	
	/**
	 * Interaction test
	 * Test 2:Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).

	 */
	
	/**
	 * Interaction test
	 * Test 3: Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).

	 */
	
	/**
	 * Interaction test
	 * Test 4:Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).

	 */
	
	
}

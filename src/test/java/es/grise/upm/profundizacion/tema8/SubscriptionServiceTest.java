package es.grise.upm.profundizacion.tema8;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SubscriptionServiceTest {
	
	private SubscriptionService serviceToTest = new SubscriptionService();
	private Client client1=mock(Client.class);
	

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
		assert(serviceToTest.subscribers.contains(client1));
	}

	/**
	 * Test 3:No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. 
	 * Al hacerlo, se lanza la excepción ExistingClientException.
	 */
	
	
	/**
	 * Test 4: Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	 */
	
	/**
	 * Test 5:No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NullClientException.
	 */
	
	
	/**
	 * Test 6:No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. 
	 * Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	
	
	/**
	 * Test 7: Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	 */


	/**
	 * Test 8:No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers.
	 *  Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	
	
	/**
	 * Test 9:Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
	 */
	
	/**
	 * Test 10: Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.

	 */
	
	
}

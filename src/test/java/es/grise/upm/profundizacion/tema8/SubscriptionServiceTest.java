package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SubscriptionServiceTest {
	
	@Mock Client mockClient;
	@Mock Message mockMessage;
	
	private SubscriptionService subscriptionService;
	
	@Before
	public void init() {
		
		MockitoAnnotations.initMocks(this);
		subscriptionService = new SubscriptionService();
	}
	
	/**
	 * No se puede añadir un Client null a la lista subscribers.
	 * @throws ExistingClientException 
	 * @throws NullClientException 
	 */
	@Test(expected = NullClientException.class)
	public void stateTest1() throws Exception {
		
		subscriptionService.addSubscriber(null);
	}
	
	/**
	 * Al añadir un Client mediante addSubscriber(), éste Client se almacena en la lista subscribers.
	 */
	@Test
	public void stateTest2() throws Exception {
		
		subscriptionService.addSubscriber(mockClient);
		
		assertThat(mockClient).isIn(subscriptionService.subscribers);
	}
	
	/**
	 * No se puede añadir dos veces el mismo Client mediante addSubscriber() en la lista subscribers. Al hacerlo, se lanza la excepción ExistingClientException.
	 */
	@Test(expected = ExistingClientException.class)
	public void stateTest3() throws Exception {
		
		subscriptionService.addSubscriber(mockClient);
		subscriptionService.addSubscriber(mockClient);
	}
	
	/**
	 * Al añadir varios Client mediante addSubscriber(), todos los Client se almacenan en la lista subscribers.
	 */
	@Test
	public void stateTest4() throws Exception {
		
		Client mockClient2 = mock(Client.class);
		Client mockClient3 = mock(Client.class);
		
		subscriptionService.addSubscriber(mockClient);
		subscriptionService.addSubscriber(mockClient2);
		subscriptionService.addSubscriber(mockClient3);
		
		assertThat(mockClient).isIn(subscriptionService.subscribers);
		assertThat(mockClient2).isIn(subscriptionService.subscribers);
		assertThat(mockClient3).isIn(subscriptionService.subscribers);
		assertThat(subscriptionService.subscribers.size()).isEqualTo(3);
	}
	
	/**
	 * No se puede eliminar (usando removeSubscriber()) un Client null de la lista subscribers. Al hacerlo, se lanza la excepción NullClientException.
	 */
	@Test(expected = NullClientException.class)
	public void stateTest5() throws Exception {
		
		subscriptionService.removeSubscriber(null);
	}
	
	/**
	 * No se puede eliminar (usando removeSubscriber()) un Client que no está almacenado en  la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	@Test(expected = NonExistingClientException.class)
	public void stateTest6() throws Exception {
		
		subscriptionService.removeSubscriber(mockClient);
	}
	
	/**
	 * Se puede eliminar correctamente (usando removeSubscriber()) un Client almacenado en  la lista subscribers.
	 */
	@Test
	public void stateTest7() throws Exception {
		
		subscriptionService.addSubscriber(mockClient);		
		subscriptionService.removeSubscriber(mockClient);
		
		assertThat(subscriptionService.subscribers).isEmpty();
	}
	
	/**
	 * No se puede eliminar (usando removeSubscriber()) dos veces el mismo Client de la lista subscribers. Al hacerlo, se lanza la excepción NonExistingClientException.
	 */
	@Test(expected = NonExistingClientException.class)
	public void stateTest8() throws Exception {
		
		subscriptionService.addSubscriber(mockClient);		
		subscriptionService.removeSubscriber(mockClient);		
		subscriptionService.removeSubscriber(mockClient);
	}
	
	/**
	 * Se pueden eliminar correctamente (usando removeSubscriber()) varios Client almacenados en  la lista subscribers.
	 */
	@Test
	public void stateTest9() throws Exception {
		
		Client mockClient2 = mock(Client.class);
		Client mockClient3 = mock(Client.class);
		
		subscriptionService.addSubscriber(mockClient);
		subscriptionService.addSubscriber(mockClient2);
		subscriptionService.addSubscriber(mockClient3);	
		subscriptionService.removeSubscriber(mockClient);		
		subscriptionService.removeSubscriber(mockClient2);		
		subscriptionService.removeSubscriber(mockClient3);
		
		assertThat(subscriptionService.subscribers).isEmpty();
	}
	
	/**
	 * Se pueden eliminar correctamente (usando removeSubscriber()) todos los Client almacenados en  la lista subscribers.
	 * 
	 * Nota: No entiendo que aporta este test respecto al anterior, o quizás no entiendo que se prentende testear con este test
	 */
	@Test
	public void stateTest10() throws Exception {
		
		Client mockClient2 = mock(Client.class);
		Client mockClient3 = mock(Client.class);
		
		subscriptionService.addSubscriber(mockClient);
		subscriptionService.addSubscriber(mockClient2);
		subscriptionService.addSubscriber(mockClient3);	
		subscriptionService.removeSubscriber(mockClient);		
		subscriptionService.removeSubscriber(mockClient2);		
		subscriptionService.removeSubscriber(mockClient3);
		
		assertThat(subscriptionService.subscribers).isEmpty();
	}
	
	/**
	 * Un Client suscrito recibe mensajes (método receiveMessage()) si tiene email (método hasEmail() == true).
	 */
	@Test
	public void interactionTest1() throws Exception {
		
		given(mockClient.hasEmail()).willReturn(true);
		
		subscriptionService.addSubscriber(mockClient);
		subscriptionService.sendMessage(mockMessage);
		
		verify(mockClient).receiveMessage(mockMessage);
	}
	
	/**
	 * Un Client suscrito no recibe mensajes (método receiveMessage()) si no tiene email (método hasEmail() == false).
	 */
	@Test
	public void interactionTest2() throws Exception {
		
		given(mockClient.hasEmail()).willReturn(false);
		
		subscriptionService.addSubscriber(mockClient);
		subscriptionService.sendMessage(mockMessage);
		
		verify(mockClient, never()).receiveMessage(mockMessage);
	}
	
	/**
	 * Varios  Client suscritos reciben mensajes (método receiveMessage()) si tienen email (método hasEmail() == true).
	 */
	@Test
	public void interactionTest3() throws Exception {
		
		Client mockClient2 = mock(Client.class);
		Client mockClient3 = mock(Client.class);
		
		given(mockClient.hasEmail()).willReturn(true);
		given(mockClient2.hasEmail()).willReturn(true);
		given(mockClient3.hasEmail()).willReturn(true);
		
		subscriptionService.addSubscriber(mockClient);
		subscriptionService.addSubscriber(mockClient2);
		subscriptionService.addSubscriber(mockClient3);
		subscriptionService.sendMessage(mockMessage);
		
		verify(mockClient).receiveMessage(mockMessage);
		verify(mockClient2).receiveMessage(mockMessage);
		verify(mockClient3).receiveMessage(mockMessage);
	}
	
	/**
	 * Al des-suscribir un Client, éste no recibe mensajes (método receiveMessage()).
	 */
	@Test
	public void interactionTest4() throws Exception {
		
		given(mockClient.hasEmail()).willReturn(true);
		
		subscriptionService.addSubscriber(mockClient);
		subscriptionService.removeSubscriber(mockClient);
		subscriptionService.sendMessage(mockMessage);
		
		verify(mockClient, never()).receiveMessage(mockMessage);
	}
}

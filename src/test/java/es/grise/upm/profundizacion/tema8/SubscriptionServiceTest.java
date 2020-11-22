package es.grise.upm.profundizacion.tema8;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class SubscriptionServiceTest {
	
	@Test(expected = NullClientException.class)
	public void stateTest_case1() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		ss.addSubscriber(null);
	}
	
	@Test
	public void stateTest_case2() throws NullClientException, ExistingClientException{
		SubscriptionService ss = new SubscriptionService();
		Client c = mock(Client.class);
		
		ss.addSubscriber(c);
		assertTrue(ss.subscribers.contains(c));
	}
	
	@Test(expected = ExistingClientException.class)
	public void stateTest_case3() throws NullClientException, ExistingClientException{
		SubscriptionService ss = new SubscriptionService();
		Client c = mock(Client.class);
		
		ss.addSubscriber(c);
		ss.addSubscriber(c);
	}
	
	@Test
	public void stateTest_case4() throws NullClientException, ExistingClientException{
		SubscriptionService ss = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		
		assertTrue(ss.subscribers.contains(c1));
		assertTrue(ss.subscribers.contains(c2));
		assertTrue(ss.subscribers.contains(c3));
	}
	
	@Test(expected = NullClientException.class)
	public void stateTest_case5() throws NullClientException, NonExistingClientException{
		SubscriptionService ss = new SubscriptionService();		
		ss.removeSubscriber(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void stateTest_case6() throws NullClientException, NonExistingClientException{
		SubscriptionService ss = new SubscriptionService();	
		Client c = mock(Client.class);
		ss.removeSubscriber(c);
	}
	
	@Test
	public void stateTest_case7() throws NullClientException, NonExistingClientException, ExistingClientException{
		SubscriptionService ss = new SubscriptionService();	
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		ss.removeSubscriber(c);
		assertFalse(ss.subscribers.contains(c));
	}
	
	@Test(expected = NonExistingClientException.class)
	public void stateTest_case8() throws NullClientException, NonExistingClientException, ExistingClientException{
		SubscriptionService ss = new SubscriptionService();	
		Client c = mock(Client.class);
		ss.addSubscriber(c);
		ss.removeSubscriber(c);
		ss.removeSubscriber(c);
	}
	
	@Test
	public void stateTest_case9() throws NullClientException, ExistingClientException, NonExistingClientException{
		SubscriptionService ss = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		
		ss.removeSubscriber(c1);
		ss.removeSubscriber(c2);
		
		assertFalse(ss.subscribers.contains(c1));
		assertFalse(ss.subscribers.contains(c2));
	}
	
	@Test
	public void stateTest_case10() throws NullClientException, ExistingClientException, NonExistingClientException{
		SubscriptionService ss = new SubscriptionService();
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		
		ss.removeSubscriber(c1);
		ss.removeSubscriber(c2);
		ss.removeSubscriber(c3);
		
		assertFalse(ss.subscribers.contains(c1));
		assertFalse(ss.subscribers.contains(c2));
		assertFalse(ss.subscribers.contains(c3));
		
		assertEquals(0, ss.subscribers.size());
	}
	
	
	@Test
	public void interactionTest_case1() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		
		Client c = mock(Client.class);
		Message m = mock(Message.class);
		
		ss.addSubscriber(c);
		
		when(c.hasEmail()).thenReturn(true);
		
		ss.sendMessage(m);
		
		verify(c).receiveMessage(m);
	}
	
	@Test
	public void interactionTest_case2() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		
		Client c = mock(Client.class);
		Message m = mock(Message.class);
		
		ss.addSubscriber(c);
		
		when(c.hasEmail()).thenReturn(false);
		
		ss.sendMessage(m);
		
		verify(c, times(0)).receiveMessage(m);
	}
	
	@Test
	public void interactionTest_case3() throws NullClientException, ExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		
		Client c1 = mock(Client.class);
		Client c2 = mock(Client.class);
		Client c3 = mock(Client.class);
		Message m = mock(Message.class);
		
		ss.addSubscriber(c1);
		ss.addSubscriber(c2);
		ss.addSubscriber(c3);
		
		when(c1.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		when(c3.hasEmail()).thenReturn(true);
		
		ss.sendMessage(m);
		
		verify(c1).receiveMessage(m);
		verify(c2).receiveMessage(m);
		verify(c3).receiveMessage(m);
	}
	
	@Test
	public void interactionTest_case4() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService ss = new SubscriptionService();
		
		Client c = mock(Client.class);
		Message m = mock(Message.class);
		
		ss.addSubscriber(c);
		
		when(c.hasEmail()).thenReturn(true);
		
		ss.removeSubscriber(c);
		
		ss.sendMessage(m);
		
		verify(c, times(0)).receiveMessage(m);
	}
	
	
	
	
}

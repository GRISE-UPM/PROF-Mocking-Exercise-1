 package es.grise.upm.profundizacion.tema8;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import  static org.mockito.Mockito.*;

import java.util.Collection;


public class SubscriptionServiceTest {
	
	@Test
	public void testClientNull() throws NullClientException, ExistingClientException {

		SubscriptionService ser = new SubscriptionService();
		ser.subscribers = mock(Collection.class);
		when(ser.subscribers.add(null)).thenReturn(false);
		Boolean b =ser.subscribers.add(null);
		assertTrue(!b);
	}
	@Test
	public void testAddClient() throws NullClientException, ExistingClientException {

		SubscriptionService ser = new SubscriptionService();
		Client c = mock(Client.class);
        ser.subscribers = mock(Collection.class);
        when(ser.subscribers.contains(c)).thenReturn(false);
        ser.addSubscriber(c);
        verify(ser.subscribers, times(1)).add(c);
	}
	
	
	@Test(expected = ExistingClientException.class)
	public void  clients2ErrorTest() throws NullClientException, ExistingClientException {

		SubscriptionService ser = new SubscriptionService();
		Client c = mock(Client.class);
        ser.subscribers = mock(Collection.class);
        when(ser.subscribers.contains(c)).thenReturn(false);
        ser.addSubscriber(c);
        when(ser.subscribers.contains(c)).thenReturn(true);
        ser.addSubscriber(c);
        verify(ser.subscribers, times(1)).add(c);
		
	}
	
	
	@Test
	public void client2Test()throws NullClientException, ExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
		Client c = mock(Client.class);
		Client c2 = mock(Client.class);
        ser.subscribers = mock(Collection.class);

        when(ser.subscribers.contains(c)).thenReturn(false);
        when(ser.subscribers.contains(c2)).thenReturn(false);

		ser.addSubscriber(c);
		ser.addSubscriber(c2);
		verify(ser.subscribers,times(1)).add(c);
		verify(ser.subscribers,times(1)).add(c2);
	}
	
	
	@Test(expected= NullClientException.class)
	public void removeCliErrorTest() throws NullClientException, NonExistingClientException {
		SubscriptionService sub = new SubscriptionService();
		sub.subscribers = mock(Collection.class);
		sub.removeSubscriber(null);
        verify(sub.subscribers, times(0)).remove(null);
	}
	
	
	@Test(expected= NonExistingClientException.class)
	public void removeCliExErrorTest() throws NullClientException, NonExistingClientException {
		SubscriptionService sub = new SubscriptionService();
		Client cli = mock(Client.class);
		sub.subscribers = mock(Collection.class);
		
		when(sub.subscribers.contains(cli)).thenReturn(false);
		sub.removeSubscriber(cli);
        verify(sub.subscribers, times(0)).remove(cli);
	}
	
	
	@Test()
	public void removeCliTest() throws NullClientException, NonExistingClientException {
		SubscriptionService sub = new SubscriptionService();
		Client cli = mock(Client.class);
		sub.subscribers = mock(Collection.class);
		when(sub.subscribers.contains(cli)).thenReturn(true);
		sub.removeSubscriber(cli);
		verify(sub.subscribers).remove(cli);
	}
	
	
	@Test(expected = NonExistingClientException.class)
	public void  clients2RemErrorTest() throws NullClientException, NonExistingClientException {
		SubscriptionService sub = new SubscriptionService();
		Client cli = mock(Client.class);
		sub.subscribers = mock(Collection.class);

		when(sub.subscribers.contains(cli)).thenReturn(true);
		sub.removeSubscriber(cli);
		verify(sub.subscribers).remove(cli);
		when(sub.subscribers.contains(cli)).thenReturn(false);
		sub.removeSubscriber(cli);

	}
	
	
	@Test
	public void removeFromList2Test() throws NullClientException, NonExistingClientException{
		SubscriptionService sub = new SubscriptionService();
		Client cli = mock(Client.class);
		Client cli2 = mock(Client.class);

		sub.subscribers = mock(Collection.class);

		when(sub.subscribers.contains(cli)).thenReturn(true);
		sub.removeSubscriber(cli);
		verify(sub.subscribers).remove(cli);
		when(sub.subscribers.contains(cli2)).thenReturn(true);
		sub.removeSubscriber(cli2);
		verify(sub.subscribers).remove(cli2);

	}
	
	
	@Test
	public void removeFromListAllTest() throws NullClientException, NonExistingClientException{
		SubscriptionService sub = new SubscriptionService();
		Client cli = mock(Client.class);
		Client cli2 = mock(Client.class);

		sub.subscribers = mock(Collection.class);

		when(sub.subscribers.contains(cli)).thenReturn(true);
		sub.removeSubscriber(cli);
		verify(sub.subscribers).remove(cli);
		when(sub.subscribers.contains(cli2)).thenReturn(true);
		sub.removeSubscriber(cli2);
		verify(sub.subscribers).remove(cli2);
		when(sub.subscribers.size()).thenReturn(0);

		assertEquals(0, sub.subscribers.size());

	}
	
	
	//Interaccion
	@Test
	public void receiveMess() throws NullClientException, ExistingClientException {
		SubscriptionService ser = new SubscriptionService();
		Client c = mock(Client.class);
		Message msg = mock(Message.class);
		ser.addSubscriber(c);;
		when(c.hasEmail()).thenReturn(true);
		ser.sendMessage(msg);
		verify(c).receiveMessage(msg);
	}
	
	
	@Test
	public void receiveNoMess() throws NullClientException, ExistingClientException {
		SubscriptionService ser = new SubscriptionService();
		Client c = mock(Client.class);
		Message msg = mock(Message.class);
		ser.addSubscriber(c);;
		when(c.hasEmail()).thenReturn(false);
		ser.sendMessage(msg);
		verify(c,times(0)).receiveMessage(msg);
	}
	
	
	@Test
	public void receive2Mess() throws NullClientException, ExistingClientException {
		SubscriptionService ser = new SubscriptionService();
		Client c = mock(Client.class);
		Client c2 = mock(Client.class);

		Message msg = mock(Message.class);
		ser.addSubscriber(c);
		ser.addSubscriber(c2);
		when(c.hasEmail()).thenReturn(true);
		when(c2.hasEmail()).thenReturn(true);
		ser.sendMessage(msg);
		verify(c).receiveMessage(msg);
		verify(c2).receiveMessage(msg);

	}
	
	
	@Test
	public void removeCli() throws NullClientException, ExistingClientException, NonExistingClientException {
		SubscriptionService ser = new SubscriptionService();
		Client c = mock(Client.class);

		Message msg = mock(Message.class);
		ser.addSubscriber(c);;
		when(c.hasEmail()).thenReturn(true);
		ser.removeSubscriber(c);
		ser.sendMessage(msg);
		verify(c, times(0)).receiveMessage(msg);

	}
	
	
	
	
}

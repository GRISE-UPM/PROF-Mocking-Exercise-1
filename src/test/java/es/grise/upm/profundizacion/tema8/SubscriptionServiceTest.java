package es.grise.upm.profundizacion.tema8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;


public class SubscriptionServiceTest {
	
	static SubscriptionService serv;
	
	@BeforeClass
	public static void setUp() {
		serv = new SubscriptionService();
	}
	
	@Test()
	public void noClientNull() {

		SubscriptionService ser = new SubscriptionService();
	    ser.subscribers = mock(Collection.class);
	    when(ser.subscribers.add(null)).thenReturn(false);
	    Boolean b =ser.subscribers.add(null);
	    assertFalse(b);
	}
	
	@Test
	public void addClient() throws NullClientException, ExistingClientException {
		SubscriptionService ser = new SubscriptionService();
		Client cli = mock(Client.class);
	    ser.subscribers = mock(Collection.class);
	    when(ser.subscribers.contains(cli)).thenReturn(false);
	    ser.addSubscriber(cli);
	    verify(ser.subscribers, times(1)).add(cli);
	}
	
	@Test(expected = ExistingClientException.class)
	public void add2Client() throws NullClientException, ExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
		Client cli = mock(Client.class);
	    ser.subscribers = mock(Collection.class);
	    when(ser.subscribers.contains(cli)).thenReturn(true);
	    ser.addSubscriber(cli);
	    verify(ser.subscribers, times(0)).add(cli);
	}
	
	@Test
	public void add2DifClient() throws NullClientException, ExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
		Client cli = mock(Client.class);
		Client cli2 = mock(Client.class);
	    ser.subscribers = mock(Collection.class);
	    
	    when(ser.subscribers.contains(cli)).thenReturn(false);
	    when(ser.subscribers.contains(cli2)).thenReturn(false);
	    ser.addSubscriber(cli);
	    ser.addSubscriber(cli2);
	    verify(ser.subscribers, times(1)).add(cli);
	    verify(ser.subscribers, times(1)).add(cli2);
	}
	
	@Test(expected = NullClientException.class)
	public void removeNullClient() throws NullClientException, NonExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
	    ser.subscribers = mock(Collection.class);
	    
	    ser.removeSubscriber(null);
	    verify(ser.subscribers, times(0)).remove(null);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void removeNotExistingClient() throws NullClientException, NonExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
		Client cli = mock(Client.class);
	    ser.subscribers = mock(Collection.class);
	    when(ser.subscribers.contains(cli)).thenReturn(false);
	    ser.removeSubscriber(cli);
	    verify(ser.subscribers, times(0)).remove(cli);
	}
	
	@Test
	public void removeClient() throws NullClientException, NonExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
		Client cli = mock(Client.class);
	    ser.subscribers = mock(Collection.class);
	    when(ser.subscribers.contains(cli)).thenReturn(true);
	    ser.removeSubscriber(cli);
	    verify(ser.subscribers, times(1)).remove(cli);
	}
	
	@Test(expected = NonExistingClientException.class)
	public void removeWrong2TimesClient() throws NullClientException, NonExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
		Client cli = mock(Client.class);
	    ser.subscribers = mock(Collection.class);
	    
	    when(ser.subscribers.contains(cli)).thenReturn(true);
	    ser.removeSubscriber(cli);
	    verify(ser.subscribers, times(1)).remove(cli);
	    
	    when(ser.subscribers.contains(cli)).thenReturn(false);
	    ser.removeSubscriber(cli);
	    verify(ser.subscribers, times(0)).remove(cli);
	}
	
	@Test
	public void remove2Client() throws NullClientException, NonExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
		Client cli = mock(Client.class);
		Client cli2 = mock(Client.class);
	    ser.subscribers = mock(Collection.class);
	    
	    when(ser.subscribers.contains(cli)).thenReturn(true);
	    ser.removeSubscriber(cli);
	    verify(ser.subscribers, times(1)).remove(cli);
	    
	    when(ser.subscribers.contains(cli2)).thenReturn(true);
	    ser.removeSubscriber(cli2);
	    verify(ser.subscribers, times(1)).remove(cli2);
	    
	    when(ser.subscribers.size()).thenReturn(1);
	    assertEquals(1, ser.subscribers.size());
	    
	}
	
	@Test
	public void removeAllClient() throws NullClientException, NonExistingClientException  {
		SubscriptionService ser = new SubscriptionService();
		Client cli = mock(Client.class);
		Client cli2 = mock(Client.class);
	    ser.subscribers = mock(Collection.class);
	    
	    when(ser.subscribers.contains(cli)).thenReturn(true);
	    ser.removeSubscriber(cli);
	    verify(ser.subscribers, times(1)).remove(cli);
	    
	    when(ser.subscribers.contains(cli2)).thenReturn(true);
	    ser.removeSubscriber(cli2);
	    verify(ser.subscribers, times(1)).remove(cli2);
	    
	    when(ser.subscribers.size()).thenReturn(0);
	    assertEquals(0, ser.subscribers.size());
	    
	}
	
	@Test
	public void correctSus() {
		SubscriptionService c  = new SubscriptionService();
		Client cli = mock(Client.class);
		Message mes = mock(Message.class);
		c.subscribers.add(cli);
		
		when(cli.hasEmail()).thenReturn(true);
		c.sendMessage(mes);
		verify(cli,times(1)).receiveMessage(mes);
		
	}
	
	@Test
	public void wrongSus() {
		SubscriptionService c  = new SubscriptionService();
		Client cli = mock(Client.class);
		Message mes = mock(Message.class);
		c.subscribers.add(cli);
		
		when(cli.hasEmail()).thenReturn(false);
		c.sendMessage(mes);
		verify(cli,times(0)).receiveMessage(mes);
		
	}
	
	@Test
	public void correct2Sus() {
		SubscriptionService c  = new SubscriptionService();
		Client cli1 = mock(Client.class);
		Client cli2 = mock(Client.class);
		Message mes = mock(Message.class);
		c.subscribers.add(cli1);
		c.subscribers.add(cli2);
		
		when(cli1.hasEmail()).thenReturn(true);
		when(cli2.hasEmail()).thenReturn(true);
		c.sendMessage(mes);
		verify(cli1,times(1)).receiveMessage(mes);
		verify(cli2,times(1)).receiveMessage(mes);
		
	}
	
	
	@Test
	public void correctDessus() {
		SubscriptionService c  = new SubscriptionService();
		Client cli = mock(Client.class);
		Message mes = mock(Message.class);
		
		c.subscribers.add(cli);		
		c.subscribers.remove(cli);
		c.sendMessage(mes);
		verify(cli,times(0)).receiveMessage(mes);
	}
	
	
}

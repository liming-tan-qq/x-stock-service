package coding.test.user;

import coding.test.common.StockListener;
import coding.test.model.ClientId;
import coding.test.model.Stock;
import coding.test.model.Symbol;
import coding.test.sellside.SubService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {

    @Mock
    private SubService subServiceMock;
    @Captor
    private ArgumentCaptor<ClientId> captorClientId;
    @Captor
    private ArgumentCaptor<List<StockListener>> captorStockListeners;

    private SubServiceNaive subServiceNaive;

    private ClientId clientId;
    private Client client;

    @Before
    public void setUp() {

        clientId = new ClientId("test client");
        client = new Client(clientId, subServiceMock);

        subServiceNaive = new SubServiceNaive();
    }

    @Test
    public void testStart() {

        client.start(Arrays.asList(new Symbol("A"), new Symbol("B")));

        verify(subServiceMock, times(1)).connect(captorClientId.capture(), captorStockListeners.capture());

        assertEquals(clientId, captorClientId.getAllValues().get(0));
        assertEquals(2, captorStockListeners.getAllValues().get(0).size());
    }

    @Test
    public void testStop() {

        client.stop();

        verify(subServiceMock, times(1)).disconnect(captorClientId.capture());
        assertEquals(clientId, captorClientId.getAllValues().get(0));
    }

    @Test
    public void testGetStock() {

        Stock result = client.getStock(new Symbol("A"));

        assertNull(result);
    }

    @Test
    public void shouldBeAbleToUpdateLocalCacheAfterReceivingUpdateFromSellSide() {

        client = new Client(clientId, subServiceNaive);

        client.start(Arrays.asList(new Symbol("A"), new Symbol("B")));

        Symbol symbolA = new Symbol("A");
        Stock stockA = new Stock(symbolA, null, LocalDateTime.now());

        Symbol symbolB = new Symbol("B");
        Stock stockB = new Stock(symbolB, null, LocalDateTime.now());

        Symbol symbolC = new Symbol("C");
        Stock stockC = new Stock(symbolC, null, LocalDateTime.now());

        subServiceNaive.update(stockA);
        subServiceNaive.update(stockB);
        subServiceNaive.update(stockC);

        assertEquals(stockA, client.getStock(symbolA));
        assertEquals(stockB, client.getStock(symbolB));
        assertNull(client.getStock(symbolC));
    }
}

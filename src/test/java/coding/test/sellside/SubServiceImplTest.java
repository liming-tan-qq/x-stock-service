package coding.test.sellside;

import coding.test.common.StockListener;
import coding.test.model.ClientId;
import coding.test.model.Stock;
import coding.test.model.Symbol;
import coding.test.upstream.Nasdaq;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubServiceImplTest {

    @Mock private Nasdaq nasdaqMock;

    // --------------------------------------

    Symbol sA = new Symbol("A");
    Symbol sB = new Symbol("B");
    Symbol sC = new Symbol("C");
    Symbol sD = new Symbol("D");

    // --------------------------------------
    private ClientId c1 = new ClientId("c1");
    @Mock private StockListener stockListener1A;
    @Mock private StockListener stockListener1B;

    // --------------------------------------
    private ClientId c2 = new ClientId("c2");
    @Mock private StockListener stockListener2B;
    @Mock private StockListener stockListener2C;

    // --------------------------------------
    private ClientId c3 = new ClientId("c3");
    @Mock private StockListener stockListener3D;

    private NasdaqNaive nasdaqNaive;

    private SubServiceImpl subServiceImpl;

    @Before
    public void setUp() {

        subServiceImpl = new SubServiceImpl(nasdaqMock);

        when(stockListener1A.getClientId()).thenReturn(c1);
        when(stockListener1A.getSymbol()).thenReturn(sA);

        when(stockListener1B.getClientId()).thenReturn(c1);
        when(stockListener1B.getSymbol()).thenReturn(sB);

        when(stockListener2B.getClientId()).thenReturn(c2);
        when(stockListener2B.getSymbol()).thenReturn(sB);

        when(stockListener2C.getClientId()).thenReturn(c2);
        when(stockListener2C.getSymbol()).thenReturn(sC);

        when(stockListener3D.getClientId()).thenReturn(c3);
        when(stockListener3D.getSymbol()).thenReturn(sD);

        nasdaqNaive = new NasdaqNaive();
    }

    @Test
    public void shouldSubscribeToNasdaqWhenClientConnect() {

        subServiceImpl.connect(c1, Arrays.asList(stockListener1A, stockListener1B));
        subServiceImpl.connect(c2, Arrays.asList(stockListener2B, stockListener2C));
        subServiceImpl.connect(c3, Arrays.asList(stockListener3D));

        verify(nasdaqMock, times(4)).subscribe(any(), any());
    }

    @Test
    public void shouldUnsubscribeFromNasdaqWhenClientDisconnectCase1() {

        subServiceImpl.connect(c1, Arrays.asList(stockListener1A, stockListener1B));
        subServiceImpl.connect(c2, Arrays.asList(stockListener2B, stockListener2C));
        subServiceImpl.connect(c3, Arrays.asList(stockListener3D));
        subServiceImpl.disconnect(c3);

        verify(nasdaqMock, times(1)).unsubscribe(any());
    }

    @Test
    public void shouldUnsubscribeFromNasdaqWhenClientDisconnectCase2() {

        subServiceImpl.connect(c1, Arrays.asList(stockListener1A, stockListener1B));
        subServiceImpl.connect(c2, Arrays.asList(stockListener2B, stockListener2C));
        subServiceImpl.connect(c3, Arrays.asList(stockListener3D));
        subServiceImpl.disconnect(c2);

        verify(nasdaqMock, times(1)).unsubscribe(any());
    }

    @Test
    public void shouldGetUpdatesFromNasdaq() {

        Stock stA1 = new Stock(sA, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stB1 = new Stock(sB, null, LocalDateTime.of(2020, 1, 10, 10, 10));
        Stock stA2 = new Stock(sA, null, LocalDateTime.of(2020, 1, 10, 10, 11));

        subServiceImpl = new SubServiceImpl(nasdaqNaive);

        subServiceImpl.connect(c1, Arrays.asList(stockListener1A, stockListener1B));
        subServiceImpl.connect(c2, Arrays.asList(stockListener2B, stockListener2C));
        subServiceImpl.connect(c3, Arrays.asList(stockListener3D));

        nasdaqNaive.update(stA1);
        nasdaqNaive.update(stB1);
        nasdaqNaive.update(stA2);

        assertEquals(stA2, subServiceImpl.getStock(sA));
        assertEquals(stB1, subServiceImpl.getStock(sB));
    }
}

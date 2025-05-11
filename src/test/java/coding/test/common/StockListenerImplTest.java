package coding.test.common;

import coding.test.model.ClientId;
import coding.test.model.Data;
import coding.test.model.Stock;
import coding.test.model.Symbol;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;

public class StockListenerImplTest {

    private static final Symbol SYMBOL_A = new Symbol("A");

    private ConcurrentMap<Symbol, Stock> stocksLocalCache;
    private StockListenerImpl stockListener;

    @Before
    public void setUp() {

        stocksLocalCache = new ConcurrentHashMap<>();
        stockListener = new StockListenerImpl(new ClientId("id"), SYMBOL_A, stocksLocalCache);
    }

    @Test
    public void shouldBeAbleToUpdateLocalCacheOnChange() {

        Data data1 = new Data(BigDecimal.valueOf(1), BigDecimal.valueOf(1));
        Stock stock1 = new Stock(SYMBOL_A, data1, LocalDateTime.of(2025, 05, 11, 10, 00, 00));
        stockListener.onChange(stock1);
        assertEquals(stock1, stocksLocalCache.get(SYMBOL_A));

        Data data2 = new Data(BigDecimal.valueOf(2), BigDecimal.valueOf(2));
        Stock stock2 = new Stock(SYMBOL_A, data2, LocalDateTime.of(2025, 05, 11, 10, 01, 01));
        stockListener.onChange(stock2);
        assertEquals(stock2, stocksLocalCache.get(SYMBOL_A));
    }
}

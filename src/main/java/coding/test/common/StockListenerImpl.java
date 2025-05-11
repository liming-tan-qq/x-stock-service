package coding.test.common;

import coding.test.model.ClientId;
import coding.test.model.Stock;
import coding.test.model.Symbol;

import java.util.concurrent.ConcurrentMap;

public class StockListenerImpl implements StockListener {

    private final ClientId clientId;
    private final Symbol symbol;
    private final ConcurrentMap<Symbol, Stock> stocksLocalCache;

    public StockListenerImpl(ClientId clientId, Symbol symbol, ConcurrentMap<Symbol, Stock> stocksLocalCache) {
        this.clientId = clientId;
        this.symbol = symbol;
        this.stocksLocalCache = stocksLocalCache;
    }

    @Override
    public ClientId getClientId() {
        return clientId;
    }

    @Override
    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public void onChange(Stock stock) {
        stocksLocalCache.put(symbol, stock);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        StockListenerImpl that = (StockListenerImpl) o;
        return getClientId().equals(that.getClientId()) && getSymbol().equals(that.getSymbol());
    }
}

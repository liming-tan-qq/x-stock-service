package coding.test.user;

import coding.test.common.StockListenerImpl;
import coding.test.model.ClientId;
import coding.test.model.Stock;
import coding.test.common.StockListener;
import coding.test.model.Symbol;
import coding.test.sellside.SubService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Client {

    private final ClientId clientId;
    private final SubService subService;

    private final ConcurrentMap<Symbol, Stock> stocksLocalCache;

    public Client(ClientId clientId, SubService subService) {

        this.clientId = clientId;
        this.subService = subService;
        this.stocksLocalCache = new ConcurrentHashMap<>();
    }

    public void start(List<Symbol> symbols) {

        List<StockListener> stockListeners = new ArrayList<>();

        for (Symbol symbol : symbols) {

            stockListeners.add(new StockListenerImpl(clientId, symbol, stocksLocalCache));
        }

        subService.connect(clientId, stockListeners);
    }

    public void stop() {

        subService.disconnect(clientId);
    }

    public Stock getStock(Symbol symbol) {

        return stocksLocalCache.get(symbol);
    }
}

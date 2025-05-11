package coding.test.sellside;

import coding.test.model.ClientId;
import coding.test.model.Stock;
import coding.test.common.StockListener;
import coding.test.model.Symbol;
import coding.test.upstream.Nasdaq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SubServiceImpl implements SubService {

    private final Nasdaq nasdaq;
    private final ConcurrentMap<Symbol, Stock> stocksLocalCache;

    private final ConcurrentMap<Symbol, List<StockListener>> listenersBySymbol;
    private final ConcurrentMap<ClientId, List<StockListener>> listenersByClientId;

    public SubServiceImpl(Nasdaq nasdaq) {

        this.nasdaq = nasdaq;
        this.stocksLocalCache = new ConcurrentHashMap<>();

        this.listenersBySymbol = new ConcurrentHashMap<>();
        this.listenersByClientId = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void connect(ClientId clientId, List<StockListener> stockListeners) {

        for (StockListener stockListener : stockListeners) {

            Symbol symbol = stockListener.getSymbol();

            if (listenersBySymbol.containsKey(symbol)) {

                listenersBySymbol.get(symbol).add(stockListener);

            } else {    // the given symbol has not been seen before

                List<StockListener> newListeners = new ArrayList<>();
                newListeners.add(stockListener);
                listenersBySymbol.put(symbol, newListeners);

                nasdaq.subscribe(symbol, new StockListener() {

                    @Override
                    public ClientId getClientId() {
                        return new ClientId("SubServiceImpl");
                    }

                    @Override
                    public Symbol getSymbol() {
                        return symbol;
                    }

                    @Override
                    public void onChange(Stock stock) {
                        stocksLocalCache.put(symbol, stock);
                        for(StockListener listener: listenersBySymbol.get(symbol)) {
                            listener.onChange(stock);
                        }
                    }
                });
            }
        }

        listenersByClientId.put(clientId, stockListeners);
    }

    @Override
    public synchronized void disconnect(ClientId clientId) {

        for (StockListener listener : listenersByClientId.get(clientId)) {

            Symbol symbol = listener.getSymbol();

            listenersBySymbol.get(symbol).remove(listener);

            if (listenersBySymbol.get(symbol).isEmpty()) {          // no more remaining listeners

                listenersBySymbol.remove(symbol);
                nasdaq.unsubscribe(symbol);
                stocksLocalCache.remove(symbol);
            }
        }

        listenersByClientId.remove(clientId);
    }

    public Stock getStock(Symbol symbol) {

        return stocksLocalCache.get(symbol);
    }
}

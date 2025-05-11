package coding.test.user;

import coding.test.common.StockListener;
import coding.test.model.ClientId;
import coding.test.model.Stock;
import coding.test.sellside.SubService;

import java.util.ArrayList;
import java.util.List;

public class SubServiceNaive implements SubService {

    private final List<StockListener> stockListeners;

    public SubServiceNaive() {

        stockListeners = new ArrayList<>();
    }

    public void update(Stock stock) {

        for (StockListener listener : stockListeners) {

            if (stock.getSymbol().equals(listener.getSymbol())) {

                listener.onChange(stock);
            }
        }
    }

    @Override
    public void connect(ClientId clientId, List<StockListener> stockListeners) {

        this.stockListeners.addAll(stockListeners);
    }

    @Override
    public void disconnect(ClientId clientId) {

        throw new UnsupportedOperationException();
    }
}

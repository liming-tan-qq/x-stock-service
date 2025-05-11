package coding.test.sellside;

import coding.test.common.StockListener;
import coding.test.model.Stock;
import coding.test.model.Symbol;
import coding.test.upstream.Nasdaq;

import java.util.ArrayList;
import java.util.List;

public class NasdaqNaive implements Nasdaq {

    private final List<StockListener> stockListeners;

    public NasdaqNaive() {

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
    public void subscribe(Symbol symbol, StockListener listener) {

        stockListeners.add(listener);
    }

    @Override
    public void unsubscribe(Symbol symbol) {

        throw new UnsupportedOperationException();
    }
}

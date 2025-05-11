package coding.test.upstream;

import coding.test.common.StockListener;
import coding.test.model.Symbol;

public interface Nasdaq {

    void subscribe(Symbol symbol, StockListener listener);

    void unsubscribe(Symbol symbol);
}

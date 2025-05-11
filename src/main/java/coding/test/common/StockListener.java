package coding.test.common;

import coding.test.model.ClientId;
import coding.test.model.Stock;
import coding.test.model.Symbol;

public interface StockListener {

    ClientId getClientId();

    Symbol getSymbol();

    void onChange(Stock stock);
}

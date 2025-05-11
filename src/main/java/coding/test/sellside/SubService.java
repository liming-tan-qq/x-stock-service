package coding.test.sellside;

import coding.test.model.ClientId;
import coding.test.common.StockListener;

import java.util.List;

public interface SubService {

    void connect(ClientId clientId, List<StockListener> stockListeners);

    void disconnect(ClientId clientId);
}

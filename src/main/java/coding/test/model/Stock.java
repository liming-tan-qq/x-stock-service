package coding.test.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Stock {

    private final Symbol symbol;      // key / id of a given stock
    private final Data data;
    private final LocalDateTime updatedAt;

    public Stock(Symbol symbol, Data data, LocalDateTime updatedAt) {
        this.symbol = symbol;
        this.data = data;
        this.updatedAt = updatedAt;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Data getData() {
        return data;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;
        return Objects.equals(getSymbol(), stock.getSymbol()) && Objects.equals(getData(), stock.getData()) && Objects.equals(getUpdatedAt(), stock.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getSymbol());
        result = 31 * result + Objects.hashCode(getData());
        result = 31 * result + Objects.hashCode(getUpdatedAt());
        return result;
    }
}

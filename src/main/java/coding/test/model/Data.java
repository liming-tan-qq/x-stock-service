package coding.test.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Data {

    // -- only adding two fields for demo purposes --
    private final BigDecimal price;
    private final BigDecimal quantity;
    // -- only adding two fields for demo purposes --

    public Data(BigDecimal price, BigDecimal quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Data data = (Data) o;
        return Objects.equals(getPrice(), data.getPrice()) && Objects.equals(getQuantity(), data.getQuantity());
    }
}

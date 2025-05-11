package coding.test.model;

import java.util.Objects;

public class Symbol {
    // added this wrapper class only for better readability purposes
    // otherwise, could just use String

    private final String symbol;

    public Symbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol1 = (Symbol) o;
        return Objects.equals(getSymbol(), symbol1.getSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSymbol());
    }
}

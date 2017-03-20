package expressivo;

import java.util.Map;

public class Const implements Expression {

    private double value;

    public Const(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public Expression differentiate(Var variable) {
        return new Const(0);
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        return this;
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        return value == ((Const) obj).value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

}

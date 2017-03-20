package expressivo;

import java.util.Map;

public class Var implements Expression {

    private String value;

    public Var(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Expression differentiate(Var variable) {
        if (!this.equals(variable)) {
            return new Const(0);
        } else {
            return new Const(1);
        }
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        if (environment.containsKey(this.value)) {
            return new Const(environment.get(this.value));
        } else {
            return this;
        }
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        return value.equals(((Var) obj).value);
    }

    @Override
    public String toString() {
        return value;
    }

}

package expressivo;

import java.util.Map;

public class Sum implements Expression {

    private Expression a;
    private Expression b;

    public Sum(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Expression differentiate(Var variable) {
        return new Sum(a.differentiate(variable), b.differentiate(variable));
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        Sum sum = new Sum(a.simplify(environment), b.simplify(environment));
        if (sum.isNumeric()) {
            return new Const(((Const) sum.a).getValue() + ((Const) sum.b).getValue());
        }
        return sum;
    }

    @Override
    public boolean isNumeric() {
        return a.isNumeric() && b.isNumeric();
    }

    @Override
    public int hashCode() {
        return 63 * (a.hashCode() + 1) * (b.hashCode() + 2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        return a.equals(((Sum) obj).a) &&
                b.equals(((Sum) obj).b);
    }

    @Override
    public String toString() {
        return "(" +  a.toString() + " + " + b.toString() + ")";
    }

}

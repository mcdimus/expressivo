package expressivo;

import java.util.Map;

public class Product implements Expression {

    private Expression a;
    private Expression b;

    public Product(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Expression differentiate(Var variable) {
        return new Sum(new Product(a, b.differentiate(variable)), new Product(b, a.differentiate(variable)));
    }

    @Override
    public Expression simplify(Map<String, Double> environment) {
        Product product = new Product(a.simplify(environment), b.simplify(environment));
        if (product.isNumeric()) {
            return new Const(((Const) product.a).getValue() * ((Const) product.b).getValue());
        }
        return product;
    }

    @Override
    public boolean isNumeric() {
        return a.isNumeric() && b.isNumeric();
    }

    @Override
    public int hashCode() {
        return 11 * (a.hashCode() + 1) * (b.hashCode() + 2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;

        return a.equals(((Product) obj).a) &&
                b.equals(((Product) obj).b);
    }

    @Override
    public String toString() {
        return a.toString() + "*" + b.toString();
    }

}

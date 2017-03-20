/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    // Testing strategy
    //   TODO

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }


    // TODO tests for Expression

    @Test
    public void const_ToString() throws Exception {
        assertEquals("10.0", new Const(10).toString());
        assertEquals("20.5", new Const(20.5).toString());
        assertEquals("30.33", new Const(30.33).toString());
        assertEquals("40.123", new Const(40.123).toString());
    }

    @Test
    public void const_hashCodeAndEquals() throws Exception {
        Const a = new Const(10);
        Const b = new Const(10);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a, b);
        assertEquals(b, a);

        Const c = new Const(40.123);
        Const d = new Const(40.123);
        assertEquals(c.hashCode(), d.hashCode());
        assertEquals(c, d);
        assertEquals(d, c);
    }

    @Test
    public void parse_Const() throws Exception {
        Expression expression1 = Expression.parse("10");
        assertEquals(new Const(10), expression1);
        assertEquals(new Const(10.0), expression1);

        Expression expression2 = Expression.parse("10.0");
        assertEquals(new Const(10), expression2);
        assertEquals(new Const(10.0), expression2);

        Expression expression3 = Expression.parse("12.345");
        assertEquals(new Const(12.345), expression3);
    }

    @Test
    public void parse_Var() throws Exception {
        Expression expression1 = Expression.parse("x");
        assertEquals(new Var("x"), expression1);
        assertNotEquals(new Var("X"), expression1);

        Expression expression2 = Expression.parse("qwerty");
        assertEquals(new Var("qwerty"), expression2);
        assertNotEquals(new Var("qweRty"), expression2);
    }

    @Test
    public void parse_Sum_IfNumbersWithWhitespaces() throws Exception {
        Expression expression1 = Expression.parse("1 + 2");
        assertEquals(new Sum(new Const(1), new Const(2)), expression1);
        assertNotEquals(new Sum(new Const(2), new Const(1)), expression1);

        Expression expression2 = Expression.parse("1.234     + 2");
        assertEquals(new Sum(new Const(1.234), new Const(2)), expression2);
        assertNotEquals(new Sum(new Const(2), new Const(1.234)), expression2);

        Expression expression3 = Expression.parse("1 +     2.345");
        assertEquals(new Sum(new Const(1), new Const(2.345)), expression3);
        assertNotEquals(new Sum(new Const(2.345), new Const(1)), expression3);

        Expression expression4 = Expression.parse("    1.234 + 2.345  ");
        assertEquals(new Sum(new Const(1.234), new Const(2.345)), expression4);
        assertNotEquals(new Sum(new Const(2.345), new Const(1.234)), expression4);
    }

    @Test
    public void parse_Sum_IfNumbersWithNoWhitespaces() throws Exception {
        Expression expression1 = Expression.parse("1+2");
        assertEquals(new Sum(new Const(1), new Const(2)), expression1);
        assertNotEquals(new Sum(new Const(2), new Const(1)), expression1);

        Expression expression2 = Expression.parse("1.234+2");
        assertEquals(new Sum(new Const(1.234), new Const(2)), expression2);
        assertNotEquals(new Sum(new Const(2), new Const(1.234)), expression2);

        Expression expression3 = Expression.parse("1+2.345");
        assertEquals(new Sum(new Const(1), new Const(2.345)), expression3);
        assertNotEquals(new Sum(new Const(2.345), new Const(1)), expression3);

        Expression expression4 = Expression.parse("1.234+2.345");
        assertEquals(new Sum(new Const(1.234), new Const(2.345)), expression4);
        assertNotEquals(new Sum(new Const(2.345), new Const(1.234)), expression4);
    }

    @Test
    public void parse_Sum_IfNumbersWithParenthesis() throws Exception {
        Expression expression1 = Expression.parse("(1+2)");
        assertEquals(new Sum(new Const(1), new Const(2)), expression1);
        assertNotEquals(new Sum(new Const(2), new Const(1)), expression1);

        Expression expression2 = Expression.parse("(1)+2");
        assertEquals(new Sum(new Const(1), new Const(2)), expression2);
        assertNotEquals(new Sum(new Const(2), new Const(1)), expression2);

        Expression expression3 = Expression.parse("1 + (2)");
        assertEquals(new Sum(new Const(1), new Const(2)), expression3);
        assertNotEquals(new Sum(new Const(2), new Const(1)), expression3);

        Expression expression4 = Expression.parse("(1) + (2)");
        assertEquals(new Sum(new Const(1), new Const(2)), expression4);
        assertNotEquals(new Sum(new Const(2), new Const(1)), expression4);
    }

    @Test
    public void parse_Sum_IfVariablesWithWhitespaces() throws Exception {
        Expression expression1 = Expression.parse("x + y");
        assertEquals(new Sum(new Var("x"), new Var("y")), expression1);
        assertNotEquals(new Sum(new Var("y"), new Var("x")), expression1);

        Expression expression2 = Expression.parse("      x     +  y         ");
        assertEquals(new Sum(new Var("x"), new Var("y")), expression2);
        assertNotEquals(new Sum(new Var("y"), new Var("x")), expression2);
    }

    @Test
    public void parse_Sum_IfVariablesWithNoWhitespaces() throws Exception {
        Expression expression1 = Expression.parse("x+y");
        assertEquals(new Sum(new Var("x"), new Var("y")), expression1);
        assertNotEquals(new Sum(new Var("y"), new Var("x")), expression1);

        Expression expression2 = Expression.parse("x+x");
        assertEquals(new Sum(new Var("x"), new Var("x")), expression2);
    }

    @Test
    public void parse_Sum_IfNumberWIthVariableAndNumber() throws Exception {
        Expression expression1 = Expression.parse("x + 10.123");
        assertEquals(new Sum(new Var("x"), new Const(10.123)), expression1);
        assertNotEquals(new Sum(new Const(10.123), new Var("x")), expression1);

        Expression expression2 = Expression.parse("10.123 + x");
        assertEquals(new Sum(new Const(10.123), new Var("x")), expression2);
        assertNotEquals(new Sum(new Var("x"), new Const(10.123)), expression2);
    }

    @Test
    public void parse_Sum_IfMultipleOperands() throws Exception {
        Expression expression1 = Expression.parse("x + 10.123 + y + 20.345");
        assertEquals(new Sum(new Sum(new Sum(new Var("x"), new Const(10.123)), new Var("y")), new Const(20.345)), expression1);

        Expression expression2 = Expression.parse("(x + 10.123 + y + 20.345)");
        assertEquals(new Sum(new Sum(new Sum(new Var("x"), new Const(10.123)), new Var("y")), new Const(20.345)), expression2);
    }

    @Test
    public void parse_Multiply_IfNumbersWithWhitespaces() throws Exception {
        Expression expression1 = Expression.parse("1 * 2");
        assertEquals(new Product(new Const(1), new Const(2)), expression1);
        assertNotEquals(new Product(new Const(2), new Const(1)), expression1);

        Expression expression2 = Expression.parse("1.234     * 2");
        assertEquals(new Product(new Const(1.234), new Const(2)), expression2);
        assertNotEquals(new Product(new Const(2), new Const(1.234)), expression2);

        Expression expression3 = Expression.parse("1 *     2.345");
        assertEquals(new Product(new Const(1), new Const(2.345)), expression3);
        assertNotEquals(new Product(new Const(2.345), new Const(1)), expression3);

        Expression expression4 = Expression.parse("    1.234 * 2.345  ");
        assertEquals(new Product(new Const(1.234), new Const(2.345)), expression4);
        assertNotEquals(new Product(new Const(2.345), new Const(1.234)), expression4);
    }

    @Test
    public void parse_Multiply_IfNumbersWithNoWhitespaces() throws Exception {
        Expression expression1 = Expression.parse("1*2");
        assertEquals(new Product(new Const(1), new Const(2)), expression1);
        assertNotEquals(new Product(new Const(2), new Const(1)), expression1);

        Expression expression2 = Expression.parse("1.234*2");
        assertEquals(new Product(new Const(1.234), new Const(2)), expression2);
        assertNotEquals(new Product(new Const(2), new Const(1.234)), expression2);

        Expression expression3 = Expression.parse("1*2.345");
        assertEquals(new Product(new Const(1), new Const(2.345)), expression3);
        assertNotEquals(new Product(new Const(2.345), new Const(1)), expression3);

        Expression expression4 = Expression.parse("1.234*2.345");
        assertEquals(new Product(new Const(1.234), new Const(2.345)), expression4);
        assertNotEquals(new Product(new Const(2.345), new Const(1.234)), expression4);
    }

    @Test
    public void parse_Multiply_IfNumbersWithParenthesis() throws Exception {
        Expression expression1 = Expression.parse("(1*2)");
        assertEquals(new Product(new Const(1), new Const(2)), expression1);
        assertNotEquals(new Product(new Const(2), new Const(1)), expression1);

        Expression expression2 = Expression.parse("(1)*2");
        assertEquals(new Product(new Const(1), new Const(2)), expression2);
        assertNotEquals(new Product(new Const(2), new Const(1)), expression2);

        Expression expression3 = Expression.parse("1 * (2)");
        assertEquals(new Product(new Const(1), new Const(2)), expression3);
        assertNotEquals(new Product(new Const(2), new Const(1)), expression3);

        Expression expression4 = Expression.parse("(1) * (2)");
        assertEquals(new Product(new Const(1), new Const(2)), expression4);
        assertNotEquals(new Product(new Const(2), new Const(1)), expression4);
    }

    @Test
    public void parse_Multiply_IfVariablesWithWhitespaces() throws Exception {
        Expression expression1 = Expression.parse("x * y");
        assertEquals(new Product(new Var("x"), new Var("y")), expression1);
        assertNotEquals(new Product(new Var("y"), new Var("x")), expression1);

        Expression expression2 = Expression.parse("      x     *  y         ");
        assertEquals(new Product(new Var("x"), new Var("y")), expression2);
        assertNotEquals(new Product(new Var("y"), new Var("x")), expression2);
    }

    @Test
    public void parse_Multiply_IfVariablesWithNoWhitespaces() throws Exception {
        Expression expression1 = Expression.parse("x*y");
        assertEquals(new Product(new Var("x"), new Var("y")), expression1);
        assertNotEquals(new Product(new Var("y"), new Var("x")), expression1);

        Expression expression2 = Expression.parse("x*x");
        assertEquals(new Product(new Var("x"), new Var("x")), expression2);
    }

    @Test
    public void parse_Multiply_IfNumberWIthVariableAndNumber() throws Exception {
        Expression expression1 = Expression.parse("x * 10.123");
        assertEquals(new Product(new Var("x"), new Const(10.123)), expression1);
        assertNotEquals(new Product(new Const(10.123), new Var("x")), expression1);

        Expression expression2 = Expression.parse("10.123 * x");
        assertEquals(new Product(new Const(10.123), new Var("x")), expression2);
        assertNotEquals(new Product(new Var("x"), new Const(10.123)), expression2);
    }

    @Test
    public void parse_Multiply_IfMultipleOperands() throws Exception {
        Expression expression1 = Expression.parse("x * 10.123 * y * 20.345");
        assertEquals(new Product(new Product(new Product(new Var("x"), new Const(10.123)), new Var("y")), new Const(20.345)), expression1);

        Expression expression2 = Expression.parse("(x * 10.123 * y * 20.345)");
        assertEquals(new Product(new Product(new Product(new Var("x"), new Const(10.123)), new Var("y")), new Const(20.345)), expression2);
    }

    @Test
    public void parse_MultiplyAndSum() throws Exception {
        Expression expression1 = Expression.parse("x * 10.123 * y + 20.345");
        assertEquals(new Sum(new Product(new Product(new Var("x"), new Const(10.123)), new Var("y")), new Const(20.345)), expression1);

        Expression expression2 = Expression.parse("(x * 10.123 * y) + 20.345");
        assertEquals(new Sum(new Product(new Product(new Var("x"), new Const(10.123)), new Var("y")), new Const(20.345)), expression2);

        Expression expression3 = Expression.parse("(x * 10.123 + y) + 20.345");
        assertEquals(new Sum(new Sum(new Product(new Var("x"), new Const(10.123)), new Var("y")), new Const(20.345)), expression3);

        Expression expression4 = Expression.parse("(x * 10.123) + (y + 20.345)");
        assertEquals(new Sum(new Product(new Var("x"), new Const(10.123)), new Sum(new Var("y"), new Const(20.345))), expression4);

        Expression expression5 = Expression.parse("x*(10.123 + y)*20.345");
        assertEquals(new Product(new Product(new Var("x"), new Sum(new Const(10.123), new Var("y"))), new Const(20.345)), expression5);
    }

    @Test
    public void differentiate_0_IfConst() throws Exception {
        Expression expression1 = Expression.parse("0").differentiate(new Var("x"));
        assertEquals(new Const(0), expression1);
    }

    @Test
    public void differentiate_0_IfVariableOtherThatGiven() throws Exception {
        Expression expression1 = Expression.parse("X").differentiate(new Var("x"));
        assertEquals(new Const(0), expression1);

        Expression expression2 = Expression.parse("y").differentiate(new Var("x"));
        assertEquals(new Const(0), expression2);

        Expression expression3 = Expression.parse("whatever").differentiate(new Var("x"));
        assertEquals(new Const(0), expression3);
    }

    @Test
    public void differentiate_1_IfSameVariable() throws Exception {
        Expression expression1 = Expression.parse("x").differentiate(new Var("x"));
        assertEquals(new Const(1), expression1);
    }

    @Test
    public void differentiate_SumOf0s_IfSumOfConst() throws Exception {
        Expression expression1 = Expression.parse("1 + 2").differentiate(new Var("x"));
        assertEquals(new Sum(new Const(0), new Const(0)), expression1);

        Expression expression2 = Expression.parse("1 + 2 + 3").differentiate(new Var("x"));
        assertEquals(new Sum(new Sum(new Const(0), new Const(0)), new Const(0)), expression2);
    }

    @Test
    public void differentiate_SumOf0s_IfSumOfOtherVariables() throws Exception {
        Expression expression1 = Expression.parse("y + z").differentiate(new Var("x"));
        assertEquals(new Sum(new Const(0), new Const(0)), expression1);

        Expression expression2 = Expression.parse("a + b + c").differentiate(new Var("x"));
        assertEquals(new Sum(new Sum(new Const(0), new Const(0)), new Const(0)), expression2);
    }

    @Test
    public void differentiate_SumOf0s_IfSumOfOtherVariablesAndConst() throws Exception {
        Expression expression1 = Expression.parse("y + 1").differentiate(new Var("x"));
        assertEquals(new Sum(new Const(0), new Const(0)), expression1);

        Expression expression2 = Expression.parse("a + b + 2").differentiate(new Var("x"));
        assertEquals(new Sum(new Sum(new Const(0), new Const(0)), new Const(0)), expression2);
    }

    @Test
    public void differentiate_SumOf0sAnd1s_IfSumOfVariablesOtherVariablesAndConst() throws Exception {
        Expression expression = Expression.parse("x + b + 2").differentiate(new Var("x"));
        assertEquals(new Sum(new Sum(new Const(1), new Const(0)), new Const(0)), expression);
    }

    @Test
    public void differentiate_CorrectExpression_IfProduct() throws Exception {
        Expression expression1 = Expression.parse("x*x").differentiate(new Var("x"));
        assertEquals(new Sum(new Product(new Var("x"), new Const(1)), new Product(new Var("x"), new Const(1))), expression1);

        Expression expression2 = Expression.parse("x*x*x").differentiate(new Var("x"));
        assertEquals(new Sum(
                new Product(new Product(new Var("x"), new Var("x")), new Const(1)),
                new Product(new Var("x"), new Sum(new Product(new Var("x"), new Const(1)), new Product(new Var("x"), new Const(1))))
        ), expression2);
    }

    @Test
    public void simplify() throws Exception {
        HashMap<String , Double> environment = new HashMap<>();
        environment.put("x", 10.0);

        Expression expression1 = Expression.parse("x + x").simplify(environment);
        assertEquals(new Const(20), expression1);
        Expression expression2 = Expression.parse("x + x + x").simplify(environment);
        assertEquals(new Const(30), expression2);
        Expression expression3 = Expression.parse("x + x + y").simplify(environment);
        assertEquals(new Sum(new Const(20), new Var("y")), expression3);
        Expression expression4 = Expression.parse("x + y + x").simplify(environment);
        assertEquals(new Sum(new Sum(new Const(10), new Var("y")), new Const(10)), expression4);
        Expression expression5 = Expression.parse("y + x + x").simplify(environment);
        assertEquals(new Sum(new Sum(new Var("y"), new Const(10)), new Const(10)), expression5);
    }

}

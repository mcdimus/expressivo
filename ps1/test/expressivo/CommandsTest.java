/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //   TODO
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void differentiate_Sum() throws Exception {
        assertEquals("0.0", Commands.differentiate("10", "x"));
        assertEquals("1.0", Commands.differentiate("x", "x"));
        assertEquals("(1.0 + 0.0)", Commands.differentiate("x + 10", "x"));
        assertEquals("((1.0 + 0.0) + 0.0)", Commands.differentiate("x + 10 + y", "x"));
        assertEquals("((1.0 + 1.0) + 1.0)", Commands.differentiate("x + x + x", "x"));
    }

    @Test
    public void differentiate_Product() throws Exception {
        assertEquals("(x*0.0 + 10.0*1.0)", Commands.differentiate("x*10", "x"));
        assertEquals("(x*1.0 + x*1.0)", Commands.differentiate("x*x", "x"));
        assertEquals("(x*x*1.0 + x*(x*1.0 + x*1.0))", Commands.differentiate("x*x*x", "x"));
        assertEquals("(x*0.0 + y*1.0)", Commands.differentiate("x*y", "x"));
        assertEquals("(y*x*0.0 + y*(y*1.0 + x*0.0))", Commands.differentiate("y*x*y", "x"));
    }

    @Test
    public void differentiate_SumAndProduct() throws Exception {
        assertEquals("((x*0.0 + 10.0*1.0) + (y*0.0 + 20.0*0.0))", Commands.differentiate("x*10 + y*20", "x"));
        assertEquals("((x*(1.0 + 0.0) + (x + 10.0)*1.0) + (x*0.0 + y*1.0))", Commands.differentiate("x*(x + 10) + x*y", "x"));
    }

}

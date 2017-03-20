package expressivo;

import lib6005.parser.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    //   Expression = Const(a:double)
    //                  + Var(a:String)
    //                  + Sum(a:Expression, b:Expression)
    //                  + Product(a:Expression, b:Expression)
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        try {
//            Parser<Grammar> parser = GrammarCompiler.compile(new File("src/expressivo/Expression.g"), Grammar.ROOT);
            Parser<Grammar> parser = GrammarCompiler.compile(Expression.class.getResourceAsStream("Expression.g"), Grammar.ROOT);
            ParseTree<Grammar> parseTree = parser.parse(input);
//            System.out.println("parseTree = " + parseTree.toString());
//            visitAll(parseTree, "\t");
            return buildAST(parseTree);
        } catch (UnableToParseException | IOException e) {
           throw new IllegalArgumentException(e.getMessage(),e);
        }
    }

    static void visitAll(ParseTree<Grammar> node, String indent){
        if(node.isTerminal()){
            System.out.println(indent + node.getName() + ":" + node.getContents());
        }else{
            System.out.println(indent + node.getName());
            for(ParseTree<Grammar> child: node){
                visitAll(child, indent + "   ");
            }
        }
    }

    /**
     * Function converts a ParseTree to an IntegerExpression.
     * @param p
     *  ParseTree<IntegerGrammar> that is assumed to have been constructed by the grammar in IntegerExpression.g
     * @return
     */
    static Expression buildAST(ParseTree<Grammar> p){

        switch(p.getName()){
            case NUMBER:
                return new Const(Double.parseDouble(p.getContents()));
            case VAR:
                return new Var(p.getContents());
            case PRIMITIVE:
                if (!p.childrenByName(Grammar.NUMBER).isEmpty()) {
                    return buildAST(p.childrenByName(Grammar.NUMBER).get(0));
                }
                if (!p.childrenByName(Grammar.VAR).isEmpty()) {
                    return buildAST(p.childrenByName(Grammar.VAR).get(0));
                }
                if (!p.childrenByName(Grammar.PRODUCT).isEmpty()) {
                    return buildAST(p.childrenByName(Grammar.PRODUCT).get(0));
                }
                return buildAST(p.childrenByName(Grammar.SUM).get(0));
            case SUM:
                boolean first = true;
                Expression result = null;
                for(ParseTree<Grammar> child : p.childrenByName(Grammar.PRODUCT)){
                    if(first){
                        result = buildAST(child);
                        first = false;
                    }else{
                        result = new Sum(result, buildAST(child));
                    }
                }
                if (first) {
                    throw new RuntimeException("sum must have a non whitespace child:" + p);
                }
                return result;
            case PRODUCT:
                boolean first_ = true;
                Expression result_ = null;
                for(ParseTree<Grammar> child : p.childrenByName(Grammar.PRIMITIVE)){
                    if(first_){
                        result_ = buildAST(child);
                        first_ = false;
                    } else{
                        result_ = new Product(result_, buildAST(child));
                    }
                }
                if (first_) {
                    throw new RuntimeException("sum must have a non whitespace child:" + p);
                }
                return result_;
            case ROOT:
                if (p.childrenByName(Grammar.SUM).isEmpty()) {
                    return buildAST(p.childrenByName(Grammar.PRODUCT).get(0));
                } else {
                    return buildAST(p.childrenByName(Grammar.SUM).get(0));
                }
            case WHITESPACE:
                throw new RuntimeException("You should never reach here:" + p);
        }
        /*
         * The compiler should be smart enough to tell that this code is unreachable, but it isn't.
         */
        throw new RuntimeException("You should never reach here:" + p);
    }

    Expression differentiate(Var variable);
    Expression simplify(Map<String,Double> environment);
    boolean isNumeric();

    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}

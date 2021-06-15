package syntax.analyzer.util;

import java.util.Arrays;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public enum Terminals {
    VAR("var"),
    OPEN_KEY("{"),
    CLOSE_KEY("}"),
    CONST("const"),
    BOOLEAN("boolean"),
    STRING("string"),
    INT("int"),
    REAL("real"),
    IDENTIFIER("identifier"),
    SEMICOLON(";"),
    OPEN_BRACKET("["),
    CLOSE_BRACKET("]"),
    EQUALS("="),
    OPEN_PARENTHESES("("),
    CLOSE_PARENTHESES(")"),
    COMMA(","),
    TYPEDEF("typedef"),
    STRUCT("struct"),
    EXTENDS("extends"),
    FUNCTION("function"),
    PROCEDURE("procedure"),
    RETURN("return"),
    TRUE("true"),
    FALSE("false"),
    MULTIPLICATION("*"),
    DIVISION("/"),
    ADD("+"),
    MINUS("-"),
    OR("||"),
    AND("&&"),
    EQUALITY("=="),
    DIFFERENT("!="),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    DOT("."),
    EXPRESSION("<Expression>"),
    READ("read"),
    PRINT("print"),
    LESS("<"),
    GREATER(">"),
    LESS_EQUAL("<="),
    GREATER_EQUAL("<="),
    EXCLAMATION("!"),
    CALL_FUNCTION("<Function>"),
    START("start"),
    GLOBAL("global"),
    LOCAL("local"),
    THEN("then");

    private final String VALUE;

    public boolean isTerminal(String value) {
        return Arrays.asList(Terminals.values())
                .stream()
                .filter(terminal -> terminal.VALUE.equals(value))
                .findAny()
                .isPresent();
    }

    public String getVALUE() {
        return VALUE;
    }

    private Terminals(String value) {
        this.VALUE = value;
    }
    
}

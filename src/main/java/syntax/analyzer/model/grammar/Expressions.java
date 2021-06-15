package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Expressions {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        orExpressionConsumer(tokens);
    }

    public static void orExpressionConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        andExpression(tokens);
        if (!tokens.isEmpty() && tokens.peek().thisLexameIs(OR.getVALUE())) {
            TokenUtil.consumerByLexame(tokens, OR);
            orExpressionConsumer(tokens);
        }
    }

    public static void andExpression(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        equalityExpression(tokens);
        if (!tokens.isEmpty() && tokens.peek().thisLexameIs(AND.getVALUE())) {
            TokenUtil.consumerByLexame(tokens, AND);
            andExpression(tokens);
        }
    }

    public static void equalityExpression(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        compareExpression(tokens);
        if (!tokens.isEmpty()) {
            Token token = tokens.peek();
            if (token.thisLexameIs(EQUALITY.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, EQUALITY);
                equalityExpression(tokens);
            } else if (token.thisLexameIs(DIFFERENT.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, DIFFERENT);
                equalityExpression(tokens);
            }
        }
    }

    public static void compareExpression(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        addExpression(tokens);
        if (!tokens.isEmpty()) {
            Token token = tokens.peek();
            if (token.thisLexameIs(LESS.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, LESS);
                compareExpression(tokens);
            } else if (token.thisLexameIs(GREATER.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, GREATER);
                compareExpression(tokens);
            } else if (token.thisLexameIs(LESS_EQUAL.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, LESS_EQUAL);
                compareExpression(tokens);
            } else if (token.thisLexameIs(GREATER_EQUAL.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, GREATER_EQUAL);
                compareExpression(tokens);
            }
        }
    }

    public static void addExpression(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        multiplicationExpressionConsumer(tokens);
        if (!tokens.isEmpty()) {
            Token token = tokens.peek();
            if (token.thisLexameIs(ADD.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, ADD);
                addExpression(tokens);
            } else if (token.thisLexameIs(MINUS.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, MINUS);
                addExpression(tokens);
            } 
        }
    }

    public static void multiplicationExpressionConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        unaryExpression(tokens);
        if (!tokens.isEmpty()) {
            Token token = tokens.peek();
            if (token.thisLexameIs(MULTIPLICATION.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, MULTIPLICATION);
                multiplicationExpressionConsumer(tokens);
            } else if (token.thisLexameIs(DIVISION.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, DIVISION);
                multiplicationExpressionConsumer(tokens);
            }
        }
    }

    public static void unaryExpression(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        if (!tokens.isEmpty() && tokens.peek().thisLexameIs(EXCLAMATION.getVALUE())) {
            TokenUtil.consumerByLexame(tokens, EXCLAMATION);
            unaryExpression(tokens);
        } else {
            primaryExpressionConsumer(tokens);
        }
    }

    public static void primaryExpressionConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        if (!tokens.isEmpty()) {
            Token token = tokens.peek();
            if (token.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                TokenUtil.consumerByLexame(tokens, OPEN_PARENTHESES);
                fullChecker(tokens);
                TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
            } else {
                try {
                    TypeDeclaration.primaryConsumer(tokens);
                } catch (SyntaxErrorException e) {
                    throw new SyntaxErrorException(token.getLexame(),
                            IDENTIFIER,
                            TRUE,
                            FALSE,
                            REAL,
                            OPEN_PARENTHESES
                    );
                }
                if (!tokens.isEmpty()) {
                    token = tokens.peek();
                    TokenType type = token.getType();
                    if (type != TokenType.RELATIONAL
                            && type != TokenType.ARITHMETIC
                            && type != TokenType.LOGICAL
                            && type != TokenType.IDENTIFIER
                            && !token.thisLexameIs(CLOSE_PARENTHESES.getVALUE())
                            && !token.thisLexameIs(SEMICOLON.getVALUE())
                            && !token.thisLexameIs(COMMA.getVALUE())) {
                        throw new SyntaxErrorException(token.getLexame(), AND,
                                OR,
                                IDENTIFIER,
                                EQUALITY,
                                DIFFERENT,
                                GREATER,
                                GREATER_EQUAL,
                                LESS,
                                LESS_EQUAL);
                    }
                }
            }
        }
    }
}

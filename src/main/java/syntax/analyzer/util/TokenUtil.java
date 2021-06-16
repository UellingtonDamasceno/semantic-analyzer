package syntax.analyzer.util;

import java.util.Arrays;
import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class TokenUtil {

    public static boolean testTypeBeforeConsume(Deque<Token> tokens, TokenType type, Terminals terminal) throws EOFNotExpectedException {
        EOFNotExpectedException.throwIfEmpty(tokens, terminal);
        return tokens.peek().getType() == type;
    }

    public static boolean testLexameBeforeConsume(Deque<Token> tokens, Terminals terminal) throws EOFNotExpectedException {
        EOFNotExpectedException.throwIfEmpty(tokens, terminal);
        return tokens.peek().thisLexameIs(terminal.getVALUE());
    }

    public static void consumerByLexame(Deque<Token> tokens, Terminals terminal) throws SyntaxErrorException, EOFNotExpectedException {
        EOFNotExpectedException.throwIfEmpty(tokens, terminal);

        Token token = tokens.pop();
        if (!token.thisLexameIs(terminal.getVALUE())) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), terminal);
        }
    }

    public static void consumerByType(Deque<Token> tokens, TokenType tokenType, Terminals terminal) throws SyntaxErrorException, EOFNotExpectedException {
        EOFNotExpectedException.throwIfEmpty(tokens, terminal);

        Token token = tokens.pop();
        if (token.getType() != tokenType) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), terminal);
        }
    }

    public static boolean contains(Token token, Terminals... terminals) {
        return Arrays.asList(terminals)
                .stream()
                .anyMatch((terminal) -> token.thisLexameIs(terminal.getVALUE()));
    }

    public static void consumer(Deque<Token> tokens) {
        tokens.pop();
    }

    public static void consumeExpectedTokenByLexame(Deque<Token> tokens, Terminals terminal) throws EOFNotExpectedException {
        try {
            TokenUtil.consumerByLexame(tokens, terminal);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewSyntaticalError(tokens, terminal);
        }
    }

    public static void consumeExpectedTokenByType(Deque<Token> tokens, TokenType tokenType, Terminals terminal) throws EOFNotExpectedException {
        try {
            TokenUtil.consumerByType(tokens, tokenType, terminal);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewSyntaticalError(tokens, terminal);
        }
    }            
}

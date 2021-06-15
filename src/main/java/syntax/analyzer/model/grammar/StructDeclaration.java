package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class StructDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumerByLexame(tokens, STRUCT);

        if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_KEY)) {
            TokenUtil.consumer(tokens);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, EXTENDS)) {
            TokenUtil.consumer(tokens);
            TokenUtil.consumeExpectedTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_KEY);
        } else {
            throw new SyntaxErrorException(tokens.peek().getLexame(), OPEN_KEY, EXTENDS);
        }
        structDefConsumer(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_KEY);
        try {
            TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewInternalError(tokens, IDENTIFIER);
        }
        TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
    }

    public static void structDefConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        if (TokenUtil.testLexameBeforeConsume(tokens, VAR)) {
            VarDeclaration.fullChecker(tokens);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, CONST)) {
//            ConstDeclaration.fullChecker(tokens);
        } else {
            throw new SyntaxErrorException(tokens.peek().getLexame(), VAR, CONST);
        }
        EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
        if (TokenUtil.testLexameBeforeConsume(tokens, VAR) || TokenUtil.testLexameBeforeConsume(tokens, CONST)) {
            structDefConsumer(tokens);
        }
    }

    public static void structUsageConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumeExpectedTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
    }
}

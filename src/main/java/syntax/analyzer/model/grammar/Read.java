package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.util.ErrorManager;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class Read {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_PARENTHESES);
        if (TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
            ErrorManager.addNewSyntaticalError(tokens, IDENTIFIER);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            ErrorManager.addNewSyntaticalError(tokens, IDENTIFIER);
            moreReadings(tokens);
        } else {
            expressionReadConsumer(tokens);
        }
        TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_PARENTHESES);
    }

    public static void expressionReadConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumeExpectedTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        if (!TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)
                && !TokenUtil.testLexameBeforeConsume(tokens, COMMA)
                && !TokenUtil.testLexameBeforeConsume(tokens, SEMICOLON)) {
            if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
                StructDeclaration.structUsageConsumer(tokens);
            } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                Arrays.dimensionConsumer(tokens);
            } else {
                ErrorManager.addNewSyntaticalError(tokens, DOT, OPEN_BRACKET, COMMA, CLOSE_PARENTHESES);
                TokenUtil.consumer(tokens);
            }
        }

        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            moreReadings(tokens);
        } else if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)) {
            ErrorManager.addNewSyntaticalError(tokens, COMMA, CLOSE_PARENTHESES);
            expressionReadConsumer(tokens);
        }
    }

    public static void moreReadings(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        expressionReadConsumer(tokens);
    }
}

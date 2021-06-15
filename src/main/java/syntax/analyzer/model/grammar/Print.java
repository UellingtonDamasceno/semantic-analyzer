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
 * @author Antônio Neto e Uellington Damasceno
 */
public class Print {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_PARENTHESES);
        if (TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
            ErrorManager.addNewInternalError(tokens, STRING, IDENTIFIER);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            ErrorManager.addNewInternalError(tokens, IDENTIFIER, STRING);
            morePrints(tokens);
        } else {
            expressionPrintConsumer(tokens);
        }
        TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_PARENTHESES);
    }

    public static void expressionPrintConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        try {
            TypeDeclaration.literalConsumer(tokens);
        } catch (SyntaxErrorException e) {
            if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.STRING, Terminals.STRING)) {
                TokenUtil.consumer(tokens);
            } else if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)) {
                TokenUtil.consumer(tokens);
                if (!TokenUtil.testLexameBeforeConsume(tokens, COMMA)
                        && !TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)
                        && !TokenUtil.testLexameBeforeConsume(tokens, SEMICOLON)) {
                    if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
                        StructDeclaration.structUsageConsumer(tokens);
                    } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                        Arrays.dimensionConsumer(tokens);
                    } else {
                        ErrorManager.addNewInternalError(tokens, DOT, OPEN_BRACKET, COMMA, CLOSE_PARENTHESES);
                        TokenUtil.consumer(tokens);
                    }
                }
            }
        }
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            morePrints(tokens);
        } else if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.STRING, Terminals.STRING)
                || TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)) {
            ErrorManager.addNewInternalError(tokens, COMMA, CLOSE_PARENTHESES);
            expressionPrintConsumer(tokens);
        }
    }

    public static void morePrints(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        expressionPrintConsumer(tokens);
    }
}

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
public class Signature {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        TokenUtil.consumer(tokens);
        try {
            TokenUtil.consumerByLexame(tokens, OPEN_PARENTHESES);
        } catch (SyntaxErrorException e) {
            TokenUtil.consumeExpectedTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_PARENTHESES);
        }
        try {
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException e) {
            try {
                typeList(tokens);
                TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_PARENTHESES);
            } catch (SyntaxErrorException ex) {
                if(TokenUtil.testLexameBeforeConsume(tokens, SEMICOLON)){
                    ErrorManager.addNewInternalError(tokens, CLOSE_PARENTHESES);
                }
            }
            TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
        }
    }

    public static void paramsChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        try {
            typedIdentifier(tokens);
            EOFNotExpectedException.throwIfEmpty(tokens, COMMA, CLOSE_PARENTHESES);
            if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                TokenUtil.consumer(tokens);
                paramsChecker(tokens);
            } else if (TypeDeclaration.primaryChecker(tokens.peek())) {
                ErrorManager.addNewInternalError(tokens, COMMA, CLOSE_PARENTHESES);
                paramsChecker(tokens);
            }
        } catch (SyntaxErrorException e) {
            if (!TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
                throw e;
            } else {
                ErrorManager.addNewInternalError(tokens, IDENTIFIER);
            }
        }
    }

    /*
    Equivalente as produções:
    Func ID,
    Param
     */
    public static void typedIdentifier(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        try {
            TypeDeclaration.typeConsumer(tokens);
            TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        } catch (SyntaxErrorException e) {
            if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, IDENTIFIER)) {
                ErrorManager.addNewInternalError(tokens, INT, REAL, STRING, BOOLEAN);
                TokenUtil.consumer(tokens);
            } else if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                ErrorManager.addNewInternalError(tokens, IDENTIFIER);
                TokenUtil.consumer(tokens);
                typedIdentifier(tokens);
            } else {
                throw e;
            }
        }
    }

    public static void typeList(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        try {
            typeConsume(tokens);
            EOFNotExpectedException.throwIfEmpty(tokens, COMMA, CLOSE_PARENTHESES);
            if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                TokenUtil.consumer(tokens);
                typeList(tokens);
            } else if (!TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
                ErrorManager.addNewInternalError(tokens, COMMA, CLOSE_PARENTHESES);
            }
        } catch (SyntaxErrorException e) {
            if (!TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
                throw e;
            }
        }
    }

    public static void typeConsume(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        try {
            try {
                TypeDeclaration.typeConsumer(tokens);
                TokenUtil.consumerByLexame(tokens, COMMA);
            } catch (SyntaxErrorException e) {
                TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            }
        } catch (SyntaxErrorException e) {
            if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)
                    || TypeDeclaration.typeChecker(tokens.peek())) {
                ErrorManager.addNewInternalError(tokens, COMMA);
                TokenUtil.consumer(tokens);
            } else if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                ErrorManager.addNewInternalError(tokens, INT, REAL, STRING, BOOLEAN, IDENTIFIER);
                TokenUtil.consumer(tokens);
                typeConsume(tokens);
            } else {
                throw e;
            }
        }
    }
}

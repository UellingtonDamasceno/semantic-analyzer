package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.SimpleIdentifier;
import semantic.analyzer.model.SymTable;
import semantic.analyzer.model.exceptions.SymbolAlreadyDeclaredException;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import syntax.analyzer.util.TokenUtil;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class ConstDeclaration {

    private static SymTable table;
    private static Token currentType;
    
    public static void fullChecker(Deque<Token> tokens, SymTable parent) throws EOFNotExpectedException {
        table = parent;
        TokenUtil.consumer(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_KEY);
        try {
            typedConstConsumer(tokens);
            TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_KEY);
        } catch (SyntaxErrorException e) {
            if (TokenUtil.testLexameBeforeConsume(tokens, CLOSE_KEY)) {
                ErrorManager.addNewSyntaticalError(e);
                TokenUtil.consumer(tokens);
            }
        }
    }

    public static void typedConstConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        try {
            currentType = tokens.peek();
            TypeDeclaration.typeConsumer(tokens);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewSyntaticalError(tokens, INT, REAL, STRING, BOOLEAN);
            throw e;
        }
        try {
            constConsumer(tokens);
            TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
        } catch (SyntaxErrorException e) {
            EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
            if (TypeDeclaration.typeChecker(tokens.peek())) {
                ErrorManager.addNewSyntaticalError(e);
                typedConstConsumer(tokens);
            } else {
                throw e;
            }
        }
        EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
        if (TypeDeclaration.typeChecker(tokens.peek())) {
            typedConstConsumer(tokens);
        }
    }

    public static void constConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        constDeclarator(tokens);
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumer(tokens);
            constConsumer(tokens);
        } else if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)) {
            ErrorManager.addNewSyntaticalError(tokens, COMMA, SEMICOLON);
            constConsumer(tokens);
        }
    }

    public static void constDeclarator(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        SimpleIdentifier id = new SimpleIdentifier(currentType, tokens.peek(), true);
        try {
            table.insert(id, tokens.peek());
        } catch (SymbolAlreadyDeclaredException ex) {
            ErrorManager.addNewSemanticalError(ex);
        }
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TokenUtil.consumeExpectedTokenByLexame(tokens, EQUALS);
        try {
            TypeDeclaration.literalConsumer(tokens);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewSyntaticalError(tokens, TRUE, FALSE, INT, REAL, STRING);
            TokenUtil.consumer(tokens);
        }
    }
}

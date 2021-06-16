package syntax.analyzer.model.grammar;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Function;
import semantic.analyzer.model.Identifiers.IdentifierWithArguments;
import semantic.analyzer.model.exceptions.SymbolAlreadyDeclaredException;
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
public class FunctionDeclaration {


    private static Token name;
    private static Token type;

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        List<Entry<Token, Token>> arguments = new LinkedList();

        TokenUtil.consumer(tokens);
        try {
            type = tokens.peek();
            TypeDeclaration.typeConsumer(tokens);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewSyntaticalError(tokens, INT, REAL, STRING, BOOLEAN);
        }

        name = tokens.peek();
        TokenUtil.consumeExpectedTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_PARENTHESES);
        try {
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException e) {
            Signature.paramsChecker(tokens, arguments);
            TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_PARENTHESES);
        }
        
        IdentifierWithArguments function = new Function(name, type, arguments);
        try {
            Program.GLOBAL_SCOPE.insert(function, name);
        } catch (SymbolAlreadyDeclaredException e) {
            ErrorManager.addNewSemanticalError(e);
        }
        
        blockFunctionChecker(tokens);    
    }

    public static void blockFunctionChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_KEY);
        StatementDeclaration.statementListChecker(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_KEY);
    }

    public static void returnChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        Token token = tokens.pop();
        Token nextToken = tokens.peek();
        tokens.push(token);

        boolean isEmptyReturn = TokenUtil.contains(token, Terminals.SEMICOLON);
        boolean isPrimaryReturn = TypeDeclaration.primaryChecker(token);

        if (!isEmptyReturn && !isPrimaryReturn) {
            ErrorManager.addNewSyntaticalError(tokens,
                    SEMICOLON,
                    IDENTIFIER,
                    TRUE,
                    FALSE,
                    STRING,
                    REAL,
                    INT);
        }
        try {
            if (token.getType() == TokenType.IDENTIFIER
                    && nextToken.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                callFunctionConsumer(tokens);
            } else if (token.getType() == TokenType.IDENTIFIER
                    && nextToken.thisLexameIs(SEMICOLON.getVALUE())) {
                TypeDeclaration.primaryConsumer(tokens);
            } else {
                Expressions.fullChecker(tokens);
            }
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewSyntaticalError(tokens,
                    SEMICOLON,
                    IDENTIFIER,
                    TRUE,
                    FALSE,
                    STRING,
                    REAL);
        }
        TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
    }

    public static void callFunctionConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumer(tokens);
        try {
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException ex) {
            argsListConsumer(tokens);
            TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_PARENTHESES);
        }
    }

    public static void argsListConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        argConsumer(tokens);
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumer(tokens);
            argsListConsumer(tokens);
        } else if (TypeDeclaration.primaryChecker(tokens.peek())) {
            ErrorManager.addNewSyntaticalError(tokens, COMMA, CLOSE_PARENTHESES);
            argsListConsumer(tokens);
        }
    }

    public static void argConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        EOFNotExpectedException.throwIfEmpty(tokens, INT, REAL, STRING, BOOLEAN, IDENTIFIER);
        Token token = tokens.pop();
        EOFNotExpectedException.throwIfEmpty(tokens, OPEN_PARENTHESES, COMMA);
        Token nextToken = tokens.peek();
        tokens.push(token);

        if (TypeDeclaration.primaryChecker(token)) {
            if (token.getType() == TokenType.IDENTIFIER
                    && nextToken.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                callFunctionConsumer(tokens);
            } else {
                TypeDeclaration.primaryConsumer(tokens);
            }
        } else {
            throw new SyntaxErrorException(token.getLexame(),
                    STRING,
                    BOOLEAN,
                    FALSE,
                    TRUE,
                    IDENTIFIER,
                    CALL_FUNCTION);
        }
    }
}
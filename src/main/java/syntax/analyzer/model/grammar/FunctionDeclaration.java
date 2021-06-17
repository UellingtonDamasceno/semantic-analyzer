package syntax.analyzer.model.grammar;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Function;
import semantic.analyzer.model.Identifiers.Identifier;
import semantic.analyzer.model.Identifiers.IdentifierWithArguments;
import semantic.analyzer.model.Identifiers.Procedure;
import semantic.analyzer.model.SymTable;
import semantic.analyzer.model.exceptions.IncompatibleArgumentSizeException;
import semantic.analyzer.model.exceptions.IncompatibleTypesException;
import semantic.analyzer.model.exceptions.SymbolAlreadyDeclaredException;
import semantic.analyzer.model.exceptions.UndeclaredSymbolException;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.model.grammar.Program.GLOBAL_SCOPE;
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

    private static SymTable scope;

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        scope = new SymTable();
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

        scope = ProcedureDeclaration.loadArguments(arguments, scope);
        IdentifierWithArguments function = new Function(name, type, arguments);
        try {
            Program.GLOBAL_SCOPE.insert(function, name);
        } catch (SymbolAlreadyDeclaredException e) {
            ErrorManager.addNewSemanticalError(e);
        }

        StatementDeclaration.fullChecker(tokens, scope);
    }

    public static void returnChecker(Deque<Token> tokens, SymTable parent) throws EOFNotExpectedException {
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
                callFunctionConsumer(tokens, parent);
            } else if (token.getType() == TokenType.IDENTIFIER
                    && nextToken.thisLexameIs(SEMICOLON.getVALUE())) {
                TypeDeclaration.primaryConsumer(tokens);
            } else {
                Expressions.fullChecker(tokens, parent);
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

    public static void callFunctionConsumer(Deque<Token> tokens, SymTable currentScope) throws SyntaxErrorException, EOFNotExpectedException {
        Token token = tokens.peek();

        TokenUtil.consumer(tokens);
        TokenUtil.consumer(tokens);
        try {
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException ex) {
            List<String> args = new LinkedList();

            argsListConsumer(tokens, args, currentScope);
            TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_PARENTHESES);

            try {
                int hashCode = new Procedure(token, args).hashCode();
                var found = (IdentifierWithArguments) GLOBAL_SCOPE.find(hashCode);
                if (!found.validateArguments(args)) {
                    ErrorManager.addNewSemanticalError(new IncompatibleTypesException(token));
                }
            } catch (UndeclaredSymbolException x) {
                x.setInfo(token);
                ErrorManager.addNewSemanticalError(x);
            } catch (IncompatibleTypesException ex1) {
                ex1.setInfo(token);
                ErrorManager.addNewSemanticalError(ex1);
            } catch (IncompatibleArgumentSizeException ex1) {
                ErrorManager.addNewSemanticalError(ex1);
            }
        }

    }

    public static void argsListConsumer(Deque<Token> tokens, List<String> arguments, SymTable currentScope) throws SyntaxErrorException, EOFNotExpectedException {
        argConsumer(tokens, arguments, currentScope);
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumer(tokens);
            argsListConsumer(tokens, arguments, currentScope);
        } else if (TypeDeclaration.primaryChecker(tokens.peek())) {
            ErrorManager.addNewSyntaticalError(tokens, COMMA, CLOSE_PARENTHESES);
            argsListConsumer(tokens, arguments, currentScope);
        }
    }

    public static void argConsumer(Deque<Token> tokens, List<String> arguments, SymTable currentScope) throws SyntaxErrorException, EOFNotExpectedException {
        EOFNotExpectedException.throwIfEmpty(tokens, INT, REAL, STRING, BOOLEAN, IDENTIFIER);
        Token token = tokens.pop();
        EOFNotExpectedException.throwIfEmpty(tokens, OPEN_PARENTHESES, COMMA);
        Token nextToken = tokens.peek();
        tokens.push(token);

        if (TypeDeclaration.primaryChecker(token)) {
            if (token.getType() == TokenType.IDENTIFIER) {
                try {
                    Identifier found = currentScope.find(token);
                    arguments.add(found.getType());
                } catch (UndeclaredSymbolException ex1) {
                    ex1.setInfo(token);
                    ErrorManager.addNewSemanticalError(ex1);
                }
            }
            if (token.getType() == TokenType.IDENTIFIER
                    && nextToken.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                callFunctionConsumer(tokens, currentScope);
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

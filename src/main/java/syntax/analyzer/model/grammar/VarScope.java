package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Identifier;
import semantic.analyzer.model.SymTable;
import semantic.analyzer.model.exceptions.InvalidAssignException;
import semantic.analyzer.model.exceptions.UndeclaredSymbolException;
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
public class VarScope {

    private static SymTable scope;

    public static void fullChecker(Deque<Token> tokens, SymTable parentScope) throws EOFNotExpectedException, SyntaxErrorException {
        VarScope.scope = parentScope;
        Token id = VarScope.typedVariableScoped(tokens);
        if (TokenUtil.testLexameBeforeConsume(tokens, EQUALS)) {
            Identifier found = null;
            try {
                found = scope.find(id);
                if (found.isConstant()) {
                    ErrorManager.addNewSemanticalError(new InvalidAssignException(found, id));
                }
            } catch (UndeclaredSymbolException ex) {
                ex.setInfo(id);
                ErrorManager.addNewSemanticalError(ex);
            }
            
            String type = (found == null ? "void": found.getType());
            allProductionsStartingWithEquals(tokens, type);
        } else {
            VarUsage.fullChecker(tokens, parentScope, id);
        }
    }

    public static void scopeModifierConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        try {
            TokenUtil.consumerByLexame(tokens, GLOBAL);

        } catch (SyntaxErrorException e) {
            try {
                TokenUtil.consumerByLexame(tokens, LOCAL);
            } catch (SyntaxErrorException e1) {
                throw new SyntaxErrorException(tokens.peek().getLexame(), GLOBAL, LOCAL);
            }
        }
    }

    public static Token typedVariableScoped(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        VarScope.scopeModifierConsumer(tokens);
        TokenUtil.consumerByLexame(tokens, DOT);
        Token id = tokens.peek();
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        return id;
    }

    private static void allProductionsStartingWithEquals(Deque<Token> tokens, String type) throws EOFNotExpectedException, SyntaxErrorException {
        try {
            VarDeclaration.variableDeclaratorConsumer(tokens, scope, type);
        } catch (SyntaxErrorException e) {
            EOFNotExpectedException.throwIfEmpty(tokens, DOT,
                    EQUALS,
                    OPEN_BRACKET);
            Token token = tokens.pop();
            EOFNotExpectedException.throwIfEmpty(tokens, IDENTIFIER,
                    LOCAL,
                    GLOBAL,
                    TRUE,
                    FALSE,
                    INT,
                    STRING,
                    REAL);
            Token nextToken = tokens.pop();
            EOFNotExpectedException.throwIfEmpty(tokens, DOT,
                    OPEN_PARENTHESES,
                    EQUALS,
                    CLOSE_BRACKET,
                    SEMICOLON,
                    OPEN_BRACKET);
            Token nextNextToken = tokens.peek();
            tokens.push(nextToken);
            tokens.push(token);
            if (nextNextToken.thisLexameIs(SEMICOLON.getVALUE())
                    || nextNextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                TokenUtil.consumeExpectedTokenByLexame(tokens, EQUALS);
                try {
                    Token id = VarScope.typedVariableScoped(tokens);
                    try {
                        scope.find(id);
                    } catch (UndeclaredSymbolException exx) {
                        exx.setInfo(id);
                        ErrorManager.addNewSemanticalError(exx);
                    }
                    if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                        Arrays.dimensionConsumer(tokens);
                    }
                } catch (SyntaxErrorException ex) {
                    try {
                        scope.find(tokens.peek());
                    } catch (UndeclaredSymbolException exx) {
                        exx.setInfo(tokens.peek());
                        ErrorManager.addNewSemanticalError(exx);
                    }
                    TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
                    if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                        Arrays.dimensionConsumer(tokens);
                    }
                }
            } else {
                VarUsage.fullChecker(tokens, scope, nextToken);
            }
        }
    }
}

package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.SymTable;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.TokenUtil;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarScope {

    private static SymTable parentScope;

    public static void fullChecker(Deque<Token> tokens, SymTable parentScope) throws EOFNotExpectedException, SyntaxErrorException {
        VarScope.parentScope = parentScope;
        typedVariableScoped(tokens);
        if (TokenUtil.testLexameBeforeConsume(tokens, EQUALS)) {
            allProductionsStartingWithEquals(tokens);
        } else {
            VarUsage.fullChecker(tokens, parentScope);
        }
    }

    public static void scopeModifierConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        try {
            TokenUtil.consumerByLexame(tokens, GLOBAL);
            parentScope = Program.GLOBAL_SCOPE;
        } catch (SyntaxErrorException e) {
            try {
                TokenUtil.consumerByLexame(tokens, LOCAL);
            } catch (SyntaxErrorException e1) {
                throw new SyntaxErrorException(tokens.peek().getLexame(), GLOBAL, LOCAL);
            }
        }
    }

    public static void typedVariableScoped(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        VarScope.scopeModifierConsumer(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, DOT);
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
    }

    private static void allProductionsStartingWithEquals(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        try {
            VarDeclaration.variableDeclaratorConsumer(tokens);
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
                    VarScope.scopeModifierConsumer(tokens);
                    if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                        Arrays.dimensionConsumer(tokens);
                    }
                } catch (SyntaxErrorException ex) {
                    TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
                    if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                        Arrays.dimensionConsumer(tokens);
                    }
                }
            } else {
                VarUsage.fullChecker(tokens, parentScope);
            }
        }
    }
}

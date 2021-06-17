package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.ComplexIdentifier;
import semantic.analyzer.model.Identifiers.Identifier;
import semantic.analyzer.model.SymTable;
import semantic.analyzer.model.exceptions.InvalidAssingException;
import semantic.analyzer.model.exceptions.UndeclaredSymbolException;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import syntax.analyzer.util.TokenUtil;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarUsage {

    private static SymTable scope;

    public static void fullChecker(Deque<Token> tokens, SymTable parentScope, Token id) throws EOFNotExpectedException, SyntaxErrorException {
        VarUsage.scope = parentScope;

        if (TokenUtil.testLexameBeforeConsume(tokens, EQUALS)) {
            try {
                Identifier found = parentScope.find(id);
                if (found.isConstant()) {
                    ErrorManager.addNewSemanticalError(new InvalidAssingException(found, id));
                }
            } catch (UndeclaredSymbolException ex) {
                ex.setInfo(id);
                ErrorManager.addNewSemanticalError(ex);
            }
            VarDeclaration.variableDeclaratorConsumer(tokens, parentScope);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
            try {
                ComplexIdentifier found = (ComplexIdentifier) Program.GLOBAL_SCOPE.find(id);
                StructDeclaration.structUsageConsumer(tokens, found.getSymTable());
            } catch (UndeclaredSymbolException ex) {
                ex.setInfo(id);
                ErrorManager.addNewSemanticalError(ex);
                StructDeclaration.structUsageConsumer(tokens);
            }
            allProductionsWithDot(tokens);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
            try {
                parentScope.find(id);
            } catch (UndeclaredSymbolException ex) {
                ex.setInfo(id);
                ErrorManager.addNewSemanticalError(ex);
            }
            Arrays.dimensionConsumer(tokens);
            allProductionsWithBracket(tokens);
        } else {
            throw new SyntaxErrorException(tokens.peek().getLexame(), EQUALS, DOT, OPEN_BRACKET);
        }
    }

    private static void allProductionsWithDot(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        if (!TokenUtil.testLexameBeforeConsume(tokens, SEMICOLON)) {
            TokenUtil.consumerByLexame(tokens, EQUALS);
            try {
                Token id = VarScope.typedVariableScoped(tokens);
                try {
                    scope.find(id);
                } catch (UndeclaredSymbolException ex) {
                    ex.setInfo(id);
                    ErrorManager.addNewSemanticalError(ex);
                }
                if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
                    StructDeclaration.structUsageConsumer(tokens, scope);
                }
            } catch (SyntaxErrorException e1) {
                EOFNotExpectedException.throwIfEmpty(tokens, IDENTIFIER);
                Token token = tokens.pop();
                EOFNotExpectedException.throwIfEmpty(tokens, OPEN_PARENTHESES, DOT, OPEN_BRACKET);
                Token nextToken = tokens.peek();
                tokens.push(token);

                if (token.getType() == TokenType.IDENTIFIER
                        && nextToken.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                    FunctionDeclaration.callFunctionConsumer(tokens);
                } else if (token.getType() == TokenType.IDENTIFIER
                        && nextToken.thisLexameIs(DOT.getVALUE())) {
                    TokenUtil.consumer(tokens);
                    try {
                        var found = (ComplexIdentifier) Program.GLOBAL_SCOPE.find(token);
                        StructDeclaration.structUsageConsumer(tokens, found.getSymTable());
                    } catch (UndeclaredSymbolException ex) {
                        ex.setInfo(token);
                        ErrorManager.addNewSemanticalError(ex);
                        StructDeclaration.structUsageConsumer(tokens);
                    }
                } else if (token.getType() == TokenType.IDENTIFIER
                        && nextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                    TokenUtil.consumer(tokens);
                    Arrays.dimensionConsumer(tokens);
                } else {
                    Expressions.fullChecker(tokens, scope);
                }
            }
        }
    }

    private static void allProductionsWithBracket(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        try {
            TokenUtil.consumerByLexame(tokens, SEMICOLON);
        } catch (SyntaxErrorException e) {
            TokenUtil.consumeExpectedTokenByLexame(tokens, EQUALS);
            try {
                Token id = VarScope.typedVariableScoped(tokens);
                if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
                    StructDeclaration.structUsageConsumer(tokens, scope);
                } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                    Arrays.dimensionConsumer(tokens);
                }
            } catch (SyntaxErrorException e1) {
                EOFNotExpectedException.throwIfEmpty(tokens, IDENTIFIER);
                Token token = tokens.pop();
                EOFNotExpectedException.throwIfEmpty(tokens, DOT, OPEN_BRACKET);
                Token nextToken = tokens.peek();
                tokens.push(token);
                if (token.getType() == TokenType.IDENTIFIER
                        && nextToken.thisLexameIs(DOT.getVALUE())) {
                    TokenUtil.consumer(tokens);
                    try {
                        var found = (ComplexIdentifier) Program.GLOBAL_SCOPE.find(token);
                        StructDeclaration.structUsageConsumer(tokens, found.getSymTable());
                    } catch (UndeclaredSymbolException ex) {
                        ex.setInfo(token);
                        ErrorManager.addNewSemanticalError(ex);
                        StructDeclaration.structUsageConsumer(tokens);
                    }
                } else if (token.getType() == TokenType.IDENTIFIER
                        && nextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                    TokenUtil.consumer(tokens);
                    try {
                        scope.find(token);
                    } catch (UndeclaredSymbolException ex) {
                        ex.setInfo(token);
                        ErrorManager.addNewSemanticalError(ex);
                    }
                    Arrays.dimensionConsumer(tokens);
                } else if (TypeDeclaration.primaryChecker(token)) {
                    try {
                        scope.find(token);
                    } catch (UndeclaredSymbolException ex) {
                        ex.setInfo(token);
                        ErrorManager.addNewSemanticalError(ex);
                    }
                    TypeDeclaration.primaryConsumer(tokens);
                } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_KEY)) {
                    Arrays.initialize(tokens);
                }
            }
        }
    }
}

package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.TokenUtil;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarUsage {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        if (TokenUtil.testLexameBeforeConsume(tokens, EQUALS)) {
            VarDeclaration.variableDeclaratorConsumer(tokens);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
            allProductionsWithDot(tokens);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
            allProductionsWithBracket(tokens);
        } else {
            throw new SyntaxErrorException(tokens.peek().getLexame(), EQUALS, DOT, OPEN_BRACKET);
        }
    }

    private static void allProductionsWithDot(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        if (!TokenUtil.testLexameBeforeConsume(tokens, SEMICOLON)) {
            TokenUtil.consumerByLexame(tokens, EQUALS);
            try {
                VarScope.typedVariableScoped(tokens);
                if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
                    StructDeclaration.structUsageConsumer(tokens);
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
                    StructDeclaration.structUsageConsumer(tokens);
                } else if (token.getType() == TokenType.IDENTIFIER
                        && nextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                    TokenUtil.consumer(tokens);
                    Arrays.dimensionConsumer(tokens);
                } else {
                    Expressions.fullChecker(tokens);
                }
            }
        }
    }

    private static void allProductionsWithBracket(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        Arrays.dimensionConsumer(tokens);
        try {
            TokenUtil.consumerByLexame(tokens, SEMICOLON);
        } catch (SyntaxErrorException e) {
            TokenUtil.consumeExpectedTokenByLexame(tokens, EQUALS);
            try {
                VarScope.typedVariableScoped(tokens);
                if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
                    StructDeclaration.structUsageConsumer(tokens);
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
                    StructDeclaration.structUsageConsumer(tokens);
                } else if (token.getType() == TokenType.IDENTIFIER
                        && nextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                    TokenUtil.consumer(tokens);
                    Arrays.dimensionConsumer(tokens);
                } else if (TypeDeclaration.primaryChecker(token)) {
                    TypeDeclaration.primaryConsumer(tokens);
                } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_KEY)) {
                    Arrays.initialize(tokens);
                }
            }
        }
    }
}

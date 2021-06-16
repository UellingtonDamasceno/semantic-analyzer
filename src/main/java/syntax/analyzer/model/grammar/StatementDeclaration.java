package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class StatementDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_KEY);
        statementListChecker(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_KEY);
    }

    public static void statementListChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        simpleStatement(tokens);
        if (!tokens.isEmpty() && !tokens.peek().thisLexameIs(CLOSE_KEY.getVALUE())) {
            statementListChecker(tokens);
        }
    }

    private static void simpleStatement(Deque<Token> tokens) throws EOFNotExpectedException {
        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(
                    READ,
                    PRINT,
                    VAR,
                    CONST,
                    IDENTIFIER,
                    IF,
                    WHILE,
                    RETURN);
        }

        Token token = tokens.peek();
        try {
            if (token.thisLexameIs(READ.getVALUE())) {
                Read.fullChecker(tokens);
                TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
            } else if (token.thisLexameIs(PRINT.getVALUE())) {
                Print.fullChecker(tokens);
                TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
            } else if (token.thisLexameIs(VAR.getVALUE())) {
//                VarDeclaration.fullChecker(tokens);
            } else if (token.thisLexameIs(CONST.getVALUE())) {
//                ConstDeclaration.fullChecker(tokens);
            } else if (token.thisLexameIs(RETURN.getVALUE())) {
                FunctionDeclaration.returnChecker(tokens);
            } else if (token.thisLexameIs(IF.getVALUE())) {
                IfElse.fullChecker(tokens);
            } else if (token.thisLexameIs(WHILE.getVALUE())) {
                WhileDeclaration.fullChecker(tokens);
            } else if (token.thisLexameIs(TYPEDEF.getVALUE())) {
                StructDeclaration.fullChecker(tokens);
            } else if (token.thisLexameIs(GLOBAL.getVALUE()) || token.thisLexameIs(LOCAL.getVALUE())) {
                VarScope.fullChecker(tokens);
                TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
            } else if (token.getType() == TokenType.IDENTIFIER) {
                Token t1 = tokens.pop();
                EOFNotExpectedException.throwIfEmpty(tokens, OPEN_PARENTHESES, IDENTIFIER);
                Token t2 = tokens.peek();
                tokens.push(t1);
                if (t2.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                    FunctionDeclaration.callFunctionConsumer(tokens);
                    TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
                } else {
                    TokenUtil.consumer(tokens);
                    VarUsage.fullChecker(tokens);
                    TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
                }
            } else {
                ErrorManager.consumer(tokens);
            }
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewSyntaticalError(e);
        }

    }

}

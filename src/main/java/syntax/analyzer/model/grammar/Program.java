package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.SymTable;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Program {

    public static SymTable GLOBAL_SCOPE = new SymTable();

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        globalStatementsList(tokens);
    }

    public static void globalStatementsList(Deque<Token> tokens) throws EOFNotExpectedException {
        globalStatementsConsumer(tokens);
        if (!tokens.isEmpty()) {
            globalStatementsList(tokens);
        }
    }

    public static void globalStatementsConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(
                    VAR,
                    CONST,
                    FUNCTION,
                    PROCEDURE,
                    TYPEDEF,
                    STRUCT);
        }
        try {
            Token token = tokens.peek();
            if (token.thisLexameIs(FUNCTION.getVALUE())) {
                FunctionDeclaration.fullChecker(tokens);
            } else if (token.thisLexameIs(TYPEDEF.getVALUE())) {
                StructDeclaration.fullChecker(tokens);
            } else if (token.thisLexameIs(VAR.getVALUE())) {
                VarDeclaration.fullChecker(tokens);
            } else if (token.thisLexameIs(CONST.getVALUE())) {
                ConstDeclaration.fullChecker(tokens, GLOBAL_SCOPE);
            } else if (token.thisLexameIs(PROCEDURE.getVALUE())) {
                token = tokens.pop();
                EOFNotExpectedException.throwIfEmpty(tokens, START, IDENTIFIER);
                Token nextToken = tokens.peek();
                tokens.push(token);
                if (nextToken.thisLexameIs(START.getVALUE())) {
                    ProcedureMain.fullChecker(tokens);
                } else {
                    ProcedureDeclaration.fullChecker(tokens);
                }
            } else if (token.thisLexameIs(OPEN_KEY.getVALUE())) {
                ErrorManager.genericBlockConsumer(tokens);
            } else if (token.getType() == TokenType.IDENTIFIER
                    || TypeDeclaration.typeChecker(token)) {
                Signature.fullChecker(tokens);
            } else {
                ErrorManager.consumer(tokens);
            }
        } catch (SyntaxErrorException e1) {
            ErrorManager.addNewInternalError(e1);
        }
    }
}

package syntax.analyzer.model.grammar;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.IdentifierWithArguments;
import semantic.analyzer.model.Identifiers.Procedure;
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
public class ProcedureDeclaration {


    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        List<Entry<Token, Token>> arguments = new LinkedList();

        TokenUtil.consumer(tokens);

        Token name = tokens.peek();
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_PARENTHESES);

        try {
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException e) {
            Signature.paramsChecker(tokens, arguments);
            TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_PARENTHESES);
        }

        IdentifierWithArguments procedure = new Procedure(arguments, name);
        try {
            Program.GLOBAL_SCOPE.insert(procedure, name);
        } catch (SymbolAlreadyDeclaredException ex) {
            ErrorManager.addNewSemanticalError(ex);
        }
        StatementDeclaration.fullChecker(tokens);
    }
}

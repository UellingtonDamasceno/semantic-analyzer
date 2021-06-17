package syntax.analyzer.model.grammar;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.IdentifierWithArguments;
import semantic.analyzer.model.Identifiers.Procedure;
import semantic.analyzer.model.Identifiers.SimpleIdentifier;
import semantic.analyzer.model.SymTable;
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

    private static SymTable table;

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
        table = loadArguments(arguments, new SymTable());
        IdentifierWithArguments procedure = new Procedure(arguments, name);
        try {
            Program.GLOBAL_SCOPE.insert(procedure, name);
        } catch (SymbolAlreadyDeclaredException ex) {
            ErrorManager.addNewSemanticalError(ex);
        }
        StatementDeclaration.fullChecker(tokens, table);
    }

    public static SymTable loadArguments(List<Entry<Token, Token>> arguments, SymTable table) {
        arguments.stream().map(typedArg -> {
            Token type = typedArg.getKey();
            Token name = typedArg.getValue();
            SimpleIdentifier id = new SimpleIdentifier(type, name, false);
            return Map.entry(id, typedArg.getValue());
        }).forEach(entry -> {
            try {
                table.insert(entry.getKey(), entry.getValue());
            } catch (SymbolAlreadyDeclaredException ex) {
                ErrorManager.addNewSemanticalError(ex);
            }
        });
        return table;
    }
}

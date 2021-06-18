package syntax.analyzer.model.grammar;

import java.util.Deque;
import java.util.List;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Procedure;
import semantic.analyzer.model.SymTable;
import semantic.analyzer.model.exceptions.SymbolAlreadyDeclaredException;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ProcedureMain {

    public static SymTable MAIN_SCOPE;

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        Token start = tokens.peek();
        try {
            Program.GLOBAL_SCOPE.insert(new Procedure(List.of(), start), start);
            ProcedureMain.MAIN_SCOPE = new SymTable();
        } catch (SymbolAlreadyDeclaredException ex) {
            ErrorManager.addNewSemanticalError(ex);
            ErrorManager.CAN_ADD = false;
        }
        TokenUtil.consumer(tokens);
        StatementDeclaration.fullChecker(tokens, MAIN_SCOPE != null ? MAIN_SCOPE : new SymTable(), "void");
        ErrorManager.CAN_ADD = true;
    }
}

package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.SymTable;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class WhileDeclaration {

    public static void fullChecker(Deque<Token> tokens, SymTable parent) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        Expressions.fullChecker(tokens);
        StatementDeclaration.fullChecker(tokens, new SymTable());
    }

}

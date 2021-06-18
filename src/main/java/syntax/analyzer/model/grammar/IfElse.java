package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.SymTable;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class IfElse {

    private static SymTable scope;
    private static String type;
    
    public static void fullChecker(Deque<Token> tokens, SymTable parent, String type) throws EOFNotExpectedException, SyntaxErrorException {
        IfElse.type = type;
        IfElse.scope = parent;
        ifConsumer(tokens);
        elseConsumer(tokens);
    }

    public static void ifConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        TokenUtil.consumer(tokens);
        Expressions.fullChecker(tokens, scope);
        TokenUtil.consumeExpectedTokenByLexame(tokens, THEN);
        StatementDeclaration.fullChecker(tokens, new SymTable(scope), type);
    }

    public static void elseConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        if (TokenUtil.testLexameBeforeConsume(tokens, ELSE)) {
            TokenUtil.consumer(tokens);
            StatementDeclaration.fullChecker(tokens, new SymTable(scope), type);
        }
    }
}

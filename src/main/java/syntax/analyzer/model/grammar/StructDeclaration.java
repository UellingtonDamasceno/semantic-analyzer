package syntax.analyzer.model.grammar;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.ComplexIdentifier;
import semantic.analyzer.model.SymTable;
import semantic.analyzer.model.exceptions.SymbolAlreadyDeclaredException;
import semantic.analyzer.model.exceptions.UndeclaredSymbolException;
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
public class StructDeclaration {

    private static SymTable table;

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        table = new SymTable();
        Token inhereted = null;
        ComplexIdentifier struct = null;

        TokenUtil.consumer(tokens);
        TokenUtil.consumerByLexame(tokens, STRUCT);

        if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_KEY)) {
            TokenUtil.consumer(tokens);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, EXTENDS)) {
            TokenUtil.consumer(tokens);
            inhereted = tokens.peek();
            TokenUtil.consumeExpectedTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_KEY);
        } else {
            throw new SyntaxErrorException(tokens.peek().getLexame(), OPEN_KEY, EXTENDS);
        }
        structDefConsumer(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_KEY);
        try {
            Token token = tokens.peek();
            String name = token.getLexame().getLexame();
            if (inhereted == null) {
                struct = new ComplexIdentifier(name, table, name);
            } else {
                try {
                    List<String> loadInhereted = new LinkedList();
                    loadInhereted.add(name);
                    loadInhereted.addAll(ComplexIdentifier.loadInhereted(Program.GLOBAL_SCOPE, inhereted.getLexame().getLexame().hashCode()));
                    struct = new ComplexIdentifier(name, table, loadInhereted);
                } catch (UndeclaredSymbolException ex) {
                    ex.setLine(inhereted.getLexame().getLine());
                    ex.setName(inhereted.getLexame().getLexame());
                    ErrorManager.addNewSemanticalError(ex);
                }
            }
            if (struct != null) {
                try {
                    Program.GLOBAL_SCOPE.insert(struct, tokens.peek());
                } catch (SymbolAlreadyDeclaredException ex) {
                    ErrorManager.addNewSemanticalError(ex);
                }
            }

            TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewSyntaticalError(tokens, IDENTIFIER);
        }
        TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
    }

    public static void structDefConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        if (TokenUtil.testLexameBeforeConsume(tokens, VAR)) {
            VarDeclaration.fullChecker(tokens, table);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, CONST)) {
            ConstDeclaration.fullChecker(tokens, table);
        } else {
            throw new SyntaxErrorException(tokens.peek().getLexame(), VAR, CONST);
        }
        EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
        if (TokenUtil.testLexameBeforeConsume(tokens, VAR) || TokenUtil.testLexameBeforeConsume(tokens, CONST)) {
            structDefConsumer(tokens);
        }
    }

    public static void structUsageConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumeExpectedTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
    }
}

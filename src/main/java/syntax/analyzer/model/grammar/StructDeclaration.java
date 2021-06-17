package syntax.analyzer.model.grammar;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.ComplexIdentifier;
import static semantic.analyzer.model.Identifiers.ComplexIdentifier.loadInhereted;
import semantic.analyzer.model.Identifiers.Identifier;
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
        ComplexIdentifier struct, parent = null;

        TokenUtil.consumer(tokens);
        TokenUtil.consumerByLexame(tokens, STRUCT);

        if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_KEY)) {
            TokenUtil.consumer(tokens);
            table = new SymTable();
        } else if (TokenUtil.testLexameBeforeConsume(tokens, EXTENDS)) {
            TokenUtil.consumer(tokens);
            try {
                parent = loadInhereted(Program.GLOBAL_SCOPE, tokens.peek());
                table = new SymTable(parent.getSymTable());
            } catch (UndeclaredSymbolException ex) {
                ex.setInfo(tokens.peek());
                ErrorManager.addNewSemanticalError(ex);
            }
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
            if (parent == null) {
                struct = new ComplexIdentifier(name, table, name);
            } else {
                List<String> inheretedList = new LinkedList();
                inheretedList.add(name);
                inheretedList.addAll(parent.getInhetedTypes());
                struct = new ComplexIdentifier(name, table, inheretedList);
            }
            try {
                Program.GLOBAL_SCOPE.insert(struct, tokens.peek());
            } catch (SymbolAlreadyDeclaredException ex) {
                ErrorManager.addNewSemanticalError(ex);
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

    public static String structUsageConsumer(Deque<Token> tokens, SymTable symTable) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        Identifier find = null;
        try {
            find = symTable.find(tokens.peek());
        } catch (UndeclaredSymbolException ex) {
            ex.setInfo(tokens.peek());
            ErrorManager.addNewSemanticalError(ex);
        }
        Token token = tokens.peek();
        TokenUtil.consumeExpectedTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);

        return find == null? "void" : find.getType();
    }
}

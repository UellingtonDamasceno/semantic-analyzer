package syntax.analyzer.util;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.SymTable;
import syntax.analyzer.model.SyntaticalError;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.model.grammar.ProcedureMain;
import syntax.analyzer.model.grammar.Program;
import syntax.analyzer.model.grammar.StatementDeclaration;
import syntax.analyzer.model.grammar.VarDeclaration;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ErrorManager {

    private static List<SyntaxErrorException> syntaticalErrors = new LinkedList();
    private static List<Exception> semanticalErrors = new LinkedList();
    private static List<String> unexpectedToken = new LinkedList();
    private static EOFNotExpectedException E;
    public static boolean CAN_ADD = true;

    public static void genericBlockConsumer(Deque<Token> tokens, String type) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        try {
            VarDeclaration.typedVariableConsumer(tokens);
            findNext(tokens, CLOSE_KEY);
            TokenUtil.consumer(tokens);
        } catch (SyntaxErrorException e) {
            try {
                VarDeclaration.varArgsConsumer(tokens);
                findNext(tokens, CLOSE_KEY);
                TokenUtil.consumer(tokens);
            } catch (SyntaxErrorException e1) {
                StatementDeclaration.statementListChecker(tokens, type);
                findNext(tokens, CLOSE_KEY);
                TokenUtil.consumer(tokens);
            }
        }
    }

    public static void consumer(Deque<Token> tokens) {
        unexpectedToken.add("TOKEN INESPERADO: \""
                + tokens.peek().getLexame().getLexame()
                + "\" NA LINHA: " + tokens.peek().getLexame().getLine());
        TokenUtil.consumer(tokens);
    }

    private static void findNext(Deque<Token> tokens, Terminals terminal) throws EOFNotExpectedException {
        if (!TokenUtil.testLexameBeforeConsume(tokens, terminal)) {
            consumer(tokens);
            findNext(tokens, terminal);
        }
    }

    public static void addNewSyntaticalError(Deque<Token> tokens, Terminals... terminals) {
        if (CAN_ADD) {
            syntaticalErrors.add(new SyntaxErrorException(tokens.peek().getLexame(), terminals));
        }
    }

    public static void addNewSyntaticalError(SyntaxErrorException e) {
        if (CAN_ADD) {
            syntaticalErrors.add(e);
        }
    }

    public static List<String> getErrors(boolean showUnexpectedTokens, boolean showSyntaticalErrors) {
        List<String> lines = new LinkedList();

        if (showSyntaticalErrors) {
            lines = syntaticalErrors.stream()
                    .map(SyntaxErrorException::getSyntaticalError)
                    .map(SyntaticalError::toString)
                    .collect(toList());
        }

        lines.addAll(semanticalErrors.stream()
                .map(Exception::getMessage)
                .collect(toList()));

        if (showUnexpectedTokens && !unexpectedToken.isEmpty()) {
            lines.addAll(unexpectedToken);
        }
        if (E != null) {
            lines.add(E.getMessage());
        }
        return lines.isEmpty()
                ? List.of("O CÓDIGO FOI ANALISADO COM SUCESSO!")
                : lines;
    }

    public static void setEOF(EOFNotExpectedException ex) {
        E = ex;
    }

    public static void clear() {
        syntaticalErrors.clear();
        E = null;
        unexpectedToken.clear();
        semanticalErrors.clear();
        ProcedureMain.MAIN_SCOPE = null;
        Program.GLOBAL_SCOPE = new SymTable();
    }

    public static void addNewSemanticalError(Exception e) {
        if (CAN_ADD) {
            semanticalErrors.add(e);
        }
    }
}

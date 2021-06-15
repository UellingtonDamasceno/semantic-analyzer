package syntax.analyzer.util;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.SyntaticalError;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.model.grammar.StatementDeclaration;
import syntax.analyzer.model.grammar.VarDeclaration;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ErrorManager {

    private static List<SyntaxErrorException> ERRORS = new LinkedList();
    private static List<String> unexpectedToken = new LinkedList();
    private static EOFNotExpectedException E;

    public static void genericBlockConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
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
                StatementDeclaration.statementListChecker(tokens);
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

    public static void addNewInternalError(Deque<Token> tokens, Terminals ... terminals) {
        ERRORS.add(new SyntaxErrorException(tokens.peek().getLexame(), terminals));
    }
    
    public static void addNewInternalError(SyntaxErrorException e) {
        ERRORS.add(e);
    }

    public static List<String> getErrors(boolean showUnexpectedTokens) {
        List<String> lines = ERRORS.stream()
                .map(SyntaxErrorException::getSyntaticalError)
                .map(SyntaticalError::toString)
                .collect(toList());

        if (showUnexpectedTokens && !unexpectedToken.isEmpty()) {
            lines.addAll(unexpectedToken);
        }
        if (E != null) {
            lines.add(E.getMessage());
        }
        return lines.isEmpty()
                ? List.of("NÃO FORAM ENCONTRADOS ERROS SINTATICOS!")
                : lines;
    }

    public static void setEOF(EOFNotExpectedException ex) {
        E = ex;
    }

    public static void clear() {
        ERRORS.clear();
        E = null;
        unexpectedToken.clear();
    }
}

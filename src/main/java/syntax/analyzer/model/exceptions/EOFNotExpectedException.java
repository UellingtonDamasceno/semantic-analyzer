package syntax.analyzer.model.exceptions;

import java.util.Arrays;
import java.util.Deque;
import static java.util.stream.Collectors.joining;
import lexical.analyzer.model.Token;
import syntax.analyzer.util.Terminals;

/**
 *
 * @author Antônio neto e Uellington Damasceno
 */
public class EOFNotExpectedException extends Exception {

    public EOFNotExpectedException(Terminals... terminals) {
        super("O arquivou terminou inesperadamente! O analisador esperava um: " 
                + Arrays.asList(terminals)
                        .stream()
                        .map(Terminals::getVALUE)
                        .collect(joining("ou", "\"", "\"")));
    }

    public EOFNotExpectedException(Terminals terminal) {
        super("O arquivou terminou inesperadamente! O analisador esperava um: " + terminal.getVALUE());
    }

    public static void throwIfEmpty(Deque<Token> tokens, Terminals... terminals) throws EOFNotExpectedException {
        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(terminals);
        }
    }

}

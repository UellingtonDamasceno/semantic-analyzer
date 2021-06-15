package syntax.analyzer.model.exceptions;

import java.util.Arrays;
import static java.util.stream.Collectors.toList;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Lexame;
import syntax.analyzer.model.SyntaticalError;
import syntax.analyzer.util.Terminals;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SyntaxErrorException extends Exception {

    private final SyntaticalError error;

    public SyntaxErrorException(Lexame lexame, Terminals... errors) {
        super();
        this.error = new SyntaticalError(TokenType.ERROR_SYNTATICAL, lexame,
                Arrays.asList(errors)
                        .stream()
                        .map(Terminals::getVALUE)
                        .collect(toList()));
    }

    public SyntaticalError getSyntaticalError() {
        return error;
    }
}

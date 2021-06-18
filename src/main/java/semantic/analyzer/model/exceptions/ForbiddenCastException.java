
package semantic.analyzer.model.exceptions;

import java.util.Arrays;
import lexical.analyzer.model.Token;

/**
 *
 * @author acmne
 */
public class ForbiddenCastException extends Exception {

    public ForbiddenCastException(Token token, String... types) {
        super("Erro Semântico: O símbolo "
                + token.getLexame().getLexame()
                + " na linha " + token.getLexame().getLine()
                + " não pôde ser convertido para "
                + Arrays.toString(types) + "!");
    }
    
    public ForbiddenCastException(int line, String type1, String type2){
        super("Erro Semântico: "
                + type1
                + " na linha " + line
                + " não pôde ser convertido para "
                + type2+ "!");
    }

}


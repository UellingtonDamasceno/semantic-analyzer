package semantic.analyzer.model.exceptions;

import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Identifier;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SymbolAlreadyDeclaredException extends Exception {

    public SymbolAlreadyDeclaredException(Identifier id, Token token) {
        super("Erro Semântico: O símbolo \""
                + id.getName() + "\" na linha "
                + token.getLexame().getLine() + " já foi declarado previamente!");
    }

}

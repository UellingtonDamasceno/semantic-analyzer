package semantic.analyzer.model.exceptions;

import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Identifier;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class InvalidAssingException extends Exception{
    public InvalidAssingException(Identifier id, Token token){
        super("Erro Semântico: O simbolo "+ id.getName() + " na linha "+ 
                token.getLexame().getLine() + " não pôde ser atribuido.");
    }
}

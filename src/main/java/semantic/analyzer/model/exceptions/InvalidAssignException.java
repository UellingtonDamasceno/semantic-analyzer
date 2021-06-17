package semantic.analyzer.model.exceptions;

import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Identifier;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class InvalidAssignException extends Exception{
    public InvalidAssignException(Identifier id, Token token){
        super("Erro Semântico: O simbolo "+ id.getName() + " na linha "+ 
                token.getLexame().getLine() + " não pôde ser atribuido.");
    }
}

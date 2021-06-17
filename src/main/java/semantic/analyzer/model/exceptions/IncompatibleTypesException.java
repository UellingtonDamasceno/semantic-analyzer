package semantic.analyzer.model.exceptions;

import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Lexame;
import lexical.analyzer.model.Token;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class IncompatibleTypesException extends Exception {

    private Token token;

    public IncompatibleTypesException() {
        this.token = new Token(TokenType.SYMBOL, new Lexame("Error", -1, -1));
    }

    public IncompatibleTypesException(Token token) {
        this.token = token;
    }

    public void setInfo(Token token) {
        this.token = token;
    }

    @Override
    public String getMessage() {
        return "Erro Semântico: O símbolo \"" + token.getLexame().getLexame()
                + "\" na linha " + token.getLexame().getLine()
                + " recebeu parâmetros que não condizem com a declaração";
    }

}

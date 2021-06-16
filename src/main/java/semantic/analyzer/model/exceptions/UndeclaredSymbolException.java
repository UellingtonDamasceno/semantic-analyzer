package semantic.analyzer.model.exceptions;

import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Identifier;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class UndeclaredSymbolException extends Exception {
    
    private String name;
    private int line;

    public UndeclaredSymbolException() {
        super();
    }
    
    public UndeclaredSymbolException(Identifier id, Token token) {
        super();
        this.name = id.getName();
        this.line = token.getLexame().getLine();
    }
    
    @Override
    public String getMessage(){
        return "Erro Semântico: O símbolo \""
                + name + "\" na linha "
                + line + " é desconhecido!"
                        + " Verifique se o mesmo não foi declarado abaixo :)";
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setLine(int line) {
        this.line = line;
    }
    
    
    
}

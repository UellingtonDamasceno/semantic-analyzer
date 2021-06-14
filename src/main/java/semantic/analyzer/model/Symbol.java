package semantic.analyzer.model;

import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Identifier;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Symbol {

    private Token token;
    private Identifier identifier;

    public Symbol(Token token, Identifier identifier) {
        this.token = token;
        this.identifier = identifier;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Symbol && ((Symbol) obj).hashCode() == this.hashCode());
    }
}

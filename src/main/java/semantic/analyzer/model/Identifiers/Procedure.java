package semantic.analyzer.model.Identifiers;

import java.util.List;
import java.util.Map.Entry;
import lexical.analyzer.model.Token;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Procedure extends IdentifierWithArguments {

    public Procedure(List<Entry<Token, Token>> args, Token name) {
        super(args, name);
    }

    public Procedure(String identifier, List<Entry<String, String>> args) {
        super(args, identifier);
    }

    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof IdentifierWithArguments && ((IdentifierWithArguments) obj).hashCode() == this.hashCode();
    }

}

package semantic.analyzer.model.Identifiers;

import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Procedure extends IdentifierWithArguments {

    public Procedure(String identifier, List<Entry<String, String>> args) {
        super(args, identifier);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Procedure && ((Procedure) obj).hashCode() == this.hashCode();
    }

}
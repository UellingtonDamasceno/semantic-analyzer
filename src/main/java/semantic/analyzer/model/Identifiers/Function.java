package semantic.analyzer.model.Identifiers;

import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Function extends IdentifierWithArguments {

    private final String typeReturn;

    public Function(String name, String typeReturn, List<Entry<String, String>> args) {
        super(name, args);
        this.typeReturn = typeReturn;
    }

    public boolean returnTypeIs(String type) {
        return this.typeReturn.equals(type);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Function && ((Function) obj).hashCode() == this.hashCode();
    }
}

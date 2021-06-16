package semantic.analyzer.model.Identifiers;

import java.util.List;
import semantic.analyzer.model.SymTable;
import semantic.analyzer.model.exceptions.UndeclaredSymbolException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ComplexIdentifier extends Identifier {

    private SymTable symTable;
    private final List<String> inhetedTypes;

    public ComplexIdentifier(String name, String... types) {
        this(name, new SymTable(), List.of(types));
    }

    public ComplexIdentifier(String name, SymTable symTable, String... types) {
        this(name, symTable, List.of(types));
    }

    public ComplexIdentifier(String name, SymTable symTable, List<String> inhetedTypes) {
        super(name);
        this.symTable = symTable;
        this.inhetedTypes = inhetedTypes;
        System.out.println(inhetedTypes);
    }

    public boolean hasParent() {
        return this.inhetedTypes.size() > 1;
    }

    public boolean isInstance(String parentType) {
        return inhetedTypes.contains(parentType);
    }

    public static List<String> loadInhereted(SymTable table, Integer parent) throws UndeclaredSymbolException {
        ComplexIdentifier struct = (ComplexIdentifier) table.find(parent);
        return struct.inhetedTypes;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ComplexIdentifier
                && ((ComplexIdentifier) obj).hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}

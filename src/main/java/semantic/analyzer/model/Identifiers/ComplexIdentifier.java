package semantic.analyzer.model.Identifiers;

import java.util.List;
import semantic.analyzer.model.SymTable;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ComplexIdentifier extends Identifier {

    private SymTable symTable;
    private final List<String> inhetedTypes;

    public ComplexIdentifier(String name, String... types){
        this(name, new SymTable(), List.of(types));
    }
    
    public ComplexIdentifier(String name, SymTable symTable, String... types) {
        this(name, symTable, List.of(types));
    }

    public ComplexIdentifier(String name, SymTable symTable, List<String> inhetedTypes) {
        super(name);
        this.symTable = symTable;
        this.inhetedTypes = inhetedTypes;
    }

    public boolean hasParent() {
        return this.inhetedTypes.size() > 1;
    }

    public boolean isInstance(String parentType) {
        return hasParent() && inhetedTypes.contains(parentType);
    }

    public boolean containsAttribute(String attribute) {
        return symTable.containsIdentifier(attribute);
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

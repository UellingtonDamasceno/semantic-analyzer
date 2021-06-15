package semantic.analyzer.model;

import java.util.HashMap;
import java.util.Map;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Identifier;
import semantic.analyzer.model.Identifiers.SimpleIdentifier;
import semantic.analyzer.model.exceptions.SymbolAlreadyDeclaredException;
import semantic.analyzer.model.exceptions.UndeclaredSymbolException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SymTable {

    private Map<String, Identifier> table;
    private SymTable prev;

    public SymTable() {
        this(null);
    }

    public SymTable(SymTable parent) {
        this.table = new HashMap();
        this.prev = parent;
    }

    public void insert(Identifier id) throws SymbolAlreadyDeclaredException {
        String identifier = id.getName();
        try {
            Identifier found = this.find(identifier, this);
            if (found.equals(id)) {
                throw new SymbolAlreadyDeclaredException();
            }
        } catch (UndeclaredSymbolException ex) {
            this.table.put(identifier, id);
        }
    }

    public boolean containsIdentifier(String identifier) {
        try {
            find(identifier);
            return true;
        } catch (UndeclaredSymbolException ex) {
            return false;
        }
    }

    public Identifier find(String identifier) throws UndeclaredSymbolException {
        return find(identifier, this);
    }

    private Identifier find(String identifier, SymTable current) throws UndeclaredSymbolException {
        if (current == null) {
            throw new UndeclaredSymbolException();
        }
        return current.table.getOrDefault(identifier, find(identifier, current.prev));
    }
}

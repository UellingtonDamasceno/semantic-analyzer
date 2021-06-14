package semantic.analyzer.model;

import java.util.HashMap;
import java.util.Map;
import semantic.analyzer.model.exceptions.SymbolAlreadyDeclaredException;
import semantic.analyzer.model.exceptions.UndeclaredSymbolException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SymTable {

    private Map<String, Symbol> table;
    private SymTable prev;

    public SymTable() {
        this(null);
    }

    public SymTable(SymTable parent) {
        this.table = new HashMap();
        this.prev = parent;
    }

    public void insert(String identifier, Symbol symbol) throws SymbolAlreadyDeclaredException {
        try {
            Symbol found = this.find(identifier, this);
            if (found.equals(symbol)) {
                throw new SymbolAlreadyDeclaredException();
            }
        } catch (UndeclaredSymbolException ex) {
            this.table.put(identifier, symbol);
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

    public Symbol find(String identifier) throws UndeclaredSymbolException {
        return find(identifier, this);
    }

    private Symbol find(String identifier, SymTable current) throws UndeclaredSymbolException {
        if (current == null) {
            throw new UndeclaredSymbolException();
        }
        return current.table.getOrDefault(identifier, find(identifier, current.prev));
    }
}

package semantic.analyzer.model;

import java.util.HashMap;
import java.util.Map;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.Identifiers.Identifier;
import semantic.analyzer.model.exceptions.SymbolAlreadyDeclaredException;
import semantic.analyzer.model.exceptions.UndeclaredSymbolException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SymTable {

    private Map<Integer, Identifier> table;
    private SymTable prev;

    public SymTable() {
        this(null);
    }

    public SymTable(SymTable parent) {
        this.table = new HashMap();
        this.prev = parent;
    }

    public void insert(Identifier id, Token token) throws SymbolAlreadyDeclaredException {
        int identifier = id.hashCode();
        try {
            Identifier found = this.find(identifier);
            if (found.equals(id)) {
                throw new SymbolAlreadyDeclaredException(id, token);
            }
        } catch (UndeclaredSymbolException ex) {
            this.table.put(identifier, id);
        }
    }

    public Identifier find(Integer identifier) throws UndeclaredSymbolException {
        return find(identifier, this);
    }

    private Identifier find(Integer identifier, SymTable current) throws UndeclaredSymbolException {
        if (current == null) {
            throw new UndeclaredSymbolException();
        }
        Identifier id = current.table.get(identifier);
        return id != null ? id : find(identifier, current.prev);
    }
}

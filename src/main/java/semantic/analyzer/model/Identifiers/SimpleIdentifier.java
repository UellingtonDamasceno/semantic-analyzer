package semantic.analyzer.model.Identifiers;

/**
 * 
 * @author Antonio Neto e Uellington Damasceno
 */
public class SimpleIdentifier extends Identifier {

    private boolean variable;
    private boolean inicialized;
    private boolean global;
    private String type;

    public SimpleIdentifier(String type, String name) {
        super(name);
        this.type = type;
    }

    public boolean isInicialized() {
        return inicialized;
    }

    public void inicialize() {
        this.inicialized = true;
    }

    public boolean isGlobal() {
        return global;
    }

    public boolean thisTypeIs(String type) {
        return this.type.equals(type);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SimpleIdentifier && ((SimpleIdentifier) obj).hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.variable ? 1 : 0);
        hash = 47 * hash + (this.global ? 1 : 0);
        hash = 47 * hash + this.type.hashCode();
        hash = 47 * hash + this.name.hashCode();
        return hash;
    }

}

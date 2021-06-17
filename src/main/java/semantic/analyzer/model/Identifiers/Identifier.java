package semantic.analyzer.model.Identifiers;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public abstract class Identifier {

    protected String name;
    protected boolean constant;
    protected String type;
    
    public Identifier(String name, boolean constant, String type) {
        this.name = name;
        this.constant = constant;
        this.type = type;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isConstant() {
        return constant;
    }
    
    public String getType(){
        return this.type;
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}

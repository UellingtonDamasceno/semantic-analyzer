package semantic.analyzer.model.Identifiers;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public abstract class Identifier {

    protected String name;
    protected boolean constant;

    public Identifier(String name, boolean constant) {
        this.name = name;
        this.constant = constant;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isConstant() {
        return constant;
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}

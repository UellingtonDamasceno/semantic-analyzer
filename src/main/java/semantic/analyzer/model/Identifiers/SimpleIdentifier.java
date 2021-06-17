package semantic.analyzer.model.Identifiers;

import lexical.analyzer.model.Token;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SimpleIdentifier extends Identifier {

    private boolean inicialized;
    private String type;

    public SimpleIdentifier(Token type, Token name, boolean isVariable) {
        this(type.getLexame().getLexame(), name.getLexame().getLexame(), isVariable);
    }

    public SimpleIdentifier(String type, String name, boolean isVariable) {
        super(name, isVariable);
        this.type = type;
    }

    public boolean isInicialized() {
        return inicialized;
    }

    public void inicialize() {
        this.inicialized = true;
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
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{")
                .append("type:")
                .append(type)
                .append(",")
                .append("name:")
                .append(name)
                .append("}")
                .toString();
    }

}

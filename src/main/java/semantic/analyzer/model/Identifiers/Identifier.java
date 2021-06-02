package semantic.analyzer.model.Identifiers;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public abstract class Identifier {

    protected String name;
    
    public Identifier(String name){
        this.name = name;
    }
    
    public final String getName(){
        return this.name;
    }
    
    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}

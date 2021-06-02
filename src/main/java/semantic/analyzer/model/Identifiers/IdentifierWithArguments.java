package semantic.analyzer.model.Identifiers;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public abstract class IdentifierWithArguments extends Identifier {

    private List<Map.Entry<String, String>> args;

    public IdentifierWithArguments(String name, List<Map.Entry<String, String>> args) {
        super(name);
        this.args = args;
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.name.hashCode();
        hash = 97 * hash + args.size();
        return 97 * hash + Stream.iterate(0, i -> i + 1)
                .limit(this.args.size())
                .mapToInt(i -> this.args.get(i).getKey().hashCode() * i)
                .sum();
    }

    @Override
    public abstract boolean equals(Object obj);

}

package semantic.analyzer.model.Identifiers;

import semantic.analyzer.model.arguments.ArgumentsSignature;
import semantic.analyzer.model.arguments.ArgumentsState;
import semantic.analyzer.model.arguments.Arguments;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.exceptions.IncompatibleArgumentSizeException;
import semantic.analyzer.model.exceptions.IncompatibleTypesException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public abstract class IdentifierWithArguments extends Identifier {

    protected ArgumentsState state;

    public IdentifierWithArguments(List<Entry<Token, Token>> args, Token name, String type) {
        this(args.stream().map((entry) -> {
            String key = entry.getKey().getLexame().getLexame();
            String value = entry.getValue().getLexame().getLexame();
            return Map.entry(key, value);
        }).collect(toList()), name.getLexame().getLexame(), type);
    }

    public IdentifierWithArguments(List<Entry<String, String>> args, String name, String type) {
        super(name, false, type);
        this.state = new Arguments(args);
    }

    public IdentifierWithArguments(String name, List<String> args, String type) {
        super(name, false, type);
        this.state = new ArgumentsSignature(args);
    }

    public void transition(List<Entry<String, String>> args) {
        this.state = this.state.changeState(args);
    }

    public boolean validateArguments(List<String> arguments) throws IncompatibleArgumentSizeException, IncompatibleTypesException {
        return state.validateArguments(arguments);
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.name.hashCode();
        hash = 97 * hash + state.size();
        return 97 * hash + Stream.iterate(0, i -> i + 1)
                .limit(this.state.size())
                .mapToInt(i -> this.state.get(i).hashCode() * i)
                .sum();
    }

    @Override
    public String toString() {
        return "IdentifierWithArguments{name= " + name + " arguments= " + state + '}';
    }

    @Override
    public abstract boolean equals(Object obj);

}

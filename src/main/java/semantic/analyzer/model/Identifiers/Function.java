package semantic.analyzer.model.Identifiers;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.stream.Collectors.toList;
import lexical.analyzer.model.Token;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Function extends IdentifierWithArguments {

    public Function(Token identifier, Token typeReturn, List<Entry<Token, Token>> args) {
        this(identifier.getLexame().getLexame(),
                typeReturn.getLexame().getLexame(),
                args.stream().map((entry) -> {
                    String key = entry.getKey().getLexame().getLexame();
                    String value = entry.getValue().getLexame().getLexame();
                    return Map.entry(key, value);
                }).collect(toList()));
    }

    public Function(String identifier, String typeReturn, List<Entry<String, String>> args) {
        super(args, identifier, typeReturn);
    }
    
    public boolean returnTypeIs(String type) {
        return this.type.equals(type);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IdentifierWithArguments && ((IdentifierWithArguments) obj).hashCode() == this.hashCode();
    }
}

package semantic.analyzer.model.arguments;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Arguments implements ArgumentsState<String> {

    private final List<Entry<String, String>> arguments;

    public Arguments(List<Map.Entry<String, String>> arguments) {
        this.arguments = arguments;
    }

    @Override
    public int size() {
        return arguments.size();
    }

    @Override
    public String get(int index) {
        return arguments.get(index).getKey();
    }

    @Override
    public boolean isValid() {
        return arguments.stream()
                .map(Entry::getValue)
                .collect(toSet())
                .size() == arguments.size();
    }
//TODO gerar erro informativo
    @Override
    public ArgumentsState<String> changeState(List<Entry<String, String>> args) {
        return this;
    }

}

package semantic.analyzer.model.arguments;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import lexical.analyzer.model.Token;
import semantic.analyzer.model.exceptions.IncompatibleArgumentSizeException;
import semantic.analyzer.model.exceptions.IncompatibleTypesException;

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
    public boolean hasRepeatedArguments() {
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

    @Override
    public String toString() {
        return arguments.stream().map((entry) -> entry.getKey() + " " + entry.getValue()).collect(Collectors.joining());
    }

    @Override
    public boolean validateArguments(List<String> args) throws IncompatibleArgumentSizeException, IncompatibleTypesException {
        if (args.size() != arguments.size()) {
            throw new IncompatibleArgumentSizeException("Tamanhos incompatíveis");
        }
        if (!isValidParams(args)) {
            throw new IncompatibleTypesException();
        }
        return isValidParams(args);
    }

    private boolean isValidParams(List<String> args) {
        return isValidParams(args.iterator(), arguments.iterator());
    }

    private boolean isValidParams(Iterator<String> it1, Iterator<Entry<String, String>> it2) {
        if (it2.hasNext()) {
            String strNext = it2.next().getKey();
            return it1.next().equals(strNext) ? isValidParams(it1, it2) : false;
        }
        return true;
    }

}

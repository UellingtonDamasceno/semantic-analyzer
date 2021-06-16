package semantic.analyzer.model.arguments;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import semantic.analyzer.model.exceptions.IncompatibleArgumentSizeException;
import semantic.analyzer.model.exceptions.IncompatibleTypesException;
import syntax.analyzer.util.ErrorManager;

/**
 *
 * @author acmne
 */
public class ArgumentsSignature implements ArgumentsState<String> {

    private final List<String> arguments;

    public ArgumentsSignature(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public int size() {
        return arguments.size();
    }

    @Override
    public String get(int index) {
        return arguments.get(index);
    }

    @Override
    public boolean hasRepeatedArguments() {
        return false;
    }

    //TODO factory
    @Override
    public ArgumentsState<String> changeState(List<Map.Entry<String, String>> args) {
        if (args.size() != arguments.size()) {
            ErrorManager.addNewSemanticalError(new IncompatibleArgumentSizeException("Tamanhos incompatíveis"));
        }
        if (!isValidParams(args)) {
            ErrorManager.addNewSemanticalError(new IncompatibleTypesException("Tipos incompatíveis"));
        }
        return new Arguments(args);
    }

    private boolean isValidParams(List<Entry<String, String>> args) {
        return isValidParams(args.iterator(), arguments.iterator());
    }

    private boolean isValidParams(Iterator<Entry<String, String>> it1, Iterator<String> it2) {
        if (it1.hasNext()) {
            String next = it1.next().getKey();
            String nextMain = it2.next();
            return next.equals(nextMain) ? isValidParams(it1, it2) : false;
        }
        return true;
    }
}

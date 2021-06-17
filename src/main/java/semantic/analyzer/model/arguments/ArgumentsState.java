package semantic.analyzer.model.arguments;

import java.util.List;
import java.util.Map.Entry;
import semantic.analyzer.model.exceptions.IncompatibleArgumentSizeException;
import semantic.analyzer.model.exceptions.IncompatibleTypesException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public interface ArgumentsState<T> {

    public int size();

    public T get(int index);

    public boolean hasRepeatedArguments();

    public ArgumentsState<T> changeState(List<Entry<T, T>> args);

    public boolean validateArguments(List<String> arguments) throws IncompatibleArgumentSizeException, IncompatibleTypesException;
}

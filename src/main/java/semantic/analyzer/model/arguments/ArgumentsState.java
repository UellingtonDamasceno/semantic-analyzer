package semantic.analyzer.model.arguments;

import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public interface ArgumentsState<T> {
    public int size();
    public T get(int index);
    public boolean isValid();
    public ArgumentsState<T> changeState(List<Entry<T, T>> args);
}

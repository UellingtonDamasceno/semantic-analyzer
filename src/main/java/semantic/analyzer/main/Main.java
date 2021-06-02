package semantic.analyzer.main;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import semantic.analyzer.model.Identifiers.Function;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Main {

    public static void main(String[] args) {
        List<Entry<String, String>> args1 = List.of(Map.entry("real", "value0"), Map.entry("real", "value2"));
        List<Entry<String, String>> args2 = List.of(Map.entry("real", "value"), Map.entry("real", "value2"));
        
        Function f1 = new Function("f1", "int", args1);
        Function f2 = new Function("f1", "boolean", args2);
        System.out.println(f1.equals(f2));
    }
}

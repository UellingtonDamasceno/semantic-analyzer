package semantic.analyzer.util;

import java.util.List;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ErrorManager {
    
    private static List<Exception> errors;
    
    
    public static void addNewError(Exception e){
        errors.add(e);
    }
}

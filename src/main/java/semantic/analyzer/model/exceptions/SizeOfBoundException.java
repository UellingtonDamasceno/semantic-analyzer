package semantic.analyzer.model.exceptions;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SizeOfBoundException extends Exception{

    public SizeOfBoundException(int MIN, int MAX) {
        super("Erro semântico:");
    }
    
}

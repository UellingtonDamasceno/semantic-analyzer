package syntax.analyzer.model;

import java.util.List;
import static java.util.stream.Collectors.joining;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Lexame;
import lexical.analyzer.model.Token;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SyntaticalError extends Token {

    private final List<String> expectedTokens;

    public SyntaticalError(TokenType type, Lexame lexame, List<String> expectedTokens) {
        super(type, lexame);
        this.expectedTokens = expectedTokens;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("ERRO SINTÁTICO, NA LINHA: ")
                .append(lexame.getLine())
                .append(" FOI ENCONTRADO UM: \'")
                .append(lexame.getLexame())
                .append("\' MAS ERA ESPERADO: ")
                .append(expectedTokens
                        .stream()
                        .collect(joining("\' ou \'", "\'", "\'")))
                .toString();
    }
}

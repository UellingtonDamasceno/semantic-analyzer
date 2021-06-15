package syntax.analyzer.model;

import java.util.ArrayDeque;
import static java.util.stream.Collectors.toCollection;
import static lexical.analyzer.enums.TokenType.BLOCK_COMMENT;
import static lexical.analyzer.enums.TokenType.LINE_COMMENT;
import lexical.analyzer.model.SourceCode;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.grammar.Program;
import syntax.analyzer.util.ErrorManager;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SyntaxAnalyzer {

    public static SourceCode analyze(SourceCode code) {
        try {
            ArrayDeque<Token> tokens = code.getTokens()
                    .stream()
                    .filter(token -> !token.isError())
                    .filter(token -> token.getType() != BLOCK_COMMENT && token.getType() != LINE_COMMENT)
                    .collect(toCollection(ArrayDeque::new));

            Program.fullChecker(tokens);
        } catch (EOFNotExpectedException ex) {
            ErrorManager.setEOF(ex);
        }
        return code;
    }
}

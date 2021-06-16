package syntax.analyzer.main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;
import lexical.analyzer.model.LexicalAnalyzer;
import lexical.analyzer.model.SourceCode;
import lexical.analyzer.model.Token;
import lexical.analyzer.util.FilesUtil;
import syntax.analyzer.model.SyntaxAnalyzer;
import syntax.analyzer.util.ErrorManager;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Main {

    public static void main(String[] args) {
        boolean showLexicalToken = Arrays.asList(args).contains("-l");
        boolean showUnexpectedToken = Arrays.asList(args).contains("-c");

        FilesUtil.createIfNotExists("./output");
        Pattern pattern = Pattern.compile(FilesUtil.regexInputFileFilter);
        try {
            FilesUtil.readAllFiles(Path.of("./input"), pattern)
                    .entrySet()
                    .stream()
                    .map(SourceCode::new)
                    .map(LexicalAnalyzer::new)
                    .map(LexicalAnalyzer::analyze)
                    .map(SyntaxAnalyzer::analyze)
                    .forEach(code -> {
                        Path path = Path.of(code.getPath()
                                .toString()
                                .replaceAll("input", "output")
                                .replaceAll("entrada", "saida"));
                        code.setPath(path);
                        List<String> lines = new LinkedList();
                        if (showLexicalToken) {
                            lines = code.getTokens()
                                    .stream()
                                    .map(Token::toString)
                                    .collect(toList());
                        }
                        lines.addAll(ErrorManager.getErrors(showUnexpectedToken, false));
                        FilesUtil.write(path, lines);
                        ErrorManager.clear();
                    });
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

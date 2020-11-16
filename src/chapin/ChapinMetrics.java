package chapin;

import chapin.enums.GroupType;
import lexer.Lexeme;
import lexer.Lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChapinMetrics {
    ChapinParser parser;
    Map<GroupType, List<String>> chapinTypes;

    public ChapinMetrics(String rawText) {
        List<Lexeme> lexemes = new Lexer(rawText).getLexemes();
        parser = new ChapinParser(lexemes);
        countChapinTypes();
    }

    public Map<GroupType, List<String>> getChapinTypes() {
        return chapinTypes;
    }

    void countChapinTypes() {
        parser.parse();
        List<ChapinVariable> chapinVariables = new ArrayList<>(parser.chapinVariables);
        chapinVariables.forEach(System.out::println);
        chapinTypes = new HashMap<>();
        chapinVariables.forEach(v -> {
            GroupType t = MetricsUtility.defineGroupType(v);
            if (!chapinTypes.containsKey(t)) {
                chapinTypes.put(t, new ArrayList<>());
            }
            chapinTypes.get(t).add(v.identifier);
        });
    }
}

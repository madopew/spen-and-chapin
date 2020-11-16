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
    Map<GroupType, List<String>> ioChapinTypes;

    public ChapinMetrics(String rawText) {
        List<Lexeme> lexemes = new Lexer(rawText).getLexemes();
        parser = new ChapinParser(lexemes);
        countChapinTypes();
    }

    public Map<GroupType, List<String>> getChapinTypes() {
        return chapinTypes;
    }

    public Map<GroupType, List<String>> getIOChapinTypes() {
        return ioChapinTypes;
    }

    void countChapinTypes() {
        parser.parse();
        List<ChapinVariable> chapinVariables = new ArrayList<>(parser.chapinVariables);
        //System.out.println(chapinVariables);
        chapinVariables.forEach(System.out::println);
        chapinTypes = new HashMap<>();
        ioChapinTypes = new HashMap<>();
        chapinVariables.forEach(v -> {
            GroupType t = MetricsUtility.defineGroupType(v);
            if (!chapinTypes.containsKey(t)) {
                chapinTypes.put(t, new ArrayList<>());
                ioChapinTypes.put(t, new ArrayList<>());
            }
            chapinTypes.get(t).add(v.identifier);
            if(v.isIO) {
                ioChapinTypes.get(t).add(v.identifier);
            }
        });
    }
}

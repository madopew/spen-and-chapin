package chapin;

import lexer.Lexer;

import java.util.Map;

public class ChapinMetrics {
    Lexer l;
    Map<String, Boolean> initList;
    public ChapinMetrics(String rawText) {
        l = new Lexer(rawText);
    }
}

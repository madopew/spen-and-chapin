package spen;

import lexer.Lexer;
import lexer.Token;
import lexer.enums.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpenMetrics {
    Lexer l;
    Map<String, Integer> spens;
    public SpenMetrics(String rawText) {
        l = new Lexer(rawText);
        calculateSpens();
    }

    void calculateSpens() {
        List<Token> tokens = l.getTokens();
        spens = new HashMap<>(tokens.size() / 3);
        for(int i = 0; i < tokens.size() - 1; i++) {
            addSpen(tokens.get(i), tokens.get(i + 1));
        }
    }

    void addSpen(Token t, Token next) {
        if(next.value.equals("("))
            return;
        if(t.type != Type.VAR)
            return;
        if(!spens.containsKey(t.value)) {
            spens.put(t.value, 0);
        } else {
            int oldValue = spens.get(t.value);
            spens.put(t.value, oldValue + 1);
        }
    }

    public Map<String, Integer> getSpens() {
        return spens;
    }
}

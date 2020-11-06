package chapin;

import lexer.Lexeme;
import lexer.Lexer;
import lexer.enums.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Metrics {
    List<Lexeme> lexemes;
    List<ChapinType> chapinTypes;
    List<ChapinVariable> chapinVariables;
    int currentIndex;

    public Metrics(String rawText) {
        lexemes = new Lexer(rawText).getLexemes();
        chapinVariables = new ArrayList<>();
        currentIndex = 0;
        countChapinTypes();
    }

    public List<ChapinType> getChapinTypes() {
        return chapinTypes;
    }

    void countChapinTypes() {
        while (currentIndex < lexemes.size()) {
            if (isInitialization(currentIndex)) {
                currentIndex++;
                handleInitialization();
                continue;
            }
            currentIndex++;

        }
    }

    void handleInitialization() {
        ChapinVariable currentVariable = new ChapinVariable(lexemes.get(currentIndex).value);
        chapinVariables.add(currentVariable);
        currentIndex++;
        while (lexemes.get(currentIndex).type != Type.DELIM) {
            if (isAssignment(currentIndex)) {
                chapinVariables.get(chapinVariables.size() - 1).initialized = true;
                currentIndex++;
                //TODO: хз что
                handleExpresion(chapinVariables.size() - 1);
                break;
            }
            currentIndex++;
        }
    }

    void handleExpresion(int varIndex) {
        while (lexemes.get(currentIndex).type != Type.DELIM) {
            if(isVariable(currentIndex)) {
                int currentVarIndex = indexOfVariable(lexemes.get(currentIndex).value);
                chapinVariables.get(currentVarIndex).isUnused = false;
                chapinVariables.get(varIndex).isModified = true;
            } else if (isAssignment(currentIndex)) {
                currentIndex++;
            } else if (isFunction(currentIndex)) {

            }
            currentIndex++;
        }
    }

    boolean isFunction(int index) {
        return lexemes.get(index).type == Type.VAR && lexemes.get(index + 1).value.equals("(");
    }

    boolean isVariable(int index) {
        return lexemes.get(index).type == Type.VAR && !lexemes.get(index + 1).value.equals("(");
    }

    boolean isAssignment(int index) {
        return " = += -= *= /= %= &= |= >>= <<= ^= ".contains(lexemes.get(index).value);
    }

    int indexOfVariable(String name) {
        for (int i = 0; i < chapinVariables.size(); i++) {
            if (chapinVariables.get(i).identifier.equals(name))
                return i;
        }
        return -1;
    }

    boolean isInitialization(int index) {
        return lexemes.get(currentIndex).value.equals("var") || lexemes.get(currentIndex).value.equals("val");
    }
}

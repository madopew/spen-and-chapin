package chapin;

import lexer.Lexeme;
import lexer.Lexer;
import lexer.enums.Type;

import java.util.ArrayList;
import java.util.List;

public class ChapinMetrics {
    List<Lexeme> lexemes;
    List<ChapinType> chapinTypes;
    List<ChapinVariable> chapinVariables;
    int currentIndex;

    public ChapinMetrics(String rawText) {
        lexemes = new Lexer(rawText).getLexemes();
        chapinVariables = new ArrayList<>();
        currentIndex = 0;
        countChapinTypes();
    }

    public List<ChapinType> getChapinTypes() {
        return chapinTypes;
    }

    public List<ChapinVariable> getChapinVariables() { return chapinVariables; }

    void countChapinTypes() {
        handleProgram();

    }

    private void handleProgram() {
        while (currentIndex < lexemes.size()) {
            String lexemeValue = lexemes.get(currentIndex).value;
            if (MetricsUtility.isTokenDefine(lexemeValue)) {
                currentIndex++;
                handleInitialization();
            } else if (MetricsUtility.isTokenCondition(lexemeValue)) {
                currentIndex += 2;
                handleCondition();
            } else if (MetricsUtility.isTokenAssignment(lexemeValue)) {
                ChapinVariable previous = findVariable(lexemes.get(currentIndex - 1).value);
                if (previous.isInitialized) {
                    currentIndex++;
                    boolean property = handleExpression();
                    previous.isIO = property;
                    previous.isModified = !property;
                } else {
                    currentIndex++;
                    previous.isInitialized = true;
                    previous.isIO = handleExpression();
                }
            } else if(MetricsUtility.isTokenOutput(lexemeValue)) {
                currentIndex += 2;
                handleOutput();
            } else if(isTokenFunction()) {
                currentIndex += 2;
                handleFunction();
            } else {
                currentIndex++;
            }
        }
    }

    private ChapinVariable findVariable(String token) {
        for (ChapinVariable v : chapinVariables) {
            if (v.identifier.equals(token))
                return v;
        }
        return null;
    }

    private boolean isTokenFunction() {
        return lexemes.get(currentIndex).type == Type.VAR && lexemes.get(currentIndex + 1).value.equals("(");
    }


    private void handleInitialization() {
        while (currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if (current.type == Type.VAR) {
                chapinVariables.add(new ChapinVariable(current.value));
                currentIndex++;
            } else if (MetricsUtility.isTokenAssignment(current.value)) {
                ChapinVariable tmpVar = chapinVariables.get(chapinVariables.size() - 1);
                tmpVar.isInitialized = true;
                currentIndex++;
                tmpVar.isIO = handleExpression();
                return;
            } else if (current.type == Type.DELIM) {
                currentIndex++;
                return;
            } else {
                currentIndex++;
            }
        }
    }

    private void handleCondition() {
        int bracketsAmount = 1;
        while(currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if(isTokenFunction()) {
                currentIndex += 2;
                handleFunction();
            } else if(current.type == Type.VAR) {
                ChapinVariable v = findVariable(current.value);
                v.isUnused = false;
                v.isInCondition = true;
                currentIndex++;
            } else if(current.value.equals("(")) {
                bracketsAmount++;
                currentIndex++;
            } else if(current.value.equals(")")) {
                bracketsAmount--;
                currentIndex++;
                if(bracketsAmount == 0)
                    return;
            } else {
                currentIndex++;
            }
        }
    }

    private void handleOutput() {
        int bracketsAmount = 1;
        while(currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if(isTokenFunction()) {
                currentIndex += 2;
                handleFunction();
            } else if(current.type == Type.VAR) {
                ChapinVariable v = findVariable(current.value);
                v.isUnused = false;
                v.isIO = true;
                currentIndex++;
            } else if(current.value.equals("(")) {
                bracketsAmount++;
                currentIndex++;
            } else if(current.value.equals(")")) {
                bracketsAmount--;
                currentIndex++;
                if(bracketsAmount == 0)
                    return;
            } else {
                currentIndex++;
            }
        }
    }

    private boolean handleExpression() {
        boolean toReturn  = false;
        while (currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if (isTokenFunction()) {
                currentIndex += 2;
                toReturn = handleFunction();
            } else if (current.type == Type.VAR) {
                findVariable(current.value).isUnused = false;
                currentIndex++;
            } else if (MetricsUtility.isTokenInput(current.value)) {
                toReturn = true;
                currentIndex += 3;
            } else if (MetricsUtility.isTokenAssignment(current.value)) {
                ChapinVariable previous = findVariable(lexemes.get(currentIndex - 1).value);
                if(previous.isInitialized) {
                    currentIndex++;
                    boolean property = handleExpression();
                    previous.isIO = property;
                    previous.isModified = !property;
                } else {
                    previous.isInitialized = true;
                    previous.isIO = handleExpression();
                }
                return false;
            } else if (current.type == Type.DELIM) {
                currentIndex++;
                return toReturn;
            } else {
                currentIndex++;
            }
        }
        return false;
    }

    private boolean handleFunction() {
        int bracketsAmount = 1;
        boolean toReturn = false;
        while(currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if(isTokenFunction()) {
                currentIndex += 2;
                handleFunction();
            } else if(current.type == Type.VAR) {
                findVariable(current.value).isUnused = false;
                currentIndex++;
            } else if(MetricsUtility.isTokenInput(current.value)) {
                toReturn = true;
                currentIndex += 3;
            } else if(current.value.equals("(")) {
                bracketsAmount++;
                currentIndex++;
            } else if(current.value.equals(")")) {
                bracketsAmount--;
                currentIndex++;
                if(bracketsAmount == 0)
                    return toReturn;
            } else {
                currentIndex++;
            }
        }
        return false;
    }
}

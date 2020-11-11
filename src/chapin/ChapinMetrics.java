package chapin;

import chapin.enums.GroupType;
import chapin.exceptions.UndefinedStateExpression;
import lexer.Lexeme;
import lexer.Lexer;
import lexer.enums.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChapinMetrics {
    List<Lexeme> lexemes;
    Map<GroupType, List<String>> chapinTypes;
    List<ChapinVariable> chapinVariables;

    int currentIndex;
    ParserState state;

    public ChapinMetrics(String rawText) {
        lexemes = new Lexer(rawText).getLexemes();
        chapinVariables = new ArrayList<>();
        currentIndex = 0;
        countChapinTypes();
    }

    public Map<GroupType, List<String>> getChapinTypes() {
        return chapinTypes;
    }

    void countChapinTypes() {
        //currentIndex = 0;
        //handleProgram();
        parse();
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

    private Token getToken(int index) {
        Lexeme current = lexemes.get(index);
        String lexemeValue = lexemes.get(index).value;
        if (MetricsUtility.isUnary(lexemeValue))
            return Token.UNARY;
        if (MetricsUtility.isTokenDefine(lexemeValue))
            return Token.DEFINE;
        if (MetricsUtility.isTokenCondition(lexemeValue))
            return Token.CONDITION;
        if (MetricsUtility.isTokenAssignment(lexemeValue))
            return Token.ASSIGNMENT;
        if (MetricsUtility.isTokenInput(lexemeValue))
            return Token.INPUT;
        if (MetricsUtility.isTokenOutput(lexemeValue))
            return Token.OUTPUT;
        if (isTokenFunction(index))
            return Token.FUNCTION;
        if (current.type == Type.VAR)
            return Token.VARIABLE;
        if (current.type == Type.DELIM)
            return Token.DELIMITER;
        if (lexemeValue.equals("("))
            return Token.OPEN_BRACKET;
        if (lexemeValue.equals(")"))
            return Token.CLOSE_BRACKET;
        return Token.OTHER;
    }

    private boolean isTokenFunction(int index) {
        return lexemes.get(index).type == Type.VAR && lexemes.get(index + 1).value.equals("(");
    }

    private void parse() {
        ChapinVariable lastVariable = new ChapinVariable();
        ChapinVariable savedVariable = new ChapinVariable();
        boolean savedProperty = true;
        int index = 0;
        int bracketAmount = 0;
        state = ParserState.NORMAL;
        while (index < lexemes.size()) {
            Lexeme current = lexemes.get(index);
            switch (getToken(index)) {
                //TODO handle function decalration
                case UNARY:
                    //TODO handle unary
                    break;
                case DEFINE:
                    if(state == ParserState.NORMAL) {
                        state = ParserState.IN_INITIALIZATION;
                        index++;
                    } else {
                        throw new UndefinedStateExpression(current.value, state.name());
                    }
                    break;
                case CONDITION:
                    if(state == ParserState.NORMAL) {
                        state = ParserState.IN_CONDITION;
                        index++;
                    } else {
                        throw new UndefinedStateExpression(current.value, state.name());
                    }
                    break;
                case ASSIGNMENT:
                    switch (state) {
                        case IN_INITIALIZATION:
                        case IN_EXPRESSION:
                        case NORMAL:
                            savedVariable = lastVariable;
                            savedProperty = false;
                            state = ParserState.IN_EXPRESSION;
                            index++;
                            break;
                        default:
                            throw new UndefinedStateExpression(current.value, state.name());
                    }
                    break;
                case INPUT:
                    switch (state) {
                        case IN_FUNCTION:
                        case IN_EXPRESSION:
                            savedProperty = true;
                            index += 3;
                            break;
                        default:
                            throw new UndefinedStateExpression(current.value, state.name());
                    }
                    break;
                case OUTPUT:
                    if (state == ParserState.NORMAL) {
                        state = ParserState.IN_OUTPUT;
                        index++;
                    } else {
                        throw new UndefinedStateExpression(current.value, state.name());
                    }
                    break;
                case FUNCTION:
                    switch (state) {
                        case IN_FUNCTION:
                        case IN_EXPRESSION:
                        case NORMAL:
                            index++;
                            state = ParserState.IN_FUNCTION;
                            break;
                        case IN_OUTPUT:
                        case IN_CONDITION:
                            index++;
                            break;
                        case IN_INITIALIZATION:
                            throw new UndefinedStateExpression(current.value, state.name());
                    }
                    break;
                case VARIABLE:
                    switch (state) {
                        case NORMAL:
                            lastVariable = findVariable(current.value);
                            break;
                        case IN_INITIALIZATION:
                            lastVariable = new ChapinVariable(current.value);
                            chapinVariables.add(lastVariable);
                            break;
                        case IN_CONDITION:
                            //TODO variable in for declaration...
                            lastVariable = findVariable(current.value);
                            lastVariable.isUnused = false;
                            lastVariable.isInCondition = true;
                            break;
                        case IN_OUTPUT:
                            lastVariable = findVariable(current.value);
                            lastVariable.isUnused = false;
                            lastVariable.isIO = true;
                            break;
                        case IN_FUNCTION:
                        case IN_EXPRESSION:
                            lastVariable = findVariable(current.value);
                            lastVariable.isUnused = false;
                            break;
                    }
                    index++;
                    break;
                case DELIMITER:
                    switch (state) {
                        case IN_EXPRESSION:
                            state = ParserState.NORMAL;
                            savedVariable.isIO |= savedProperty;
                            if(savedVariable.isInitialized)
                                savedVariable.isModified |= !savedProperty;
                            else
                                savedVariable.isInitialized = true;
                            break;
                        case IN_INITIALIZATION:
                            state = ParserState.NORMAL;
                            break;
                        case IN_FUNCTION:
                        case IN_OUTPUT:
                        case IN_CONDITION:
                        case NORMAL:
                            break;
                    }
                    index++;
                    break;
                case OPEN_BRACKET:
                    switch (state) {
                        case IN_EXPRESSION:
                        case NORMAL:
                        case IN_INITIALIZATION:
                            break;
                        case IN_CONDITION:
                        case IN_OUTPUT:
                        case IN_FUNCTION:
                            bracketAmount++;
                            break;
                    }
                    index++;
                    break;
                case CLOSE_BRACKET:
                    switch (state) {
                        case IN_EXPRESSION:
                        case NORMAL:
                        case IN_INITIALIZATION:
                            break;
                        case IN_OUTPUT:
                        case IN_CONDITION:
                            bracketAmount--;
                            if(bracketAmount == 0)
                                state = ParserState.NORMAL;
                            break;
                        case IN_FUNCTION:
                            bracketAmount--;
                            if(bracketAmount == 0) {
                                state = ParserState.NORMAL;
                                savedVariable.isIO |= savedProperty;
                                if(savedVariable.isInitialized)
                                    savedVariable.isModified |= !savedProperty;
                                else
                                    savedVariable.isInitialized = true;
                            }
                            break;
                    }
                    index++;
                    break;
                case OTHER:
                    index++;
                    break;
            }
        }
    }

    private void handleProgram() {
        while (currentIndex < lexemes.size()) {
            String lexemeValue = lexemes.get(currentIndex).value;
            if (MetricsUtility.isUnary(lexemeValue)) {
                handleUnary();
            } else if (MetricsUtility.isTokenDefine(lexemeValue)) {
                currentIndex++;
                handleInitialization();
            } else if (MetricsUtility.isTokenCondition(lexemeValue)) {
                currentIndex += 2;
                handleCondition();
            } else if (MetricsUtility.isTokenAssignment(lexemeValue)) {
                ChapinVariable previous = findVariable(lexemes.get(currentIndex - 1).value);
                currentIndex++;
                boolean property = handleExpression();
                previous.isIO |= property;
                if (previous.isInitialized) {
                    previous.isModified |= !property;
                } else {
                    previous.isInitialized = true;
                }
            } else if (MetricsUtility.isTokenOutput(lexemeValue)) {
                currentIndex += 2;
                handleOutput();
            } else if (isCurrentTokenFunction()) {
                currentIndex += 2;
                handleFunction();
            } else {
                currentIndex++;
            }
        }
    }

    private void handleUnary() {
        if (lexemes.get(currentIndex - 1).type == Type.VAR) {
            findVariable(lexemes.get(currentIndex - 1).value).isModified = true;
            currentIndex++;
        } else {
            findVariable(lexemes.get(currentIndex + 1).value).isModified = true;
            currentIndex += 2;
        }
    }

    private ChapinVariable findVariable(String token) {
        for (ChapinVariable v : chapinVariables) {
            if (v.identifier.equals(token))
                return v;
        }
        return null;
    }

    private boolean isCurrentTokenFunction() {
        return lexemes.get(currentIndex).type == Type.VAR && lexemes.get(currentIndex + 1).value.equals("(");
    }

    private void handleInitialization() {
        while (currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if (MetricsUtility.isUnary(current.value)) {
                handleUnary();
            } else if (current.type == Type.VAR) {
                chapinVariables.add(new ChapinVariable(current.value));
                currentIndex++;
            } else if (MetricsUtility.isTokenAssignment(current.value)) {
                ChapinVariable previous = chapinVariables.get(chapinVariables.size() - 1);
                currentIndex++;
                boolean property = handleExpression();
                previous.isIO |= property;
                if (previous.isInitialized) {
                    previous.isModified |= !property;
                } else {
                    previous.isInitialized = true;
                }
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
        while (currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if (MetricsUtility.isUnary(current.value)) {
                handleUnary();
            } else if (isCurrentTokenFunction()) {
                currentIndex += 2;
                bracketsAmount++;
            } else if (current.type == Type.VAR) {
                ChapinVariable v = findVariable(current.value);
                v.isUnused = false;
                v.isInCondition = true;
                currentIndex++;
            } else if (current.value.equals("(")) {
                bracketsAmount++;
                currentIndex++;
            } else if (current.value.equals(")")) {
                bracketsAmount--;
                currentIndex++;
                if (bracketsAmount == 0)
                    return;
            } else {
                currentIndex++;
            }
        }
    }

    private void handleOutput() {
        int bracketsAmount = 1;
        while (currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if (MetricsUtility.isUnary(current.value)) {
                handleUnary();
            } else if (isCurrentTokenFunction()) {
                currentIndex += 2;
                bracketsAmount++;
            } else if (current.type == Type.VAR) {
                ChapinVariable v = findVariable(current.value);
                v.isUnused = false;
                v.isIO = true;
                currentIndex++;
            } else if (current.value.equals("(")) {
                bracketsAmount++;
                currentIndex++;
            } else if (current.value.equals(")")) {
                bracketsAmount--;
                currentIndex++;
                if (bracketsAmount == 0)
                    return;
            } else {
                currentIndex++;
            }
        }
    }

    private boolean handleExpression() {
        boolean toReturn = false;
        while (currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if (MetricsUtility.isUnary(current.value)) {
                handleUnary();
            } else if (MetricsUtility.isTokenInput(current.value)) {
                toReturn = true;
                currentIndex += 3;
            } else if (isCurrentTokenFunction()) {
                currentIndex += 2;
                toReturn = handleFunction();
            } else if (current.type == Type.VAR) {
                findVariable(current.value).isUnused = false;
                currentIndex++;
            } else if (MetricsUtility.isTokenAssignment(current.value)) {
                ChapinVariable previous = findVariable(lexemes.get(currentIndex - 1).value);
                currentIndex++;
                boolean property = handleExpression();
                previous.isIO |= property;
                if (previous.isInitialized) {
                    previous.isModified |= !property;
                } else {
                    previous.isInitialized = true;
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
        while (currentIndex < lexemes.size()) {
            Lexeme current = lexemes.get(currentIndex);
            if (MetricsUtility.isUnary(current.value)) {
                handleUnary();
            } else if (MetricsUtility.isTokenInput(current.value)) {
                toReturn = true;
                currentIndex += 3;
            } else if (isCurrentTokenFunction()) {
                currentIndex += 2;
                handleFunction();
            } else if (current.type == Type.VAR) {
                findVariable(current.value).isUnused = false;
                currentIndex++;
            } else if (current.value.equals("(")) {
                bracketsAmount++;
                currentIndex++;
            } else if (current.value.equals(")")) {
                bracketsAmount--;
                currentIndex++;
                if (bracketsAmount == 0)
                    return toReturn;
            } else {
                currentIndex++;
            }
        }
        return false;
    }
}

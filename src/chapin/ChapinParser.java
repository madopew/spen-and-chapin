package chapin;

import chapin.exceptions.UndefinedStateExpression;
import chapin.exceptions.UndefinedVariableExpression;
import lexer.Lexeme;
import lexer.enums.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class ChapinParser {
    List<ChapinVariable> chapinVariables;
    private final List<Lexeme> lexemes;

    public ChapinParser(List<Lexeme> lexemes) {
        this.lexemes = new ArrayList<>(lexemes);
        chapinVariables = new ArrayList<>();
    }

    enum ParserState {
        NORMAL,
        IN_INITIALIZATION,
        IN_CONDITION,
        IN_EXPRESSION,
        IN_OUTPUT,
        IN_FUNCTION,
        IN_FUNC_DEFINITION
    }

    private static class Token {
        static final short
                UNARY = 0,
                DEFINE = 1,
                CONDITION = 2,
                ASSIGNMENT = 3,
                INPUT = 4,
                OUTPUT = 5,
                FUNCTION = 6,
                VARIABLE = 7,
                DELIMITER = 8,
                OPEN_BRACKET = 9,
                CLOSE_BRACKET = 10,
                OTHER = 11,
                FUN = 12;
    }

    @FunctionalInterface
    private interface tokenHandler {
        void handle(Lexeme current);
    }
    private final tokenHandler[] jumpTable = {
            this::handleUnary,
            this::handleDefine,
            this::handleCondition,
            this::handleAssignment,
            this::handleInput,
            this::handleOutput,
            this::handleFunction,
            this::handleVariable,
            this::handleDelimiter,
            this::handleOpenBracket,
            this::handleCloseBracket,
            this::handleOther,
            this::handleFun
    };

    private ChapinVariable lastVariable;
    private ChapinVariable savedVariable;
    private boolean savedProperty;
    private int index;
    private int bracketAmount;
    private ParserState state;
    private Stack<ParserState> funcStateStack;

    private void initParser() {
        lastVariable = new ChapinVariable();
        savedVariable = new ChapinVariable();
        savedProperty = true;
        index = 0;
        bracketAmount = 0;
        state = ParserState.NORMAL;
        funcStateStack = new Stack<>();
    }

    void parse() {
        initParser();
        while (index < lexemes.size()) {
            Lexeme current = lexemes.get(index);
            jumpTable[getToken(index)].handle(current);
        }
    }

    private void handleUnary(Lexeme current) {
        if (lexemes.get(index - 1).type == Type.VAR) {
            lastVariable.isModified = true;
        } else if (lexemes.get(index + 1).type == Type.VAR){
            findVariable(lexemes.get(index + 1).value).isModified = true;
        } else {
            throw new UndefinedStateExpression(current.value, state.name());
        }
        index++;
    }

    private void handleDefine(Lexeme current) {
        if(state == ParserState.NORMAL) {
            state = ParserState.IN_INITIALIZATION;
            index++;
        } else {
            throw new UndefinedStateExpression(current.value, state.name());
        }
    }

    private void handleCondition(Lexeme current) {
        if(state == ParserState.NORMAL) {
            state = ParserState.IN_CONDITION;
            index++;
        } else {
            throw new UndefinedStateExpression(current.value, state.name());
        }
    }

    private void handleAssignment(Lexeme current) {
        switch (state) {
            case IN_EXPRESSION:
                savedVariable.isIO |= savedProperty;
                if(savedVariable.isInitialized)
                    savedVariable.isModified = true;
                else
                    savedVariable.isInitialized = true;
            case IN_INITIALIZATION:
            case NORMAL:
                savedVariable = lastVariable;
                savedProperty = false;
                state = ParserState.IN_EXPRESSION;
                break;
            default:
                throw new UndefinedStateExpression(current.value, state.name());
        }
        index++;
    }

    private void handleInput(Lexeme current) {
        switch (state) {
            case IN_FUNCTION:
            case IN_EXPRESSION:
                savedProperty = true;
                index += 3;
                break;
            default:
                throw new UndefinedStateExpression(current.value, state.name());
        }
    }

    private void handleOutput(Lexeme current) {
        if (state == ParserState.NORMAL) {
            state = ParserState.IN_OUTPUT;
            index++;
        } else {
            throw new UndefinedStateExpression(current.value, state.name());
        }
    }

    private void handleFunction(Lexeme current) {
        switch (state) {
            case IN_EXPRESSION:
            case NORMAL:
                funcStateStack.push(state);
                index++;
                state = ParserState.IN_FUNCTION;
                break;
            case IN_FUNCTION:
            case IN_OUTPUT:
            case IN_CONDITION:
            case IN_FUNC_DEFINITION:
                index++;
                break;
            case IN_INITIALIZATION:
                throw new UndefinedStateExpression(current.value, state.name());
        }
    }

    private void handleVariable(Lexeme current) {
        switch (state) {
            case NORMAL:
                lastVariable = findVariable(current.value);
                break;
            case IN_FUNC_DEFINITION:
                lastVariable = new ChapinVariable(current.value);
                lastVariable.isInitialized = true;
                chapinVariables.add(lastVariable);
                break;
            case IN_INITIALIZATION:
                lastVariable = new ChapinVariable(current.value);
                chapinVariables.add(lastVariable);
                break;
            case IN_CONDITION:
                lastVariable = findVariable(current.value);

                // conditional variable initialization
                /*try {
                    lastVariable = findVariable(current.value);
                } catch (UndefinedVariableExpression e) {
                    System.err.printf("Undefined token '%s' met in condition. New variable assigned.\n", current.value);
                    lastVariable = new ChapinVariable(current.value);
                    chapinVariables.add(lastVariable);
                }*/

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
    }

    private void handleDelimiter(Lexeme current) {
        switch (state) {
            case IN_EXPRESSION:
                state = ParserState.NORMAL;
                savedVariable.isIO |= savedProperty;
                if(savedVariable.isInitialized)
                    savedVariable.isModified = true;
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
    }

    private void handleOpenBracket(Lexeme current) {
        switch (state) {
            case IN_FUNC_DEFINITION:
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
    }

    private void handleCloseBracket(Lexeme current) {
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
                    state = funcStateStack.pop();
                    savedVariable.isIO |= savedProperty;
                }
                break;
            case IN_FUNC_DEFINITION:
                state = ParserState.NORMAL;
                break;
        }
        index++;
    }

    private void handleOther(Lexeme current) {
        index++;
    }

    private void handleFun(Lexeme current) {
        if (state == ParserState.NORMAL) {
            state = ParserState.IN_FUNC_DEFINITION;
        } else {
            throw new UndefinedStateExpression(current.value, state.name());
        }
        index++;
    }

    private int getToken(int index) {
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
        if (lexemeValue.equals("fun"))
            return Token.FUN;
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

    private ChapinVariable findVariable(String token) {
        for (ChapinVariable v : chapinVariables) {
            if (v.identifier.equals(token))
                return v;
        }
        throw new UndefinedVariableExpression(token);
    }
}

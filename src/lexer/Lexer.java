package lexer;

import lexer.enums.Type;

import java.util.ArrayList;

public class Lexer {
    private final ArrayList<Lexeme> lexemes;
    private final char[] inputStream;
    private int currentIndex = 0;
    public Lexer(String unParsedText) {
        lexemes = new ArrayList<>();
        this.inputStream = unParsedText.toCharArray();
        parse();
    }

    public ArrayList<Lexeme> getLexemes() {
        return lexemes;
    }

    private void parse() {
        while(currentIndex < inputStream.length) {
            readNext();
            currentIndex++;
        }
    }

    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        for (Lexeme lexeme : lexemes) toReturn.append(lexeme.toString()).append("\n");
        return toReturn.toString();
    }

    private void readNext() {
        char ch = inputStream[currentIndex];
        if(ch == ' ' || ch == '\t') {
            return;
        }
        if (ch == '\n' || ch == '\r' || ch == ';') {
            Lexeme t = new Lexeme(Type.DELIM, ch + "");
            lexemes.add(t);
            return;
        }
        if(ch == '/' && (inputStream[currentIndex+1] == '/' || inputStream[currentIndex+1] == '*')) {
            skipComment();
            return;
        }
        if(ch == '"') {
            readString();
            return;
        }
        if(ch == '\'') {
            readChar();
            return;
        }
        if(Character.isDigit(ch)) {
            readNumber();
            return;
        }
        if(Character.isLetter(ch) || ch == '_') {
            readIdentificator();
            return;
        }
        if(",(){}[]".indexOf(ch) >= 0) {
            Lexeme t = new Lexeme(Type.PUNC, ch + "");
            lexemes.add(t);
            return;
        }
        if(isOp(ch)) {
            readOp();
            return;
        }
        System.err.printf("Cannot read character '%c' [%d]\n",ch,currentIndex);
    }

    private void skipComment() {
        if(inputStream[currentIndex+1] == '/') {
            while(inputStream[currentIndex] != '\n')
                currentIndex++;
        } else if(inputStream[currentIndex+1] == '*') {
            currentIndex += 3;
            while(inputStream[currentIndex-1] != '*' || inputStream[currentIndex] != '/') {
                currentIndex++;
            }
        }
    }

    private void readString() {
        if(inputStream[currentIndex] == '"' &&
                inputStream[currentIndex + 1] == '"' &&
                inputStream[currentIndex + 2] == '"') {
            readMultilineString();
        } else {
            StringBuilder lexeme = new StringBuilder("\"");
            currentIndex++;
            while(inputStream[currentIndex] != '"') {
                if(inputStream[currentIndex] == '\\')
                    lexeme.append(inputStream[currentIndex++]);
                lexeme.append(inputStream[currentIndex++]);
            }
            lexeme.append("\"");
            lexemes.add(new Lexeme(Type.STR, lexeme.toString()));
        }
    }

    private void readMultilineString() {
        StringBuilder lexeme = new StringBuilder("\"");
        currentIndex += 3;
        while(!(inputStream[currentIndex] == '"' &&
                inputStream[currentIndex + 1] == '"' &&
                inputStream[currentIndex + 2] == '"')) {
            if(inputStream[currentIndex] == '\n' ||
                    inputStream[currentIndex] == '\r' ||
                    inputStream[currentIndex] == '\t') {
                currentIndex++;
                continue;
            }
            lexeme.append(inputStream[currentIndex++]);
        }
        lexeme.append("\"");
        currentIndex += 2;
        lexemes.add(new Lexeme(Type.STR, lexeme.toString()));
    }

    private void readChar() {
        StringBuilder lexeme = new StringBuilder("'");
        currentIndex++;
        while(inputStream[currentIndex] != '\'') {
            if(inputStream[currentIndex] == '\\')
                lexeme.append(inputStream[currentIndex++]);
            lexeme.append(inputStream[currentIndex++]);
        }
        lexeme.append("'");
        lexemes.add(new Lexeme(Type.CHAR, lexeme.toString()));
    }

    private void readNumber() {
        StringBuilder lexeme = new StringBuilder();
        while(isDigit(inputStream[currentIndex]) ||
                (inputStream[currentIndex] == '.' && isDigit(inputStream[currentIndex + 1]))) {
            if(inputStream[currentIndex] == 'e')
                lexeme.append(inputStream[currentIndex++]);
            lexeme.append(inputStream[currentIndex++]);
        }
        currentIndex--;
        lexemes.add(new Lexeme(Type.NUM, lexeme.toString()));
    }

    private boolean isDigit(char ch) {
        return "0123456789ABCDEFbefLx_".indexOf(ch) >= 0;
    }

    private void readIdentificator() {
        StringBuilder lexeme = new StringBuilder();
        while(Character.isDigit(inputStream[currentIndex]) ||
                Character.isLetter(inputStream[currentIndex]) ||
                inputStream[currentIndex] == '_') {
            lexeme.append(inputStream[currentIndex++]);
        }
        currentIndex--;
        Type type = isHardKeyword(lexeme.toString()) ? Type.HKW : isSoftKeyword(lexeme.toString()) ? Type.SKW : Type.VAR;
        lexemes.add(new Lexeme(type, lexeme.toString()));
    }

    private boolean isHardKeyword(String text) {
        String keywords = " as break class continue do else false for fun if in interface is null object package return super this throw true try typealias typeof val var when while ";
        return keywords.contains(" " + text + " ");
    }

    private boolean isSoftKeyword(String text) {
        String keywords = " Int Double Boolean String Char Float Long until downTo by catch constructor delegate dynamic field file finally get import init param property receiver set setparam where actual abstract annotation companion const crossinline data enum expect external final inflix inline inner internal lateinit noinline open operator out override private protected public reified sealed suspend tailrec vararg field it ";
        return keywords.contains(" " + text + " ");
    }

    private boolean isOp(char ch) {
        return "+-/*=%&|<>!@:?.".indexOf(ch) >= 0;
    }

    private boolean isCombOp(String op, char next) {
        return " += -= *= /= %= ++ -- && || == != === !== >= <= !! ?. ?: :: .. -> ".contains(op + next);
    }

    private void readOp() {
        StringBuilder lexem = new StringBuilder();
        while(isOp(inputStream[currentIndex]) && isCombOp(lexem.toString(), inputStream[currentIndex])) {
            lexem.append(inputStream[currentIndex++]);
        }
        currentIndex--;
        lexemes.add(new Lexeme(Type.OP, lexem.toString()));
    }
}
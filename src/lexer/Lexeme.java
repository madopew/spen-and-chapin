package lexer;

import lexer.enums.Type;

public class Lexeme {
    public Type type;
    public String value;
    public Lexeme(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return "{type: " + type + ", value: " + value + "}";
    }
}

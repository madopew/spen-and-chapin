package chapin.exceptions;

public class UndefinedVariableExpression extends RuntimeException {
    public UndefinedVariableExpression(String token) {
        super(String.format("Undefined token %s", token));
    }
}

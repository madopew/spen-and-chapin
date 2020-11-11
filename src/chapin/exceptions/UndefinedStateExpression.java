package chapin.exceptions;

public class UndefinedStateExpression extends RuntimeException {
    public UndefinedStateExpression(String token, String state) {
        super(String.format("Unexpected token '%s' in state '%s'", token, state));
    }
}

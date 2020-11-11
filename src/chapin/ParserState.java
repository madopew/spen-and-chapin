package chapin;

enum ParserState {
    NORMAL,
    IN_INITIALIZATION,
    IN_CONDITION,
    IN_EXPRESSION,
    IN_OUTPUT,
    IN_FUNCTION
}

package chapin;

class ChapinVariable extends Variable {
    boolean isIO;
    boolean isModified;
    boolean isInCondition;
    boolean isUnused;

    public ChapinVariable() {

    }

    public ChapinVariable(String name) {
        super(name);
        isIO = false;
        isModified = false;
        isInCondition = false;
        isUnused = true;
    }

    @Override
    public String toString() {
        return String.format("{id:'%s', init:'%s', io:'%s', mod:'%s', cond:'%s', unus:'%s'}",
                identifier, isInitialized, isIO, isModified, isInCondition, isUnused);
    }
}

package chapin;

class ChapinVariable extends Variable{
    boolean isIO;
    boolean isModified;
    boolean isInCondition;
    boolean isUnused;

    public ChapinVariable(String name) {
        super(name);
        isIO = false;
        isModified = false;
        isInCondition = false;
        isUnused = true;
    }
}

package chapin;

class VariableProperties {
    String variableName;
    boolean isIO;
    boolean isModified;
    boolean isInCondition;
    boolean isUnused;

    public VariableProperties(String name) {
        variableName = name;
        isIO = false;
        isModified = false;
        isInCondition = false;
        isUnused = true;
    }
}

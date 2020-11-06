package chapin;

class Variable {
    String identifier;
    boolean initialized;

    public Variable(String name) {
        this.identifier = name;
        initialized = false;
    }
}

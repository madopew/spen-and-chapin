package chapin;

import chapin.enums.GroupType;

import java.util.ArrayList;
import java.util.List;

public class ChapinType {
    GroupType type;
    List<String> variables;
    int amount;

    public ChapinType(GroupType type, List<String> variables) {
        this.type = type;
        this.variables = new ArrayList<>(variables);
        this.amount = variables.size();
    }

    public GroupType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public List<String> getVariables() {
        return variables;
    }

}

package chapin;

import chapin.enums.GroupType;

class ChapinMetricsUtility {
    static GroupType defineGroupType(VariableProperties vp) {
        if(vp.isUnused)
            return GroupType.T;
        if(vp.isInCondition)
            return GroupType.C;
        if(vp.isModified)
            return GroupType.M;
        return GroupType.P;
    }
}

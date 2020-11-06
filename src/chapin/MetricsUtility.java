package chapin;

import chapin.enums.GroupType;

class MetricsUtility {
    static GroupType defineGroupType(ChapinVariable vp) {
        if(vp.isUnused)
            return GroupType.T;
        if(vp.isInCondition)
            return GroupType.C;
        if(vp.isModified)
            return GroupType.M;
        return GroupType.P;
    }
}

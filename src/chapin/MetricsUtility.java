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

    static boolean isTokenDefine(String token) {
        return " var val ".contains(" " + token + " ");
    }

    static boolean isTokenCondition(String token) {
        return " if while for when ".contains(" " + token + " ");
    }

    static boolean isTokenAssignment(String token) {
        return " = += -= /= *= %= >>= <<= &= |= ".contains(" " + token + " ");
    }

    static boolean isTokenOutput(String token) {
        return " print println ".contains(" " + token + " ");
    }

    static boolean isTokenInput(String token) {
        return " read readln readLine ".contains(" " + token + " ");
    }

    static boolean isTokenType(String token) {
        return " Int Integer Double Boolean String Char Float Long ".contains(" " + token + " ");
    }

    static boolean isUnary(String token) {
        return " ++ -- ".contains(" " + token + " ");
    }
}

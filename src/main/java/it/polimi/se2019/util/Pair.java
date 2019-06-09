package it.polimi.se2019.util;

public class Pair<First, Second> {
    First mFirst;
    Second mSecond;

    public Pair(First first, Second second) {
        mFirst = first;
        mSecond = second;
    }

    public First getFirst() {
        return mFirst;
    }

    public Second getSecond() {
        return mSecond;
    }
}

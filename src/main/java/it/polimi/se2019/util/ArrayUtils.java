package it.polimi.se2019.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ArrayUtils {
    private ArrayUtils() {

    }

    public static <T> T[] ofAll(T ele, Supplier<T[]> arrayFactory) {
        T[] result = arrayFactory.get();
        Arrays.fill(result, ele);

        return result;
    }

    public static boolean[] ofAll(boolean ele, int size) {
        boolean[] result = new boolean[size];
        Arrays.fill(result, ele);

        return result;
    }

    public static boolean[] from(List<Boolean> list) {
        boolean[] result = new boolean[list.size()];

        IntStream.range(0, list.size())
                .forEach(i -> result[i] = list.get(i));

        return result;
    }
}

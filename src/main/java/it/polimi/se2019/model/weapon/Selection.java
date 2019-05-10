package it.polimi.se2019.model.weapon;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.weapon.serialization.CustomSelectionAdapter;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class representing a selection of something
 */
@JsonAdapter(CustomSelectionAdapter.class)
public class Selection<T> {
    Predicate<T> mCharacteristicFunction;

    /**
     * constructs a selection from a set representing its contents
     * @param contents elements contained in the selection
     * @return constructed selection
     */
    public static<T> Selection<T> fromSet(Set<T> contents) {
        Selection<T> result = new Selection();
        result.mCharacteristicFunction = (element) -> contents.contains(element);

        return result;
    }

    // TODO: refine doc
    /**
     * returns a set containing all elements considered inside this selection
     * @domain all elements that exist, without this, the function would have to check infinite elements
     * @return selection as a set
     */
    public Set<T> toSet(Set<T> domain) {
        return domain.stream()
                .filter(mCharacteristicFunction)
                .collect(Collectors.toSet());
    }

    /**
     * constructs a selection from a set representing its contents
     * @param  element only element contained in the selection
     * @return constructed selection
     */
    public static<T> Selection<T> fromSingle(T element) {
        return fromSet(Collections.singleton(element));
    }

    // TODO: add doc
    public Stream<T> stream(Set<T> domain) {
        return domain.stream()
                .filter(mCharacteristicFunction);
    }

    // TODO: add doc
    // TODO: reimplement using cloning
    public Selection<T> negate() {
        Selection<T> result = new Selection();
        result.mCharacteristicFunction = mCharacteristicFunction.negate();

        return result;
    }

    /**
     *
     * @param element specified element
     * @return true if {@code element} is contained inside this selection
     */
    public boolean contains(T element) {
        return mCharacteristicFunction.test(element);
    }
}

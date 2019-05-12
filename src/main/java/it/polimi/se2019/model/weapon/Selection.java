package it.polimi.se2019.model.weapon;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.weapon.serialization.CustomSelectionAdapter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class representing a selection of something
 */
@JsonAdapter(CustomSelectionAdapter.class)
public class Selection<T> {
    Optional<Stream<T>> mDomain = Optional.empty();
    Predicate<T> mCharacteristicFunction;

    // trivial setters
    public void setDomain(Stream<T> domain) {
        mDomain = Optional.ofNullable(domain);
    }

    /**
     * constructs a selection from a set representing its contents
     * @param contents elements contained in the selection
     * @return constructed selection
     */
    public static<T> Selection<T> fromSet(Set<T> contents) {
        Selection<T> result = new Selection();
        result.mDomain = Optional.of(contents.stream());
        result.mCharacteristicFunction = contents::contains;

        return result;
    }

    /**
     * constructs a selection from a set representing its contents
     * @return constructed selection
     */
    public static<T> Selection<T> of(T... contents) {
        Selection<T> result = new Selection();
        result.mDomain = Optional.of(Arrays.stream(contents));
        result.mCharacteristicFunction = element -> Arrays.stream(contents)
                .anyMatch(arrayEle -> arrayEle.equals(element));

        return result;
    }

    // TODO: refine doc
    /**
     * returns a set containing all elements considered inside this selection
     * @domain all elements that exist, without this, the function would have to check infinite elements
     * @return selection as a set
     */
    public Set<T> asSet() {
        if (!mDomain.isPresent())
            throw new UnsupportedOperationException("Cannot transform unbounded Selection to Set!");

        return mDomain.get()
                .filter(mCharacteristicFunction)
                .collect(Collectors.toSet());
    }

    /**
     * constructs a selection from a set representing its contents
     * @param  element only element contained in the selection
     * @return constructed selectionS
     */
    public static<T> Selection<T> fromSingle(T element) {
        return fromSet(Collections.singleton(element));
    }

    // TODO: add doc
    public Stream<T> stream(Set<T> domain) {
        if (!mDomain.isPresent())
            throw new UnsupportedOperationException("Cannot transform unbounded Selection to Stream!");

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

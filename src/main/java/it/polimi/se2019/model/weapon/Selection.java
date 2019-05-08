package it.polimi.se2019.model.weapon;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.weapon.serialization.CustomSelectionAdapter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Utility class representing a selection of something
 */
@JsonAdapter(CustomSelectionAdapter.class)
public class Selection<T> {
    private final Set<T> mContents = new HashSet();

    /**
     * constructs a selection from a set representing its contents
     * @param contents elements contained in the selection
     * @return constructed selection
     */
    public static<T> Selection<T> fromSet(Set<T> contents) {
        Selection<T> result = new Selection();
        result.mContents.addAll(contents);
        
        return result;
    }

    /**
     * returns a set containing all elements considered inside this selection
     * @return selection as a set
     */
    public Set<T> toSet() {
        return mContents;
    }

    /**
     * constructs a selection from a set representing its contents
     * @param  element only element contained in the selection
     * @return constructed selection
     */
    public static<T> Selection<T> fromSingle(T element) {
        Selection<T> result = new Selection();
        result.mContents.add(element);

        return result;
    }

    public Stream<T> stream() {
        return mContents.stream();
    }

    /**
     *
     * @param element specified element
     * @return true if {@code element} is contained inside this selection
     */
    public boolean contains(T element) {
        return mContents.contains(element);
    }
}

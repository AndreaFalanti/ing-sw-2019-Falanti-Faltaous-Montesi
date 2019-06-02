package it.polimi.se2019.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Objects;

/**
 * Utility wrapper class mostly used to quickly compare json strings properly in tests
 */
public class PrettyJsonElement {
    private JsonElement mContents;

    public PrettyJsonElement(String contentsStr) {
        mContents = new Gson().fromJson(contentsStr, JsonElement.class);
    }
    public PrettyJsonElement(JsonElement contents) {
        mContents = contents;
    }

    public JsonElement get() {
        return mContents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        PrettyJsonElement that = (PrettyJsonElement) o;

        return Objects.equals(mContents, that.mContents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mContents);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create()
                .toJson(mContents);
    }
}

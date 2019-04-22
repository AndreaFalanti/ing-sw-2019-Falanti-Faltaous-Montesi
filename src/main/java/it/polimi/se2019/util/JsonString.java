package it.polimi.se2019.util;

import com.google.gson.JsonDeserializer;

import java.util.Objects;

/**
 * Utility wrapper class mostly used to quickly compare json strings properly in tests
 */
public class JsonString {
    private String mContents;

    public JsonString(String contents) {
        mContents = contents;
    }

    public String get() {
        return mContents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mContents);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        JsonString jsonString = (JsonString) obj;

        return StringUtils.equalIgnoringWhitespace(mContents, jsonString.mContents);
    }

    @Override
    public String toString() {
        return get();
    }
}

package it.polimi.se2019.model.board;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Builder for initializing Board objects
 */
class Builder {
    public final String FIRST_HALF_FRONT_PATH = "resources/first_half_front.json";

    private Board mToBuild;

    public Builder() {
        mToBuild = new Board();
    }

    public Builder(Board board) {
        mToBuild = board;
    }

    public Builder fromJson(String jsonStr) {
        mToBuild = Board.fromJson(jsonStr);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(FIRST_HALF_FRONT_PATH, mToBuild);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        Builder casted = (Builder) other;

        return mToBuild.equals(casted.mToBuild);
    }

    public Builder deepCopy() {
        return new Builder(mToBuild.deepCopy());
    }

    @Override
    public String toString() {
        return mToBuild.toString();
    }

    public Builder combineRight(Board toCombineWith) throws BadSizeException {
        if (toCombineWith.getHeight() != mToBuild.getHeight())
            throw new BadSizeException("Trying to combineRight boards with different heights!");

        for (int i = 0; i < mToBuild.getRows().size(); i++) {
            mToBuild.getRows().get(i).addAll(toCombineWith.getRows().get(i));
        }

        mToBuild.mWidth += toCombineWith.mWidth;

        return this;
    }

    public Builder configuration1() {
        return this;
    }

    public Board build() {
        return mToBuild;
    }
}

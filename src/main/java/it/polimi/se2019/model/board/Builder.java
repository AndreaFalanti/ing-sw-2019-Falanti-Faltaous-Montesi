package it.polimi.se2019.model.board;

/**
 * Builder for initializing Board objects
 */
class Builder {
    public final String FIRST_HALF_FRONT_PATH = "resources/first_half_front.json";

    private Board mToBuild = new Board();

    public Builder fromJson(String jsonStr) {
        mToBuild = Board.fromJson(jsonStr);
        return this;
    }

    public Builder combine(Board board) {
        /*
         123  1234
        1--- 1----
         456  5678
        2--- 2----
         789  9ABC
        3--- 3----

        1234567
        -------
        89ABCDE
        -------
         */



    }


    public Builder configuration1() {
        mToBuild = fromJson(FIRST_HALF_FRONT_PATH)
                .combine(Board.fromJson(SECOND_HALF_FRONT_PATH));
        return this;
    }

    public Board build() {
        return mToBuild;
    }
}

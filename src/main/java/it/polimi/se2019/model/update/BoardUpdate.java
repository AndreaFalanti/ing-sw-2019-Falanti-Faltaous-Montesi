package it.polimi.se2019.model.update;

import it.polimi.se2019.model.board.Board;

public class BoardUpdate implements Update {
    private Board mBoard;

    public BoardUpdate(Board board) {
        mBoard = board;
    }

    public Board getBoard() {
        return mBoard;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}

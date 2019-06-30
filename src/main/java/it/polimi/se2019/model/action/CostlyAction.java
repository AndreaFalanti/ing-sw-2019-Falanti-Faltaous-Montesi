package it.polimi.se2019.model.action;

public interface CostlyAction extends Action {
    boolean[] getDiscardedCards ();
    void setDiscardedCards (boolean[] discardedCards);
}

package it.polimi.se2019.model.action;

/**
 * Common interface to all actions that also perform an ammo payment
 *
 * @author Andrea Falanti
 */
public interface CostlyAction extends Action {
    boolean[] getDiscardedCards ();
    void setDiscardedCards (boolean[] discardedCards);
}

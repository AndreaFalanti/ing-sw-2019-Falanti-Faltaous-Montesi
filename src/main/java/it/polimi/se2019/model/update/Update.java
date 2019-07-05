package it.polimi.se2019.model.update;

/**
 * Basic interface common to all update messages, used by model to notify view of game data changes
 *
 * @author Andrea Falanti
 */
public interface Update {
    void handleMe(UpdateHandler handler);
}

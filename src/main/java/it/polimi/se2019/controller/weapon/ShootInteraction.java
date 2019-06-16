package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.Request;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShootInteraction {
    private Thread mThread = null;
    private boolean mOccupied = false;
    private BlockingQueue<Request> mRequests = new LinkedBlockingQueue<>();
    private Logger mLogger = Logger.getLogger(getClass().getName());

    public boolean isOccupied() {
        return mOccupied;
    }

    public BlockingQueue<Request> getRequestQueue() {
        return mRequests;
    }

    public void putRequest(Request request) {
        if (!isOccupied())
            throw new InvalidStateException("No one is shooting!");

        mRequests.add(request);
    }

    public void exec(Game game, View view, PlayerColor shooter, Expression weaponBehaviour) {
        mThread = new Thread(() -> {
            ShootContext initialContext = new ShootContext(game, view, shooter, this);

            weaponBehaviour.eval(initialContext);
        });

        mThread.start();
    }

    public void reset() throws InterruptedException {
        // since the game needs to be reset, the current shoot interaction is not longer valid,
        // and so can be interrupted
        mThread.interrupt();
        mThread.join();

        // reset the game to the way it was before the begging of the shoot interaction
        throw new UnsupportedOperationException("WIP");
    }

    private void handleInterruption() throws InterruptedException {
        mLogger.log(Level.SEVERE, "Something inside a shoot interaction was interrupted!\n" +
                "Resetting shoot interaction and interrupting his thread...\n" +
                "WARNING: The game still does not reset itself to its original \"pre-shoot\" state!");
        reset();
    }
}

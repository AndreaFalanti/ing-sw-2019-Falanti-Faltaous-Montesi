package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.Request;
import sun.plugin.dom.exception.InvalidStateException;

import javax.xml.ws.WebServiceProvider;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShootInteraction {
    private Logger mLogger = Logger.getLogger(getClass().getName()) ;

    private boolean mOccupied = false;
    private Thread mThread = null;
    private final BlockingQueue<Request> mRequests = new LinkedBlockingQueue<>();
    private final Object mLock = new Object();

    public boolean isOccupied() {
        return mOccupied;
    }

    public BlockingQueue<Request> getRequestQueue() {
        return mRequests;
    }

    public Object getLock() {
        return mLock;
    }

    public void putRequest(Request request) {
        if (!isOccupied())
            throw new InvalidStateException("No one is shooting!");

        mLogger.log(Level.INFO, "Putting request in shoot interaction queue: {0}", request);
        mRequests.add(request);
    }

    public void exec(Game game, View view, PlayerColor shooter, Expression weaponBehaviour) {
        // announce that thread is occupied
        mOccupied = true;

        mThread = new Thread(() -> {
            // evaluate shoot expression
            ShootContext context = new ShootContext(game, view, shooter, this);
            try {
                weaponBehaviour.eval(context);
            } catch (UndoShootInteractionException e) {
                mLogger.log(Level.INFO,
                        "Undoing shoot interaction...");
                context.getUndoInfo().doUndo();
            } catch (Exception e) {
                mLogger.log(Level.WARNING,
                        "Shutting down shoot interaction because of exception in expression evaluation...");
                throw e;
            } finally {
                synchronized (mLock) {
                    mLock.notifyAll();
                }
            }

            // announce end of shoot interaction
            synchronized (mLock) {
                mLogger.info("Shutting down shoot interaction thread...");

                if (!mRequests.isEmpty())
                    mLogger.log(Level.SEVERE,
                            "Shoot interaction thread is shutting down, but there are still " +
                                    "requests to process in the request queue!\n" +
                                    "Unprocessed requests: {0}", mRequests
                    );
                mRequests.clear();

                mOccupied = false;
                mLock.notifyAll();
            }
        });

        mLogger.info("Starting shoot interaction thread...");
        mThread.start();
    }

    @Deprecated
    public void reset() throws InterruptedException {
        // since the game needs to be reset, the current shoot interaction is not longer valid,
        // and so can be interrupted
        mThread.interrupt();
        mThread.join();

        // reset the game to the way it was before the begging of the shoot interaction
        throw new UnsupportedOperationException("WIP");
    }

    @Deprecated
    private void handleInterruption() throws InterruptedException {
        mLogger.log(Level.SEVERE, "Something inside a shoot interaction was interrupted!\n" +
                "Resetting shoot interaction and interrupting his thread...\n" +
                "WARNING: The game still does not reset itself to its original \"pre-shoot\" state!");
        reset();
    }
}

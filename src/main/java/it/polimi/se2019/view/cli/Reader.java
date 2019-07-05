package it.polimi.se2019.view.cli;
import java.util.concurrent.*;

public class Reader {
    private final int tries;
    private final int timeout ;
    private final TimeUnit unit;

    public Reader(int tries, int timeout, TimeUnit unit) {
        this.tries = tries;
        this.timeout = timeout;
        this.unit = unit;
    }



    public String readLine() throws InterruptedException {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        String input = null;
        try {

            for (int i = 0; i < tries; i++) {

                Future<String> result = ex.submit(
                        new ConsoleReader());
                try {
                    input = result.get(timeout, unit);
                    break;
                } catch (ExecutionException e) {

                } catch (TimeoutException e) {

                    result.cancel(true);
                }
            }
        } finally {
            ex.shutdownNow();
        }
        return input;
    }
}
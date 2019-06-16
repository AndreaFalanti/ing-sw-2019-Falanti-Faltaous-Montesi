package it.polimi.se2019.controller.weapon;

public class EvaluationInterruptedException extends RuntimeException {
    public EvaluationInterruptedException(String interruptedProcessName) {
        super("Evaluation of shoot expression was interrupted while waiting for the " +
                interruptedProcessName + " to provide needed info");
    }
}

package org.nobugs.mcmc.diagnostics;

import java.io.Closeable;

public class Monitor implements Closeable {
    private int steps;
    private int rejected;

    public Monitor() {
        this.steps = 0;
        this.rejected = 0;
    }

    public void recordStep(StepOutcome outcome) {
        steps++;
        if (outcome == StepOutcome.REJECTED) {
            rejected++;
        }
    }

    @Override
    public void close() {
        System.out.print("Did " + steps + " and rejected " + rejected);
    }

    public enum StepOutcome {ACCEPTED, REJECTED}
}

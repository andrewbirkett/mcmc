package org.nobugs.mcmc.diagnostics;

import java.io.Closeable;
import java.io.IOException;

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
    public void close() throws IOException {
        System.out.printf("Did " + steps + " and rejected " + rejected);
    }

    public enum StepOutcome {ACCEPTED, REJECTED}
}

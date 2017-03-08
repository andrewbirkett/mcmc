package org.nobugs.mcmc.diagnostics;

public interface Tracer {
    void update(double[] params);
}

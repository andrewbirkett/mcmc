package org.nobugs.mcmc;

public interface Tracer {
    void update(double[] params, double logProbabilityDensity);
}

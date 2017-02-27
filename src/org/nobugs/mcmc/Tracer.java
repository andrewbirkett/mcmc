package org.nobugs.mcmc;

public interface Tracer {
    void update(double mu, double sigma, double probability);
}

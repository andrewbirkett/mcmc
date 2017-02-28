package org.nobugs.mcmc.distribution;

public interface Distribution {
    double logdensity(double[] parameters, double[] data);
}

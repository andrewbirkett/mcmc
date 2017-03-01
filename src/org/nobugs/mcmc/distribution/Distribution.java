package org.nobugs.mcmc.distribution;

import org.nobugs.mcmc.Datapoints;

public interface Distribution {
    double logdensity(double[] parameters, Datapoints data);
}

package org.nobugs.mcmc.distribution;

import org.nobugs.mcmc.Data;

public interface Distribution {
    double logdensity(double[] parameters, Data data);
}

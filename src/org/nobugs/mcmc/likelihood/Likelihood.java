package org.nobugs.mcmc.likelihood;

import org.nobugs.mcmc.Data;

public interface Likelihood {
    double logLikelihood(double[] parameters, Data data);
}

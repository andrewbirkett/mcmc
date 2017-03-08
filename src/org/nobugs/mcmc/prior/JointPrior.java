package org.nobugs.mcmc.prior;

public interface JointPrior {
    double logProbability(double[] parameters);
}

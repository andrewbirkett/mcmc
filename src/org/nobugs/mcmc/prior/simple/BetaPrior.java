package org.nobugs.mcmc.prior.simple;

import cern.jet.random.Beta;
import cern.jet.random.engine.RandomEngine;

public class BetaPrior implements SimplePrior {
    private final Beta distribution;

    public BetaPrior(double a, double b, RandomEngine randomEngine) {
        this.distribution = new Beta(a, b, randomEngine);
    }

    @Override
    public double logProbability(double value) {
        return Math.log(distribution.pdf(value));
    }
}

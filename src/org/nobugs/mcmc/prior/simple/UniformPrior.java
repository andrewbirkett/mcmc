package org.nobugs.mcmc.prior.simple;

import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

public class UniformPrior implements SimplePrior {
    private final Uniform distribution;

    public UniformPrior(double min, double max, RandomEngine randomEngine) {
        this.distribution = new Uniform(min, max, randomEngine);
    }

    @Override
    public double logProbability(double value) {
        return Math.log(distribution.pdf(value));
    }
}

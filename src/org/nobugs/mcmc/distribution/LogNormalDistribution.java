package org.nobugs.mcmc.distribution;

import org.nobugs.mcmc.Data;

public class LogNormalDistribution implements Distribution {
    @Override
    public double logdensity(double[] parameters, Data data) {
        double logMu = parameters[0];
        double logSigma = parameters[1];

        double logProbability = 0;

        logProbability += data.size() * -Math.log(logSigma * Math.sqrt(2 * Math.PI));
        double denom = 2 * square(logSigma);
        for (double d : data.getAll()) {
            logProbability -= Math.log(d) + square(Math.log(d) - logMu) / denom;
        }
        return logProbability;
    }

    private double square(double d) {
        return d * d;
    }
}

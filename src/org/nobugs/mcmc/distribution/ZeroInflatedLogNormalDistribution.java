package org.nobugs.mcmc.distribution;

import org.nobugs.mcmc.Datapoints;

public class ZeroInflatedLogNormalDistribution implements Distribution {
    @Override
    public double logdensity(double[] parameters, Datapoints data) {
        double p = parameters[0];
        double logMu = parameters[1];
        double logSigma = parameters[2];

        double common = Math.log(p)
                + Math.log(1)
                - Math.log(logSigma)
                - Math.log(Math.sqrt(2 * Math.PI));
        double twiceLogSigmaSquared = 2 * logSigma * logSigma;
        double zeroCase = Math.log(1.0 - p);

        double logProbability = 0;
        for (double d : data.getAll()) {
            if (d == 0) {
                logProbability += zeroCase;
            } else {
                double logD = Math.log(d);
                logProbability += common - logD - square(logD - logMu) / twiceLogSigmaSquared;
            }
        }
        return logProbability;
    }

    private double square(double d) {
        return d * d;
    }
}

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

        double logProbability = data.getZeros() * zeroCase;

        for (int i = 0; i < data.getNonZeroValues().size(); i++) {
            double d = data.getNonZeroValues().get(i);

                double logD = Math.log(d);
                logProbability += common - logD - square(logD - logMu) / twiceLogSigmaSquared;
        }
        return logProbability;
    }

    private double square(double d) {
        return d * d;
    }
}

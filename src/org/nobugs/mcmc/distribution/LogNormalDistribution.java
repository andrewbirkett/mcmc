package org.nobugs.mcmc.distribution;

public class LogNormalDistribution implements Distribution {
    @Override
    public double logdensity(double[] parameters, double[] data) {
        double logMu = parameters[0];
        double logSigma = parameters[1];

        double logProbability = 0;

        logProbability += data.length * -Math.log(logSigma * Math.sqrt(2 * Math.PI));
        double denom = 2 * square(logSigma);
        for (double d : data) {
            logProbability -= Math.log(d) + square(Math.log(d) - logMu) / denom;
        }
        return logProbability;
    }

    private double square(double d) {
        return d * d;
    }
}

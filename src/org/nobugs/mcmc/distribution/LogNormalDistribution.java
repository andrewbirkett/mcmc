package org.nobugs.mcmc.distribution;

public class LogNormalDistribution implements Distribution {
    @Override
    public double logdensity(double[] parameters, double[] data) {
        double logMu = parameters[0];
        double logSigma = parameters[1];

        double probability = 1;

        for (double d : data) {
            double a = 1 / (d * logSigma * Math.sqrt(2 * Math.PI));
            double b = Math.exp(-(Math.pow(Math.log(d) - logMu, 2.0) / (2 * logSigma * logSigma)));
            probability *= (a * b);
        }
        return Math.log(probability);
    }
}

package org.nobugs.mcmc.distribution;

public class ZeroInflatedLogNormalDistribution implements Distribution {
    @Override
    public double logdensity(double[] parameters, double[] data) {
        double p = parameters[0];
        double logMu = parameters[1];
        double logSigma = parameters[2];

        double probability = 1;

        for (double d : data) {
            double thisP;
            if (d == 0) {
                thisP = (1.0 - p);
            } else {
                double a = 1 / (d * logSigma * Math.sqrt(2 * Math.PI));
                double b = Math.exp(-(Math.pow(Math.log(d) - logMu, 2.0) / (2 * logSigma * logSigma)));
                thisP = p * a * b;
            }
            probability *= thisP;
        }
        return Math.log(probability);

    }

    private double square(double d) {
        return d * d;
    }
}

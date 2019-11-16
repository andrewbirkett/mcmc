package org.nobugs.mcmc.likelihood;

import org.nobugs.mcmc.Data;

public class LogNormalLikelihood implements Likelihood {
    @Override
    public double logLikelihood(double[] parameters, Data data) {
        double logMu = parameters[0];
        double logSigma = parameters[1];

        double logProbability = 0;

        logProbability += data.size() * -Math.log(logSigma * Math.sqrt(2 * Math.PI));
        double denom = 2 * square(logSigma);
        for (int i = 0; i < data.getNonZeroValues().size(); i++) {
            double d = data.getNonZeroValues().get(i);
            logProbability -= Math.log(d) + square(Math.log(d) - logMu) / denom;

        }
        return logProbability;
    }

    @Override
    public boolean supports(double[] parameters) {
        double logSigma = parameters[1];
        return logSigma >= 0;
    }

    private double square(double d) {
        return d * d;
    }
}

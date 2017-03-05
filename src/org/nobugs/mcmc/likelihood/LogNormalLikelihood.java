package org.nobugs.mcmc.likelihood;

import com.google.common.base.Preconditions;
import org.nobugs.mcmc.Data;

public class LogNormalLikelihood implements Likelihood {
    @Override
    public double logLikelihood(double[] parameters, Data data) {
        double logMu = parameters[0];
        double logSigma = parameters[1];

        Preconditions.checkArgument(logSigma >= 0.0);
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

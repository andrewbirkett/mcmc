package org.nobugs.mcmc.likelihood;

import com.google.common.base.Preconditions;
import org.nobugs.mcmc.Data;

public class NormalLikelihood implements Likelihood {
    @Override
    public double logLikelihood(double[] parameters, Data data) {
        double mu = parameters[0];
        double sigma = parameters[1];

        double variance = sigma * sigma;
        double sqrt_inv = 1.0 / Math.sqrt(2.0 * Math.PI * variance);

        double logProbability = 0;
        logProbability += data.size() * Math.log(sqrt_inv);
        for (int i = 0; i < data.getAll().size(); i++) {
            double d = data.getAll().get(i);
            double delta = d - mu;
            logProbability += -(delta * delta) / (2.0 * variance);
        }

        return logProbability;
    }

    @Override
    public boolean supports(double[] parameters) {
        Preconditions.checkArgument(parameters.length == 2);
        double sigma = parameters[1];
        return sigma >= 0.0;
    }
}

package org.nobugs.mcmc.likelihood;

import org.nobugs.mcmc.Data;

import static com.google.common.base.Preconditions.checkArgument;

public class NormalLikelihood implements Likelihood {
    @Override
    public double logLikelihood(double[] parameters, Data data) {
        double mu = parameters[0];
        double sigma = parameters[1];

        checkArgument(sigma >= 0.0);

        double variance = sigma * sigma;
        double sqrt_inv = 1.0 / Math.sqrt(2.0 * Math.PI * variance);

        double sum = 0;
        for (double d : data.getAll()) {
            double delta = d - mu;
            sum += -(delta * delta) / (2.0 * variance);
        }

        return data.size() * Math.log(sqrt_inv) + sum;
    }
}

package org.nobugs.mcmc.likelihood;

import org.nobugs.mcmc.Data;

import static com.google.common.base.Preconditions.checkArgument;

public class BernoulliLikelihood implements Likelihood {

    @Override
    public double logLikelihood(double[] parameters, Data data) {
        double theta = parameters[0];
        checkArgument(theta >= 0.0 && theta <= 1.0);

        double logProbability = 0;
        if (data.getZeros() > 0) {
            logProbability += Math.log(1 - theta) * data.getZeros();
        }
        if (data.getNonZeroValues().size() > 0) {
            logProbability += Math.log(theta) * data.getNonZeroValues().size();
        }
        return logProbability;
    }

    @Override
    public boolean supports(double[] parameters) {
        checkArgument(parameters.length == 1);
        double theta = parameters[0];
        return theta >= 0.0 && theta <= 1.0;
    }
}

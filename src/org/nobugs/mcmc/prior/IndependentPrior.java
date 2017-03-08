package org.nobugs.mcmc.prior;

import com.google.common.base.Preconditions;
import org.nobugs.mcmc.prior.simple.SimplePrior;

public class IndependentPrior implements JointPrior {

    private final SimplePrior[] priors;

    public IndependentPrior(SimplePrior... priors) {
        this.priors = priors;
    }

    @Override
    public double logProbability(double[] parameters) {
        Preconditions.checkArgument(parameters.length == priors.length);
        double logProbability = 0.0;
        for (int i = 0; i < priors.length; i++) {
            logProbability += priors[i].logProbability(parameters[i]);
        }

        return logProbability;
    }
}

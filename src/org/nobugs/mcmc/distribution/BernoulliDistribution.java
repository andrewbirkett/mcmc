package org.nobugs.mcmc.distribution;

import com.google.common.base.Preconditions;
import org.nobugs.mcmc.Data;

public class BernoulliDistribution implements Distribution {
    @Override
    public double logdensity(double[] parameters, Data data) {
        double theta = parameters[0];
        Preconditions.checkArgument( theta >= 0.0 && theta <= 1.0 );

        double logprobability = 0;
        if (data.getZeros() > 0 )  {
            logprobability += Math.log(1-theta) * data.getZeros();
        }
        if (data.getNonZeroValues().size() > 0) {
            logprobability += Math.log(theta) * data.getNonZeroValues().size();
        }
        return logprobability;
    }
}

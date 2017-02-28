package org.nobugs.mcmc.distribution;

public class NormalDistribution implements Distribution {
    @Override
    public double logdensity(double[] parameters, double[] data) {
        double mu = parameters[0];
        double sigma = parameters[1];
        double variance = sigma * sigma;
        double sqrt_inv = 1.0 / Math.sqrt(2.0 * Math.PI * variance);

        double sum = 0;
        for (double d : data) {
            double delta = d - mu;
            sum += -(delta * delta) / (2.0 * variance);
        }

        return data.length * Math.log(sqrt_inv) + sum;
    }
}

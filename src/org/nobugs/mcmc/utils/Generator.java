package org.nobugs.mcmc.utils;

import cern.jet.random.Binomial;
import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import org.nobugs.mcmc.Data;

public class Generator {

    public static Data bernoulli(double theta, int n, RandomEngine randomEngine) {
        Binomial binomial = new Binomial(n, theta, randomEngine);
        int ones = binomial.nextInt();
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = (i < ones) ? 1.0 : 0.0;
        }
        return new Data(data);
    }


    public static Data normal(double mean, double sd, int n, RandomEngine randomEngine) {
        Normal normal = new Normal(mean, sd, randomEngine);

        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = normal.nextDouble();
        }
        return new Data(data);
    }

    public static Data logNormal(double logMean, double logSd, int n, RandomEngine randomEngine) {
        Normal normal = new Normal(logMean, logSd, randomEngine);

        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = Math.exp(normal.nextDouble());
        }
        return new Data(data);
    }

    public static Data zeroInflatedLogNormal(double p, double logMean, double logSd, int n, RandomEngine randomEngine) {
        Normal normal = new Normal(logMean, logSd, randomEngine);
        Uniform uniform = new Uniform(0, 1, randomEngine);
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = uniform.nextDouble() < p ? Math.exp(normal.nextDouble()) : 0.0;
        }
        return new Data(data);
    }

}

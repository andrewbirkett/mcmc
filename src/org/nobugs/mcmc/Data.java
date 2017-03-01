package org.nobugs.mcmc;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

public class Data {

    public static Datapoints generateNormal(double mean, double sd, int n, RandomEngine randomEngine) {
        Normal normal = new Normal(mean, sd, randomEngine);

        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = normal.nextDouble();
        }
        return new Datapoints(data);
    }

    public static Datapoints generateLogNormal(double logMean, double logSd, int n, RandomEngine randomEngine) {
        Normal normal = new Normal(logMean, logSd, randomEngine);

        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = Math.exp(normal.nextDouble());
        }
        return new Datapoints(data);
    }

    public static Datapoints generateZeroInflatedLogNormal(double p, double logMean, double logSd, int n, RandomEngine randomEngine) {
        Normal normal = new Normal(logMean, logSd, randomEngine);
        Uniform uniform = new Uniform(0, 1, randomEngine);
        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = uniform.nextDouble() < p ? Math.exp(normal.nextDouble()) : 0.0;
        }
        return new Datapoints(data);
    }

}

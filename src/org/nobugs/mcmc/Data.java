package org.nobugs.mcmc;

import cern.jet.random.Normal;
import cern.jet.random.engine.RandomEngine;

public class Data {

    public static double[] generate(double mean, double sd, int n, RandomEngine randomEngine) {
        Normal normal = new Normal(mean, sd, randomEngine);

        double[] data = new double[n];
        for (int i = 0; i < n; i++) {
            data[i] = normal.nextDouble();
        }
        return data;
    }
}

package org.nobugs.mcmc;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class MeanTracer implements Tracer {
    private SummaryStatistics[] stats;

    public MeanTracer() {
    }

    private void initialize(int n) {
        this.stats = new SummaryStatistics[n];
        for (int i = 0; i < n; i++) {
            stats[i] = new SummaryStatistics();
        }
    }

    @Override
    public void update(double[] parameters, double probability) {
        if (stats == null) {
            initialize(parameters.length);
        }

        for (int i = 0; i < parameters.length; i++) {
            stats[i].addValue(parameters[i]);
        }
    }

    public double[] means() {
        double[] result = new double[stats.length];
        for (int i = 0; i < stats.length; i++) {
            result[i] = stats[i].getMean();
        }
        return result;
    }
}

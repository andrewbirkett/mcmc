package org.nobugs.mcmc.diagnostics;

public class MeanTracer implements Tracer {
    private double[] sums;
    private int count;

    public MeanTracer() {
    }

    private void initialize(int n) {
        this.sums = new double[n];
        for (int i = 0; i < n; i++) {
            sums[i] = 0;
        }
    }

    @Override
    public void update(double[] parameters, double probability) {
        if (sums == null) {
            initialize(parameters.length);
        }

        for (int i = 0; i < parameters.length; i++) {
            sums[i] += parameters[i];
        }
        count++;
    }

    public double[] means() {
        double[] result = new double[sums.length];
        for (int i = 0; i < sums.length; i++) {
            result[i] = sums[i] / count;
        }
        return result;
    }
}

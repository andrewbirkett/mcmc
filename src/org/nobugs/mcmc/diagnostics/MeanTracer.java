package org.nobugs.mcmc.diagnostics;

public class MeanTracer implements Tracer {
    private double[] sums;
    private int count;

    public MeanTracer() {
    }

    @Override
    public void update(double[] parameters) {
        if (sums == null) {
            lazyInitialize(parameters.length);
        }

        for (int i = 0; i < parameters.length; i++) {
            sums[i] += parameters[i];
        }
        count++;
    }

    private void lazyInitialize(int n) {
        this.sums = new double[n];
        for (int i = 0; i < n; i++) {
            sums[i] = 0;
        }
    }

    public double[] means() {
        double[] result = new double[sums.length];
        for (int i = 0; i < sums.length; i++) {
            result[i] = sums[i] / count;
        }
        return result;
    }
}

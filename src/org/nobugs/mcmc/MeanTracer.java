package org.nobugs.mcmc;

public class MeanTracer implements Tracer {
    private double sumMu;
    private double sumSigma;
    private int count;

    public MeanTracer() {
        this.sumMu = 0;
        this.sumSigma = 0;
        this.count = 0;
    }

    @Override
    public void update(double mu, double sigma, double probability) {
        sumMu += mu;
        sumSigma += sigma;
        count++;
    }

    public double meanMu() {
        return sumMu / count;
    }

    public double meanSigma() {
        return sumSigma / count;
    }
}

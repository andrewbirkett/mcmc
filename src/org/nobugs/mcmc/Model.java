package org.nobugs.mcmc;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.nobugs.mcmc.Monitor.StepOutcome.ACCEPTED;
import static org.nobugs.mcmc.Monitor.StepOutcome.REJECTED;

public class Model {
    private final List<Tracer> tracers;
    private final double[] data;
    private double mu;
    private double sigma;
    private Normal proposal;
    private Uniform uniform;
    private Monitor monitor;

    public Model(RandomEngine randomEngine, Monitor monitor, double[] data, double initMu, double initSigma) throws Exception {
        this.proposal = new Normal(0, 0.01, randomEngine);
        this.uniform = new Uniform(0, 1, randomEngine);
        this.monitor = monitor;
        this.data = data;
        this.tracers = new ArrayList<>();
        this.mu = initMu;
        this.sigma = initSigma;
    }

    public void addTracer(Tracer tracer) {
        tracers.add(tracer);
    }

    public void update() throws IOException {
        double deltaMu = proposal.nextDouble(); // proposal.nextDouble();
        double deltaSigma = proposal.nextDouble();

        double muProposed = mu + deltaMu;
        double sigmaProposed = sigma + deltaSigma;

        if (sigmaProposed < 0) return;

        double currentDensity = density(mu, sigma, data);
        double proposedDensity = density(muProposed, sigmaProposed, data);

        for (Tracer tracer : tracers) {
            tracer.update(mu, sigma, currentDensity);
        }

        if (proposedDensity > currentDensity) {
            monitor.recordStep(ACCEPTED);
            mu = muProposed;
            sigma = sigmaProposed;
        } else {
            double ratio = proposedDensity / currentDensity;
            double roll = uniform.nextDouble();
            if (roll < ratio) {
                monitor.recordStep(ACCEPTED);
                mu = muProposed;
                sigma = sigmaProposed;
            } else {
                monitor.recordStep(REJECTED);
            }
        }
    }

    private double density(double mu, double sigma, double[] data) {
        double variance = sigma * sigma;
        double sqrt_inv = 1.0 / Math.sqrt(2.0 * Math.PI * variance);

        double sum = 0;
        for (double d : data) {
            double delta = d - mu;
            sum += -(delta * delta) / (2.0 * variance);
        }

        return Math.pow(sqrt_inv, data.length) * Math.exp(sum);
    }

}
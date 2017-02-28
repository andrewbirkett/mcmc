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
    private Normal proposal;
    private Uniform uniform;
    private Monitor monitor;
    private double[] parameters;

    public Model(RandomEngine randomEngine, Monitor monitor, double[] data, double[] parameters) throws Exception {
        this.proposal = new Normal(0, 0.01, randomEngine);
        this.uniform = new Uniform(0, 1, randomEngine);
        this.monitor = monitor;
        this.data = data;
        this.tracers = new ArrayList<>();
        this.parameters = parameters;
    }

    private static double logdensity(double[] parameters, double[] data) {
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

    public void addTracer(Tracer tracer) {
        tracers.add(tracer);
    }

    public void update() throws IOException {
        double[] delta = new double[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            delta[i] = proposal.nextDouble();
        }

        double[] proposed = new double[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            proposed[i] = parameters[i] + delta[i];
        }

        if (proposed[1] < 0) return;

        double currentDensity = logdensity(parameters, data);
        double proposedDensity = logdensity(proposed, data);

        for (Tracer tracer : tracers) {
            tracer.update(parameters, currentDensity);
        }

        if (proposedDensity > currentDensity) {
            monitor.recordStep(ACCEPTED);
            parameters = proposed;
        } else {
            double ratio = Math.exp(proposedDensity - currentDensity);
            double roll = uniform.nextDouble();
            if (roll < ratio) {
                monitor.recordStep(ACCEPTED);
                parameters = proposed;
            } else {
                monitor.recordStep(REJECTED);
            }

        }
    }
}
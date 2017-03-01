package org.nobugs.mcmc;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import org.nobugs.mcmc.distribution.Distribution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.nobugs.mcmc.Monitor.StepOutcome.ACCEPTED;
import static org.nobugs.mcmc.Monitor.StepOutcome.REJECTED;

public class Model {
    private final List<Tracer> tracers;
    private final Datapoints data;
    private final Distribution distribution;
    private Normal proposal;
    private Uniform uniform;
    private Monitor monitor;
    private double[] parameters;

    public Model(RandomEngine randomEngine, Monitor monitor, Datapoints data, double[] parameters, Distribution distribution) throws Exception {
        this.proposal = new Normal(0, 0.01, randomEngine);
        this.uniform = new Uniform(0, 1, randomEngine);
        this.monitor = monitor;
        this.data = data;
        this.tracers = new ArrayList<>();
        this.parameters = parameters;
        this.distribution = distribution;
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

        double currentDensity = distribution.logdensity(parameters, data);
        double proposedDensity = distribution.logdensity(proposed, data);

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
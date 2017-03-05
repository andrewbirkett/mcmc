package org.nobugs.mcmc.sampler;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import org.nobugs.mcmc.Data;
import org.nobugs.mcmc.diagnostics.Monitor;
import org.nobugs.mcmc.diagnostics.Tracer;
import org.nobugs.mcmc.likelihood.Likelihood;

import java.io.IOException;

import static org.nobugs.mcmc.diagnostics.Monitor.StepOutcome.ACCEPTED;
import static org.nobugs.mcmc.diagnostics.Monitor.StepOutcome.REJECTED;

public class MetropolisHastings implements Sampler {
    private final Data data;
    private final Likelihood likelihood;
    private Tracer tracer;
    private Normal proposal;
    private Uniform uniform;
    private Monitor monitor;
    private double[] parameters;

    public MetropolisHastings(RandomEngine randomEngine, Monitor monitor, Data data, double[] parameters, Likelihood likelihood, Normal proposal) throws Exception {
        this.proposal = proposal;
        this.uniform = new Uniform(0, 1, randomEngine);
        this.monitor = monitor;
        this.data = data;
        this.tracer = null;
        this.parameters = parameters;
        this.likelihood = likelihood;
    }

    @Override
    public void addTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void update() throws IOException {
        double[] delta = new double[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            delta[i] = proposal.nextDouble();
        }

        double[] proposed = new double[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            proposed[i] = parameters[i] + delta[i];
        }

        double currentDensity = likelihood.logLikelihood(parameters, data);
        double proposedDensity = likelihood.logLikelihood(proposed, data);

        if (tracer != null) {
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
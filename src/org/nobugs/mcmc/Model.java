package org.nobugs.mcmc;

import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.RandomEngine;
import org.apache.log4j.Logger;

import java.io.IOException;

import static org.nobugs.mcmc.Monitor.StepOutcome.ACCEPTED;
import static org.nobugs.mcmc.Monitor.StepOutcome.REJECTED;

public class Model {
    private static Logger LOG = Logger.getLogger(Model.class);

    private final Coda coda;
    private double mu = 2.5;
    private double sigma = 0.7;
    private Normal proposal;
    private Uniform uniform;
    private Monitor monitor;

    public Model(RandomEngine randomEngine, Coda coda, Monitor monitor) throws Exception {
        this.proposal = new Normal(0, 0.01, randomEngine);
        this.uniform = new Uniform(0, 1, randomEngine);
        this.monitor = monitor;
        this.coda = coda;
    }

    public void update() throws IOException {
        LOG.debug("Mu: " + mu + " Sigma: " + sigma);

        double deltaMu = proposal.nextDouble(); // proposal.nextDouble();
        double deltaSigma = proposal.nextDouble();
        LOG.debug("dMu: " + deltaMu + " dSigma: " + deltaSigma);

        double muProposed = mu + deltaMu;
        double sigmaProposed = sigma + deltaSigma;
        LOG.debug("Mu': " + muProposed + " Sigma': " + sigmaProposed);

        if (sigmaProposed < 0) return;

        double currentDensity = density(mu, sigma, Config.data);
        double proposedDensity = density(muProposed, sigmaProposed, Config.data);

        coda.add(mu, sigma, currentDensity);
        LOG.debug("Current: " + currentDensity + " Proposed: " + proposedDensity);

        if (proposedDensity > currentDensity) {
            LOG.debug("Jumping to higher probability");
            monitor.recordStep(ACCEPTED);
            mu = muProposed;
            sigma = sigmaProposed;
        } else {
            double ratio = proposedDensity / currentDensity;
            double roll = uniform.nextDouble();
            LOG.debug("Ratio is " + ratio);
            LOG.debug("Roll is " + roll);
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
        double density = 1.0;
        for (double d : data) {
            density *= density(mu, sigma, d);
        }
        return density;
    }

    private double density(double mu, double sigma, double data) {
        return new Normal(mu, sigma, null).pdf(data);
    }
}
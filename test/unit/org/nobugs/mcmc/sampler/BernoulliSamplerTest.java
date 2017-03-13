package org.nobugs.mcmc.sampler;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nobugs.mcmc.Data;
import org.nobugs.mcmc.diagnostics.MeanTracer;
import org.nobugs.mcmc.diagnostics.Monitor;
import org.nobugs.mcmc.likelihood.BernoulliLikelihood;
import org.nobugs.mcmc.likelihood.Likelihood;
import org.nobugs.mcmc.prior.IndependentPrior;
import org.nobugs.mcmc.prior.JointPrior;
import org.nobugs.mcmc.prior.simple.BetaPrior;
import org.nobugs.mcmc.utils.Generator;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)

public class BernoulliSamplerTest {

    private static final MersenneTwister RANDOM_ENGINE = new MersenneTwister();

    private final double theta;
    private final double priorAlpha;
    private final double priorBeta;

    public BernoulliSamplerTest(double theta, double priorAlpha, double priorBeta) {
        this.theta = theta;
        this.priorAlpha = priorAlpha;
        this.priorBeta = priorBeta;
    }

    @Parameterized.Parameters(name = "theta={0}, priorA={1}, priorB={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // theta, prior-alpha, prior-beta
                {0.1, 1, 1},
                {0.5, 1, 1},
                {0.8, 1, 1},

                {0.1, 2, 1},
                {0.1, 2, 2},
                {0.1, 1, 10},
        });
    }

    @Test
    public void test() throws Exception {
        MersenneTwister randomEngine = new MersenneTwister(4);

        int datapoints = 100;
        Data data = Generator.bernoulli(theta, datapoints, randomEngine);
        double expectedPosteriorMean = expectedPosteriorMean(data);

        try (Monitor monitor = new Monitor()) {
            int burnin = 100_000;
            int nsteps = 100_000;

            double[] inits = {0.5};
            Likelihood likelihood = new BernoulliLikelihood();
            AbstractDistribution[] proposal = new AbstractDistribution[]{
                    new Normal(0, 0.05, randomEngine)
            };

            JointPrior prior = new IndependentPrior(new BetaPrior(priorAlpha, priorBeta, RANDOM_ENGINE));
            Sampler sampler = new MetropolisHastings(randomEngine, monitor, data, inits, likelihood, proposal, prior);
            for (int i = 0; i < burnin; i++) {
                sampler.update();
            }

            MeanTracer tracer = new MeanTracer();
            sampler.addTracer(tracer);

            for (int i = 0; i < nsteps; i++) {
                sampler.update();
            }

            double[] means = tracer.means();
            assertThat(means[0], closeTo(expectedPosteriorMean, 0.01));
        }
    }

    private double expectedPosteriorMean(Data data) {
        int successes = data.getNonZeroValues().size();
        int failures = data.getZeros();

        double posteriorAlpha = priorAlpha + successes;
        double posteriorBeta = priorBeta + failures;

        return betaMean(posteriorAlpha, posteriorBeta);
    }

    private double betaMean(double alpha, double beta) {
        return alpha / (alpha + beta);
    }

}
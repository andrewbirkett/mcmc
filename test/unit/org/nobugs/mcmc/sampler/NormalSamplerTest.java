package org.nobugs.mcmc.sampler;

import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nobugs.mcmc.Data;
import org.nobugs.mcmc.diagnostics.MeanTracer;
import org.nobugs.mcmc.diagnostics.Monitor;
import org.nobugs.mcmc.likelihood.Likelihood;
import org.nobugs.mcmc.likelihood.NormalLikelihood;
import org.nobugs.mcmc.prior.IndependentPrior;
import org.nobugs.mcmc.prior.JointPrior;
import org.nobugs.mcmc.prior.simple.UniformPrior;
import org.nobugs.mcmc.utils.Generator;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class NormalSamplerTest {

    private final double mu;
    private final double sigma;

    public NormalSamplerTest(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    @Parameterized.Parameters(name = "mu={0}, sigma={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {8, 2},
                {0, 1},
                {3, 3}
        });
    }

    @Test
    public void test() throws Exception {
        MersenneTwister randomEngine = new MersenneTwister();
        
        int datapoints = 100;

        Data data = Generator.normal(mu, sigma, datapoints, randomEngine);
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        for (int i = 0; i < data.getAll().size(); i++) {
            double d = data.getAll().get(i);
            summaryStatistics.addValue(d);
        }
        double sampleMean = summaryStatistics.getMean();
        double sampleSd = summaryStatistics.getStandardDeviation();
        try (Monitor monitor = new Monitor()) {
            int burnin = 100_000;
            int nsteps = 100_000;

            double[] inits = {5, 3};
            Likelihood likelihood = new NormalLikelihood();
            Normal proposal = new Normal(0, 0.1, randomEngine);

            JointPrior prior = new IndependentPrior(
                    new UniformPrior(-2,12, randomEngine),
                    new UniformPrior(0,10, randomEngine)
            );
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
            assertThat(means[0], closeTo(sampleMean, 0.1));
            assertThat(means[1], closeTo(sampleSd, 0.1));
        }
    }

}
package org.nobugs.mcmc.sampler;

import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nobugs.mcmc.Data;
import org.nobugs.mcmc.diagnostics.MeanTracer;
import org.nobugs.mcmc.diagnostics.Monitor;
import org.nobugs.mcmc.distribution.BernoulliDistribution;
import org.nobugs.mcmc.distribution.Distribution;
import org.nobugs.mcmc.utils.Generator;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class BernoulliSamplerTest {

    @Test
    public void test() throws Exception {

        MersenneTwister randomEngine = new MersenneTwister();

        int datapoints = 100;
        double theta = 0.1;
        Data data = Generator.bernoulli(theta, datapoints, randomEngine);
        try (Monitor monitor = new Monitor()) {
            int burnin = 100_000;
            int nsteps = 100_000;

            double[] inits = {0.5};
            Distribution likelihood = new BernoulliDistribution();
            Normal proposal = new Normal(0, 1, randomEngine);
            Sampler sampler = new MetropolisHastings(randomEngine, monitor, data, inits, likelihood, proposal);
            for (int i = 0; i < burnin; i++) {
                sampler.update();
            }

            MeanTracer tracer = new MeanTracer();
            sampler.addTracer(tracer);
            for (int i = 0; i < nsteps; i++) {
                sampler.update();
            }

            double[] means = tracer.means();
            assertThat(means[0], closeTo(0.1, 0.01));
            System.out.println("MeanTheta:" + means[0]);
        }
    }

}
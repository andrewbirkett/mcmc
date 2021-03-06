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
import org.nobugs.mcmc.likelihood.Likelihood;
import org.nobugs.mcmc.likelihood.ZeroInflatedLogNormalLikelihood;
import org.nobugs.mcmc.prior.*;
import org.nobugs.mcmc.prior.simple.BetaPrior;
import org.nobugs.mcmc.prior.simple.UniformPrior;
import org.nobugs.mcmc.utils.Generator;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ZeroInflatedLogNormalSamplerTest {

    private final double p;
    private final double logmu;
    private final double logsigma;

    public ZeroInflatedLogNormalSamplerTest(double p, double logmu, double logsigma) {
        this.p = p;
        this.logmu = logmu;
        this.logsigma = logsigma;
    }

    @Parameterized.Parameters(name = "p={0} logmu={1}, logsigma={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0.5, 0, 1},
                {0.8, 0, 1},
                {0.9, 0, 1},

                {0.5, 1, 2},
        });
    }

    @Test
    public void test() throws Exception {
        MersenneTwister randomEngine = new MersenneTwister();

        int numDatapoints = 10_000;
        Data data = Generator.zeroInflatedLogNormal(p, logmu, logsigma, numDatapoints, randomEngine);

        try (Monitor monitor = new Monitor()) {
            int burnin = 10_000;
            int nsteps = 10_000;

            double[] inits = {0.5, 0, 1};
            Likelihood likelihood = new ZeroInflatedLogNormalLikelihood();
            AbstractDistribution[] proposal = new AbstractDistribution[]{
                    new Normal(0, 0.01, randomEngine),
                    new Normal(0, 0.01, randomEngine),
                    new Normal(0, 0.01, randomEngine)
            };

            JointPrior prior = new IndependentPrior(new BetaPrior(1,1, randomEngine),
                    new UniformPrior(0, 10, randomEngine),
                    new UniformPrior( 0, 10, randomEngine ));
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
            assertThat(means[0], closeTo(p, 0.1));
            assertThat(means[1], closeTo(logmu, 0.1));
            assertThat(means[2], closeTo(logsigma, 0.1));
        }
    }
}
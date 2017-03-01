package org.nobugs.mcmc;

import cern.jet.random.engine.MersenneTwister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nobugs.mcmc.distribution.Distribution;
import org.nobugs.mcmc.distribution.ZeroInflatedLogNormalDistribution;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ZeroInflatedLogNormalModelTest {

    private final double p;
    private final double logmu;
    private final double logsigma;

    public ZeroInflatedLogNormalModelTest(double p, double logmu, double logsigma) {
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

        int datapoints = 10_000; // 5 secs for 10k data points and 10k jumps
        // => for 22Mm datapoints would be 3 hours
        double[] data = Data.generateZeroInflatedLogNormal(p, logmu, logsigma, datapoints, randomEngine);

        try (Monitor monitor = new Monitor()) {
            int burnin = 10_000;
            int nsteps = 10_000;

            double[] inits = {0.5, 0, 1};
            Distribution likelihood = new ZeroInflatedLogNormalDistribution();
            Model model = new Model(randomEngine, monitor, data, inits, likelihood);
            for (int i = 0; i < burnin; i++) {
                model.update();
            }

            MeanTracer tracer = new MeanTracer();
            model.addTracer(tracer);
            for (int i = 0; i < nsteps; i++) {
                model.update();
            }

            double[] means = tracer.means();
            System.out.println("P: " + means[0] + " MeanMu:" + means[1] + " meanSigma:" + means[2]);
            assertThat(means[0], closeTo(p, 0.1));
            assertThat(means[1], closeTo(logmu, 0.1));
            assertThat(means[2], closeTo(logsigma, 0.1));
        }
    }

}
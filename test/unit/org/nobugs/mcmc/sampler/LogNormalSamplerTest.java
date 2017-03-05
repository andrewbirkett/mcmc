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
import org.nobugs.mcmc.distribution.Distribution;
import org.nobugs.mcmc.distribution.LogNormalDistribution;
import org.nobugs.mcmc.utils.Generator;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class LogNormalSamplerTest {

    private final double logmu;
    private final double logsigma;

    public LogNormalSamplerTest(double logmu, double logsigma) {
        this.logmu = logmu;
        this.logsigma = logsigma;
    }

    @Parameterized.Parameters(name = "logmu={0}, logsigma={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {3.5, 2}, {0, 1}, {3, 3}
        });
    }

    @Test
    public void test() throws Exception {
        MersenneTwister randomEngine = new MersenneTwister();

        int n = 100;
        Data data = Generator.logNormal(logmu, logsigma, n, randomEngine);
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        for (double d : data.getAll()) {
            summaryStatistics.addValue(Math.log(d));
        }
        double sampleLogMean = summaryStatistics.getMean();
        double sampleLogSd = summaryStatistics.getStandardDeviation();
        try (Monitor monitor = new Monitor()) {
            int burnin = 100_000;
            int nsteps = 100_000;

            double[] inits = {3, 5};
            Distribution likelihood = new LogNormalDistribution();
            Normal proposal = new Normal(0, 0.01, randomEngine);
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
            assertThat(means[0], closeTo(sampleLogMean, 0.1));
            assertThat(means[1], closeTo(sampleLogSd, 0.1));
            System.out.println("MeanMu:" + means[0] + " meanSigma:" + means[1]);
        }
    }

}
package org.nobugs.mcmc;

import cern.jet.random.engine.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nobugs.mcmc.distribution.Distribution;
import org.nobugs.mcmc.distribution.LogNormalDistribution;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class LogNormalModelTest {

    private final double logmu;
    private final double logsigma;

    public LogNormalModelTest(double logmu, double logsigma) {
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

        int datapoints = 100;
        double[] data = Data.generateLogNormal(logmu, logsigma, datapoints, randomEngine);
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        for (double d : data) {
            summaryStatistics.addValue(Math.log(d));
        }
        double sampleLogMean = summaryStatistics.getMean();
        double sampleLogSd = summaryStatistics.getStandardDeviation();
        try (Monitor monitor = new Monitor()) {
            int burnin = 100_000;
            int nsteps = 100_000;

            double[] inits = {3, 5};
            Distribution likelihood = new LogNormalDistribution();
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
            assertThat(means[0], closeTo(sampleLogMean, 0.1));
            assertThat(means[1], closeTo(sampleLogSd, 0.1));
            System.out.println("MeanMu:" + means[0] + " meanSigma:" + means[1]);
        }
    }

}
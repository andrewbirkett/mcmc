package org.nobugs.mcmc;

import cern.jet.random.engine.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class ModelTest {

    private final double mu;
    private final double sigma;

    public ModelTest(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    @Parameterized.Parameters(name = "mu={0}, sigma={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {10, 2}, {0, 1}, {3, 3}
        });
    }

    @Test
    public void test() throws Exception {

        MersenneTwister randomEngine = new MersenneTwister();

        int datapoints = 100;
        double[] data = Data.generate(mu, sigma, datapoints, randomEngine);
        SummaryStatistics summaryStatistics = new SummaryStatistics();
        for (double d : data) {
            summaryStatistics.addValue(d);
        }
        double sampleMean = summaryStatistics.getMean();
        double sampleSd = summaryStatistics.getStandardDeviation();
        try (Monitor monitor = new Monitor()) {
            int burnin = 100_000;
            int nsteps = 100_000;

            double[] inits = {5, 3};
            Model model = new Model(randomEngine, monitor, data, inits);
            for (int i = 0; i < burnin; i++) {
                model.update();
            }

            MeanTracer tracer = new MeanTracer();
            model.addTracer(tracer);
            for (int i = 0; i < nsteps; i++) {
                model.update();
            }

            double[] means = tracer.means();
            assertThat(means[0], closeTo(sampleMean, 0.1));
            assertThat(means[1], closeTo(sampleSd, 0.1));
            System.out.println("MeanMu:" + means[0] + " meanSigma:" + means[1]);
        }
    }

}
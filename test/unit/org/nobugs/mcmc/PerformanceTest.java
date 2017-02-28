package org.nobugs.mcmc;

import cern.jet.random.engine.MersenneTwister;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

public class PerformanceTest {

    @Test
    public void performance() throws Exception {

        MersenneTwister randomEngine = new MersenneTwister();

        int datapoints = 100;
        double[] data = Data.generate(0, 1, datapoints, randomEngine);

        try (Monitor monitor = new Monitor()) {
            int warmup = 100_000;
            int nsteps = 1_000_000;

            Model model = new Model(randomEngine, monitor, data, new double[]{1, 1.5});
            for (int i = 0; i < warmup; i++) {
                model.update();
            }

            StopWatch stopwatch = new StopWatch();
            stopwatch.start();
            for (int i = 0; i < nsteps; i++) {
                model.update();
            }
            stopwatch.stop();
            System.out.println("Took " + stopwatch.getTime() + "ms for " + nsteps + " steps with " +
                    datapoints + " datapoints (av " + 1000 * stopwatch.getTime() / nsteps + "us)");
        }
    }
}
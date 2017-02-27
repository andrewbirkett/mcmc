package org.nobugs.mcmc;

import cern.jet.random.engine.MersenneTwister;
import org.junit.Test;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class ModelTest {

    @Test
    public void test() throws Exception {

        MersenneTwister randomEngine = new MersenneTwister();

        int datapoints = 50;
        double[] data = Data.generate(10, 2, datapoints, randomEngine);


        try (Monitor monitor = new Monitor()) {
            int burnin = 100_000;
            int nsteps = 100_000;

            MeanTracer tracer = new MeanTracer();
            Model model = new Model(randomEngine, tracer, monitor, data, 5, 3);
            for (int i = 0; i < burnin; i++) {
                model.update();
            }

            for (int i = 0; i < nsteps; i++) {
                model.update();
            }

            assertThat(tracer.meanMu(), closeTo(10, 0.5));
            assertThat(tracer.meanSigma(), closeTo(2, 0.2));
            System.out.println("MeanMu:" + tracer.meanMu() + " meanSigma:" + tracer.meanSigma());
        }
    }

}
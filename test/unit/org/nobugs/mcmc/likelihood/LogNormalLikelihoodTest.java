package org.nobugs.mcmc.likelihood;

import org.junit.Test;
import org.nobugs.mcmc.Data;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class LogNormalLikelihoodTest {
    @Test
    public void test() throws Exception {
        assertThat(likelihood(new double[]{0, 1}, new Data(1)), closeTo(0.3989423, 1e-5));
        assertThat(likelihood(new double[]{0, 1}, new Data(2)), closeTo(0.156874, 1e-5));
        assertThat(likelihood(new double[]{1, 1}, new Data(1)), closeTo(0.2419707, 1e-5));
        assertThat(likelihood(new double[]{0, 2}, new Data(1)), closeTo(0.1994711, 1e-5));
        assertThat(likelihood(new double[]{0, 2}, new Data(2)), closeTo(0.0939221, 1e-5));
        assertThat(likelihood(new double[]{10, 2}, new Data(22000)), closeTo(9.066868e-06, 1e-5));

    }

    private double likelihood(double[] parameters, Data data) {
        return Math.exp(new LogNormalLikelihood().logLikelihood(parameters, data));
    }
}
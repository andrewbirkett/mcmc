package org.nobugs.mcmc.distribution;

import org.junit.Test;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class LogNormalDistributionTest {
    @Test
    public void probabilityDensity() throws Exception {
        assertThat(probabilityDensity(new double[]{0, 1}, new double[]{1}), closeTo(0.3989423, 1e-5));
        assertThat(probabilityDensity(new double[]{0, 1}, new double[]{2}), closeTo(0.156874, 1e-5));
        assertThat(probabilityDensity(new double[]{1, 1}, new double[]{1}), closeTo(0.2419707, 1e-5));
        assertThat(probabilityDensity(new double[]{0, 2}, new double[]{1}), closeTo(0.1994711, 1e-5));
        assertThat(probabilityDensity(new double[]{0, 2}, new double[]{2}), closeTo(0.0939221, 1e-5));
        assertThat(probabilityDensity(new double[]{10, 2}, new double[]{22000}), closeTo(9.066868e-06, 1e-5));

    }

    private double probabilityDensity(double[] parameters, double[] data) {
        return Math.exp(new LogNormalDistribution().logdensity(parameters, data));
    }
}
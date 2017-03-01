package org.nobugs.mcmc.distribution;

import org.junit.Test;
import org.nobugs.mcmc.Datapoints;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class ZeroInflatedLogNormalDistributionTest {
    @Test
    public void probabilityDensity() throws Exception {
        assertThat(probabilityDensity(new double[]{0.9, 0, 1}, new Datapoints(0)), closeTo(0.1, 1e-5));
        assertThat(probabilityDensity(new double[]{0.9, 0, 1}, new Datapoints(1)), closeTo(0.9 * 0.3989423, 1e-5));

        assertThat(probabilityDensity(new double[]{0.9, 0, 1}, new Datapoints(0, 0)), closeTo(0.01, 1e-5));
        assertThat(probabilityDensity(new double[]{0.9, 0, 1}, new Datapoints(0, 1)), closeTo(0.9 * 0.3989423 * 0.1, 1e-5));
    }

    private double probabilityDensity(double[] parameters, Datapoints data) {
        return Math.exp(new ZeroInflatedLogNormalDistribution().logdensity(parameters, data));
    }
}
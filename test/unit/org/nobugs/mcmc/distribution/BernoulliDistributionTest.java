package org.nobugs.mcmc.distribution;

import org.junit.Test;
import org.nobugs.mcmc.Data;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class BernoulliDistributionTest {
    @Test
    public void probabilityDensity() throws Exception {
        assertThat(probabilityDensity(new double[]{0.9}, new Data(0)), closeTo(0.1, 1e-5));
        assertThat(probabilityDensity(new double[]{0.9}, new Data(1)), closeTo(0.9, 1e-5));
        assertThat(probabilityDensity(new double[]{0.9}, new Data(1,1)), closeTo(0.81, 1e-5));
        assertThat(probabilityDensity(new double[]{0.9}, new Data(1,0)), closeTo(0.09, 1e-5));
        assertThat(probabilityDensity(new double[]{0.9}, new Data(0,0)), closeTo(0.01, 1e-5));
    }

    @Test
    public void extreme() throws Exception {
        assertThat(probabilityDensity(new double[]{0}, new Data(0)), is(1.0));
        assertThat(probabilityDensity(new double[]{0}, new Data(1)), is(0.0));
        assertThat(probabilityDensity(new double[]{1}, new Data(0)), is(0.0));
        assertThat(probabilityDensity(new double[]{1}, new Data(1)), is(1.0));
    }

    private double probabilityDensity(double[] parameters, Data data) {
        return Math.exp(new BernoulliDistribution().logdensity(parameters, data));
    }
}
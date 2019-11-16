package org.nobugs.mcmc.likelihood;

import org.junit.Test;
import org.nobugs.mcmc.Data;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class BernoulliLikelihoodTest {
    @Test
    public void test() {
        assertThat(likelihood(new double[]{0.9}, new Data(0)), closeTo(0.1, 1e-5));
        assertThat(likelihood(new double[]{0.9}, new Data(1)), closeTo(0.9, 1e-5));
        assertThat(likelihood(new double[]{0.9}, new Data(1, 1)), closeTo(0.81, 1e-5));
        assertThat(likelihood(new double[]{0.9}, new Data(1, 0)), closeTo(0.09, 1e-5));
        assertThat(likelihood(new double[]{0.9}, new Data(0, 0)), closeTo(0.01, 1e-5));
    }

    @Test
    public void extreme() {
        assertThat(likelihood(new double[]{0}, new Data(0)), is(1.0));
        assertThat(likelihood(new double[]{0}, new Data(1)), is(0.0));
        assertThat(likelihood(new double[]{1}, new Data(0)), is(0.0));
        assertThat(likelihood(new double[]{1}, new Data(1)), is(1.0));
    }

    private double likelihood(double[] parameters, Data data) {
        return Math.exp(new BernoulliLikelihood().logLikelihood(parameters, data));
    }
}
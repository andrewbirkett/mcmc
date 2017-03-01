package org.nobugs.mcmc;

public class Datapoints {
    private double[] xs;


    public Datapoints(double... xs) {
        this.xs = xs;
    }

    public double[] get() {
        return xs;
    }

    public int size() {
        return xs.length;
    }
}

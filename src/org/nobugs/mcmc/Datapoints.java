package org.nobugs.mcmc;

import cern.colt.list.DoubleArrayList;

public class Datapoints {
    private DoubleArrayList data;
    private int zeros;

    public Datapoints(double... xs) {
        this.data = new DoubleArrayList();
        for (double x : xs) {
            if (x == 0) {
                zeros++;
            } else {
                data.add(x);
            }
        }
    }

    public double[] getAll() {
        if (zeros > 0) throw new IllegalStateException("TODO: pad with zeros");
        return data.elements();
    }

    public int getZeros() {
        return zeros;
    }

    public DoubleArrayList getNonZeroValues() {
        return data;
    }

    public int size() {
        return data.size() + zeros;
    }
}

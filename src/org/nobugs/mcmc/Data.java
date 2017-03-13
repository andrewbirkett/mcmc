package org.nobugs.mcmc;

import cern.colt.list.DoubleArrayList;

public class Data {
    private final DoubleArrayList data;
    private int zeros;

    public Data(double... xs) {
        this(0, xs);
    }

    public Data(int numZeros, double... rest) {
        this.data = new DoubleArrayList();
        this.zeros = numZeros;
        for (double d : rest) {
            add(d);
        }
    }

    private void add(double x) {
        if (x == 0) {
            zeros++;
        } else {
            data.add(x);
        }
    }

    public DoubleArrayList getAll() {
        if (zeros > 0) throw new IllegalStateException("TODO: pad with zeros");
        return data;
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

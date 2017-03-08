package org.nobugs.mcmc;

import cern.colt.list.DoubleArrayList;

public class Data {
    private final DoubleArrayList data;
    private int zeros;

    public Data(double... xs) {
        this.data = new DoubleArrayList();
        for (double x : xs) {
            if (x == 0) {
                zeros++;
            } else {
                data.add(x);
            }
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

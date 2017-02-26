package org.nobugs.mcmc;

import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;

public class Main {
    public static void main(String[] args) {
        MersenneTwister randomEngine = new MersenneTwister();
        Normal normal = new Normal(0, 1, randomEngine);
        System.out.println(normal.pdf(0));
    }
}

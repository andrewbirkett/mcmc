package org.nobugs.mcmc;

import cern.jet.random.engine.MersenneTwister;
import org.nobugs.mcmc.distribution.Distribution;
import org.nobugs.mcmc.distribution.NormalDistribution;

import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        int seed = (int) System.currentTimeMillis();

        MersenneTwister randomEngine = new MersenneTwister(seed);

        try (Monitor monitor = new Monitor();
             Coda coda = new Coda(Paths.get("/tmp", "coda.txt"))) {
            Distribution likelihood = new NormalDistribution();
            Model model = new Model(randomEngine, monitor, Config.data, new double[]{0, 1}, likelihood);
            model.addTracer(coda);
            for (int i = 0; i < Config.jumps; i++) {
                model.update();
            }
        }
    }
}

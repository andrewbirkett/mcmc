package org.nobugs.mcmc.sampler;

import org.nobugs.mcmc.diagnostics.Tracer;

import java.io.IOException;

public interface Sampler {
    void addTracer(Tracer tracer);

    void update() throws IOException;
}
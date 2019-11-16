package org.nobugs.mcmc.sampler;

import org.nobugs.mcmc.diagnostics.Tracer;

interface Sampler {
    void addTracer(Tracer tracer);
    void update();
}

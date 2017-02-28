package org.nobugs.mcmc;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class Coda implements Tracer, Closeable {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    private final OutputStream outputStream;

    public Coda(Path filename) throws IOException {
        this.outputStream = new BufferedOutputStream(new FileOutputStream(filename.toFile()));
        IOUtils.write("mu,sigma,p\n", outputStream, "UTF-8");
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    public void update(double[] parameters, double logProbabilityDensity) {
        try {
            String string = StringUtils.join(parameters, ',') + "," + Double.toString(logProbabilityDensity) + "\n";
            IOUtils.write(string, outputStream, CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

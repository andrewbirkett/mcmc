package org.nobugs.mcmc;

import org.apache.commons.io.IOUtils;

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

    public void update(double mu, double sigma, double p) {
        try {
            String string = Double.toString(mu) + "," + Double.toString(sigma) + "," + Double.toString(p) + "\n";
            IOUtils.write(string, outputStream, CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
